/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import akka.stream.Materializer
import org.joda.time.{Interval, LocalDate}
import org.mockito.ArgumentMatchers.{any, refEq, eq => eqTo}
import org.mockito.Mockito.{verifyNoInteractions, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.FakeRequest
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{
  AuthConnector,
  Enrolment,
  Enrolments,
  InsufficientEnrolments
}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.{
  LiveWorkingTaxCreditController,
  SandboxWorkingTaxCreditController
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.MatchNotFoundException
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{
  LiveTaxCreditsService,
  SandboxTaxCreditsService
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.DomainHelpers
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class WorkingTaxCreditControllerSpec
    extends SpecBase
    with MockitoSugar
    with DomainHelpers {

  implicit lazy val materializer: Materializer = fakeApplication.materializer
  private val testMatchId =
    UUID.fromString("be2dbba5-f650-47cf-9753-91cdaeb16ebe")
  private val fromDate = new LocalDate("2017-03-02").toDateTimeAtStartOfDay
  private val toDate = new LocalDate("2017-05-31").toDateTimeAtStartOfDay
  private val testInterval = new Interval(fromDate, toDate)

  trait Fixture {

    val scopeService = mock[ScopesService]
    val liveTaxCreditsService = mock[LiveTaxCreditsService]
    val sandboxTaxCreditsService = mock[SandboxTaxCreditsService]
    val mockAuthConnector: AuthConnector = mock[AuthConnector]

    when(
      mockAuthConnector.authorise(
        eqTo(Enrolment("test-scope")),
        refEq(Retrievals.allEnrolments))(any(), any()))
      .thenReturn(Future.successful(Enrolments(Set(Enrolment("test-scope")))))

    val scopes: Iterable[String] =
      Iterable("test-scope")

    val liveWorkingTaxCreditsController =
      new LiveWorkingTaxCreditController(
        mockAuthConnector,
        cc,
        scopeService,
        liveTaxCreditsService
      )

    val sandboxWorkingTaxCreditsController =
      new SandboxWorkingTaxCreditController(
        mockAuthConnector,
        cc,
        scopeService,
        sandboxTaxCreditsService
      )

    when(scopeService.getEndPointScopes(any())).thenReturn(scopes)

    implicit val hc: HeaderCarrier = HeaderCarrier()

    implicit val executionContext =
      fakeApplication.injector.instanceOf[ExecutionContext]
  }

  "working tax credits controller" when {
    "the live controller" should {
      "the working tax credit function" should {
        "Return Applications when successful" in new Fixture {

          val fakeRequest = FakeRequest("GET", s"/working-tax-credits/")

          when(
            liveTaxCreditsService.getWorkingTaxCredits(
              eqTo(testMatchId),
              eqTo(testInterval),
              eqTo(List("test-scope")))(any(), any(), any()))
            .thenReturn(
              Future.successful(
                Seq(createValidWtcApplication(), createValidWtcApplication()))
            )

          val result =
            liveWorkingTaxCreditsController
              .workingTaxCredit(testMatchId, testInterval)(fakeRequest)

          status(result) shouldBe OK
        }

        "return 404 (not found) for an invalid matchId" in new Fixture {
          when(
            liveTaxCreditsService.getWorkingTaxCredits(
              eqTo(testMatchId),
              eqTo(testInterval),
              eqTo(List("test-scope")))(any(), any(), any()))
            .thenReturn(
              Future.failed(new MatchNotFoundException)
            )

          val fakeRequest = FakeRequest("GET", s"/working-tax-credits/")

          val result = liveWorkingTaxCreditsController.workingTaxCredit(
            testMatchId,
            testInterval)(fakeRequest)

          status(result) shouldBe NOT_FOUND

          contentAsJson(result) shouldBe Json.obj(
            "code" -> "NOT_FOUND",
            "message" -> "The resource can not be found"
          )
        }

        "return 401 when the bearer token does not have enrolment test-scope" in new Fixture {

          when(mockAuthConnector.authorise(any(), any())(any(), any()))
            .thenReturn(Future.failed(InsufficientEnrolments()))

          val fakeRequest = FakeRequest("GET", s"/working-tax-credits/")

          val result = liveWorkingTaxCreditsController.workingTaxCredit(
            testMatchId,
            testInterval)(fakeRequest)

          status(result) shouldBe UNAUTHORIZED
          verifyNoInteractions(liveTaxCreditsService)
        }

        "return error when no scopes" in new Fixture {
          when(scopeService.getEndPointScopes(any())).thenReturn(None)

          val fakeRequest =
            FakeRequest("GET", s"/working-tax-credits/")

          val result =
            intercept[Exception] {
              await(
                liveWorkingTaxCreditsController
                  .workingTaxCredit(testMatchId, testInterval)(fakeRequest))
            }
          assert(result.getMessage == "No scopes defined")
        }
      }
    }
  }
}
