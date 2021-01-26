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

import java.util.UUID
import akka.stream.Materializer
import org.mockito.ArgumentMatchers.{any, refEq, eq => eqTo}
import org.mockito.Mockito.{verifyNoInteractions, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{
  AuthConnector,
  Enrolment,
  Enrolments,
  InsufficientEnrolments
}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{BadRequestException, HeaderCarrier}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.{
  LiveRootController,
  SandboxRootController
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.{
  MatchNotFoundException,
  MatchedCitizen
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.{
  ScopesHelper,
  ScopesService
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{
  LiveTaxCreditsService,
  SandboxTaxCreditsService
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.config.ScopesConfigHelper
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import scala.concurrent.{ExecutionContext, Future}

class RootControllerSpec extends SpecBase with MockitoSugar {

  val sampleCorrelationId = "188e9400-b636-4a3b-80ba-230a8c72b92a"
  val correlationIdHeader = "CorrelationId" -> sampleCorrelationId

  implicit lazy val materializer: Materializer = fakeApplication.materializer

  private val testMatchId =
    UUID.fromString("be2dbba5-f650-47cf-9753-91cdaeb16ebe")

  trait Fixture extends ScopesConfigHelper {

    implicit val ec: ExecutionContext =
      fakeApplication.injector.instanceOf[ExecutionContext]
    lazy val scopeService: ScopesService = new ScopesService(mockScopesConfig)
    lazy val scopesHelper: ScopesHelper = new ScopesHelper(scopeService)

    val liveTaxCreditsService = mock[LiveTaxCreditsService]
    val sandboxTaxCreditsService = mock[SandboxTaxCreditsService]
    val mockAuthConnector: AuthConnector = mock[AuthConnector]

    val testNino = Nino("AB123456C")

    when(
      mockAuthConnector.authorise(
        eqTo(Enrolment("test-scope")),
        refEq(Retrievals.allEnrolments))(any(), any()))
      .thenReturn(Future.successful(Enrolments(Set(Enrolment("test-scope")))))

    val liveRootController =
      new LiveRootController(
        mockAuthConnector,
        cc,
        scopeService,
        scopesHelper,
        liveTaxCreditsService
      )

    val sandboxRootController =
      new SandboxRootController(
        mockAuthConnector,
        cc,
        scopeService,
        scopesHelper,
        sandboxTaxCreditsService
      )

  }

  "Root" should {
    "return a 404 (not found) when a match id does not match live data" in new Fixture {

      when(liveTaxCreditsService.resolve(eqTo(testMatchId))(any[HeaderCarrier]))
        .thenReturn(Future.failed(new MatchNotFoundException))

      val eventualResult = liveRootController.root(testMatchId)(
        FakeRequest().withHeaders(correlationIdHeader))

      status(eventualResult) shouldBe NOT_FOUND
      contentAsJson(eventualResult) shouldBe Json.obj(
        "code" -> "NOT_FOUND",
        "message" -> "The resource can not be found"
      )
    }

    "return a 200 (ok) when a match id matches live data" in new Fixture {

      when(liveTaxCreditsService.resolve(eqTo(testMatchId))(any[HeaderCarrier]))
        .thenReturn(Future.successful(MatchedCitizen(testMatchId, testNino)))

      val eventualResult = liveRootController.root(testMatchId)(
        FakeRequest().withHeaders(correlationIdHeader))

      status(eventualResult) shouldBe OK
      contentAsJson(eventualResult) shouldBe Json.obj(
        "_links" -> Json.obj(
          "child-tax-credit" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/child-tax-credit?matchId=$testMatchId{&fromDate,toDate}",
            "title" -> "Get an individual's child tax credits data"
          ),
          "working-tax-credit" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/working-tax-credit?matchId=$testMatchId{&fromDate,toDate}",
            "title" -> "Get an individual's working tax credits data"
          ),
          "self" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/?matchId=$testMatchId"
          )
        )
      )
    }

    "fail with status 401 when the bearer token does not have enrolment test-scope" in new Fixture {

      when(mockAuthConnector.authorise(any(), any())(any(), any()))
        .thenReturn(Future.failed(InsufficientEnrolments()))

      val result = liveRootController.root(testMatchId)(
        FakeRequest().withHeaders(correlationIdHeader))

      status(result) shouldBe UNAUTHORIZED
      verifyNoInteractions(liveTaxCreditsService)
    }

    "not require bearer token authentication for sandbox" in new Fixture {

      when(
        sandboxTaxCreditsService.resolve(eqTo(testMatchId))(any[HeaderCarrier]))
        .thenReturn(Future.successful(MatchedCitizen(testMatchId, testNino)))

      val result = sandboxRootController.root(testMatchId)(
        FakeRequest().withHeaders(correlationIdHeader))

      status(result) shouldBe OK
      verifyNoInteractions(mockAuthConnector)
    }

    "throws an exception when missing CorrelationId Header" in new Fixture {
      when(
        sandboxTaxCreditsService.resolve(eqTo(testMatchId))(any[HeaderCarrier]))
        .thenReturn(Future.successful(MatchedCitizen(testMatchId, testNino)))

      val exception =
        intercept[BadRequestException](
          liveRootController.root(testMatchId)(FakeRequest()))

      exception.message shouldBe "CorrelationId is required"
      exception.responseCode shouldBe BAD_REQUEST
    }

    "throws an exception when CorrelationId Header is malformed" in new Fixture {
      when(
        sandboxTaxCreditsService.resolve(eqTo(testMatchId))(any[HeaderCarrier]))
        .thenReturn(Future.successful(MatchedCitizen(testMatchId, testNino)))

      val exception =
        intercept[BadRequestException](
          liveRootController.root(testMatchId)(
            FakeRequest().withHeaders("CorrelationId" -> "test")))

      exception.message shouldBe "Malformed CorrelationId"
      exception.responseCode shouldBe BAD_REQUEST
    }
  }
}
