/*
 * Copyright 2023 HM Revenue & Customs
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

import org.apache.pekko.stream.Materializer
import org.mockito.ArgumentMatchers.{any, eq => eqTo, refEq}
import org.mockito.Mockito
import org.mockito.Mockito.{times, verify, verifyNoInteractions, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{AuthConnector, Enrolment, Enrolments, InsufficientEnrolments}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.AuditHelper
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.WorkingTaxCreditController
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.MatchNotFoundException
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{ScopesService, TaxCreditsService}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.Interval
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.DomainHelpers
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import java.time.LocalDate
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class WorkingTaxCreditControllerSpec extends SpecBase with MockitoSugar with DomainHelpers {

  val sampleCorrelationId = "188e9400-b636-4a3b-80ba-230a8c72b92a"
  val correlationIdHeader: (String, String) = "CorrelationId" -> sampleCorrelationId

  implicit lazy val materializer: Materializer = fakeApplication().materializer
  private val testMatchId =
    UUID.fromString("be2dbba5-f650-47cf-9753-91cdaeb16ebe")
  private val fromDate = LocalDate.parse("2017-03-02").atStartOfDay()
  private val toDate = LocalDate.parse("2017-05-31").atStartOfDay()
  private val testInterval = Interval(fromDate, toDate)

  trait Fixture {

    implicit val executionContext: ExecutionContext =
      fakeApplication().injector.instanceOf[ExecutionContext]

    val scopeService: ScopesService = mock[ScopesService]
    val liveTaxCreditsService: TaxCreditsService = mock[TaxCreditsService]
    val mockAuthConnector: AuthConnector = mock[AuthConnector]
    val auditHelper: AuditHelper = mock[AuditHelper]

    when(mockAuthConnector.authorise(eqTo(Enrolment("test-scope")), refEq(Retrievals.allEnrolments))(any(), any()))
      .thenReturn(Future.successful(Enrolments(Set(Enrolment("test-scope")))))

    val scopes: Iterable[String] =
      Iterable("test-scope")

    val workingTaxCreditsController =
      new WorkingTaxCreditController(
        mockAuthConnector,
        cc,
        scopeService,
        auditHelper,
        liveTaxCreditsService
      )

    when(scopeService.getEndPointScopes(any())).thenReturn(scopes)

    implicit val hc: HeaderCarrier = HeaderCarrier()

  }

  "working tax credits controller" when {
    "the live controller" should {
      "the working tax credit function" should {
        "Return Applications when successful" in new Fixture {

          Mockito.reset(workingTaxCreditsController.auditHelper)

          val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
            FakeRequest("GET", s"/working-tax-credits/")
              .withHeaders(correlationIdHeader)

          when(
            liveTaxCreditsService
              .getWorkingTaxCredits(eqTo(testMatchId), eqTo(testInterval), eqTo(Set("test-scope")))(any(), any(), any())
          )
            .thenReturn(
              Future.successful(Seq(createValidWtcApplication(), createValidWtcApplication()))
            )

          val result: Future[Result] =
            workingTaxCreditsController
              .workingTaxCredit(testMatchId, testInterval)(fakeRequest)

          status(result) shouldBe OK

          verify(workingTaxCreditsController.auditHelper, times(1))
            .workingTaxCreditAuditApiResponse(any(), any(), any(), any(), any(), any())(any())

          verify(workingTaxCreditsController.auditHelper, times(1))
            .auditAuthScopes(any(), any(), any())(any())
        }

        "return 404 (not found) for an invalid matchId" in new Fixture {

          Mockito.reset(workingTaxCreditsController.auditHelper)

          when(
            liveTaxCreditsService
              .getWorkingTaxCredits(eqTo(testMatchId), eqTo(testInterval), eqTo(Set("test-scope")))(any(), any(), any())
          )
            .thenReturn(
              Future.failed(new MatchNotFoundException)
            )

          val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
            FakeRequest("GET", s"/working-tax-credits/")
              .withHeaders(correlationIdHeader)

          val result: Future[Result] = workingTaxCreditsController
            .workingTaxCredit(testMatchId, testInterval)(fakeRequest)

          status(result) shouldBe NOT_FOUND

          contentAsJson(result) shouldBe Json.obj(
            "code"    -> "NOT_FOUND",
            "message" -> "The resource can not be found"
          )

          verify(workingTaxCreditsController.auditHelper, times(1))
            .auditApiFailure(any(), any(), any(), any(), any())(any())
        }

        "return 401 when the bearer token does not have enrolment test-scope" in new Fixture {

          when(mockAuthConnector.authorise(any(), any())(any(), any()))
            .thenReturn(Future.failed(InsufficientEnrolments()))

          val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
            FakeRequest("GET", s"/working-tax-credits/")
              .withHeaders(correlationIdHeader)

          val result: Future[Result] = workingTaxCreditsController
            .workingTaxCredit(testMatchId, testInterval)(fakeRequest)

          status(result) shouldBe UNAUTHORIZED
          verifyNoInteractions(liveTaxCreditsService)
        }

        "return error when no scopes" in new Fixture {
          when(scopeService.getEndPointScopes(any())).thenReturn(List.empty)

          val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
            FakeRequest("GET", s"/working-tax-credits/")
              .withHeaders(correlationIdHeader)

          val result: Exception =
            intercept[Exception] {
              await(
                workingTaxCreditsController
                  .workingTaxCredit(testMatchId, testInterval)(fakeRequest)
              )
            }
          assert(result.getMessage == "No scopes defined")
        }
        "throws an exception when missing CorrelationId Header" in new Fixture {

          Mockito.reset(workingTaxCreditsController.auditHelper)

          val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
            FakeRequest("GET", s"/working-tax-credits/")

          when(
            liveTaxCreditsService
              .getWorkingTaxCredits(eqTo(testMatchId), eqTo(testInterval), eqTo(Set("test-scope")))(any(), any(), any())
          )
            .thenReturn(
              Future.successful(Seq(createValidWtcApplication(), createValidWtcApplication()))
            )

          val result: Future[Result] =
            workingTaxCreditsController
              .workingTaxCredit(testMatchId, testInterval)(fakeRequest)

          status(result) shouldBe BAD_REQUEST
          contentAsJson(result) shouldBe Json.parse(
            """{
              |    "code": "INVALID_REQUEST",
              |    "message": "CorrelationId is required"
              |}""".stripMargin
          )
          verify(workingTaxCreditsController.auditHelper, times(1))
            .auditApiFailure(any(), any(), any(), any(), any())(any())
        }

        "throws an exception when CorrelationId Header is malformed" in new Fixture {

          Mockito.reset(workingTaxCreditsController.auditHelper)

          val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
            FakeRequest("GET", s"/working-tax-credits/")
              .withHeaders("correlationId" -> "InvalidId")

          when(
            liveTaxCreditsService
              .getWorkingTaxCredits(eqTo(testMatchId), eqTo(testInterval), eqTo(Set("test-scope")))(any(), any(), any())
          )
            .thenReturn(
              Future.successful(Seq(createValidWtcApplication(), createValidWtcApplication()))
            )

          val result: Future[Result] =
            workingTaxCreditsController
              .workingTaxCredit(testMatchId, testInterval)(fakeRequest)

          status(result) shouldBe BAD_REQUEST
          contentAsJson(result) shouldBe Json.parse(
            """{
              |    "code": "INVALID_REQUEST",
              |    "message": "Malformed CorrelationId"
              |}""".stripMargin
          )
          verify(workingTaxCreditsController.auditHelper, times(1))
            .auditApiFailure(any(), any(), any(), any(), any())(any())
        }
      }
    }
  }
}
