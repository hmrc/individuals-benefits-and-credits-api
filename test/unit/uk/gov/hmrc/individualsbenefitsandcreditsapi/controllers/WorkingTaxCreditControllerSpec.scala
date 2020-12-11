/*
 * Copyright 2020 HM Revenue & Customs
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
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.auth.core.{AuthConnector, Enrolment, EnrolmentIdentifier, Enrolments}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.{LiveWorkingTaxCreditController, SandboxWorkingTaxCreditController}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{LiveTaxCreditsService, SandboxTaxCreditsService, TaxCreditsService}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class WorkingTaxCreditControllerSpec extends SpecBase with MockitoSugar {
  implicit lazy val materializer: Materializer = fakeApplication.materializer
  private val testUUID = UUID.fromString("be2dbba5-f650-47cf-9753-91cdaeb16ebe")
  private val fromDate = new LocalDate("2017-03-02").toDateTimeAtStartOfDay
  private val toDate = new LocalDate("2017-05-31").toDateTimeAtStartOfDay
  private val testInterval = new Interval(fromDate, toDate)


  private val enrolments = Enrolments(
    Set(
      Enrolment("read:hello-scopes-1",
                Seq(EnrolmentIdentifier("FOO", "BAR")),
                "Activated"),
      Enrolment("read:hello-scopes-2",
                Seq(EnrolmentIdentifier("FOO2", "BAR2")),
                "Activated"),
      Enrolment("read:hello-scopes-3",
                Seq(EnrolmentIdentifier("FOO3", "BAR3")),
                "Activated")
    )
  )

  private def fakeAuthConnector(stubbedRetrievalResult: Future[_]) =
    new AuthConnector {

      def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(
          implicit hc: HeaderCarrier,
          ec: ExecutionContext): Future[A] = {
        stubbedRetrievalResult.map(_.asInstanceOf[A])
      }
    }

  private def myRetrievals = Future.successful(
    enrolments
  )

  trait Fixture {

    val scopeService = mock[ScopesService]
    val liveTaxCreditsService = mock[LiveTaxCreditsService]
    val sandboxTaxCreditsService = mock[SandboxTaxCreditsService]

    val scopes: Iterable[String] =
      Iterable("read:hello-scopes-1", "read:hello-scopes-2")

    val liveWorkingTaxCreditsController =
      new LiveWorkingTaxCreditController(
        fakeAuthConnector(myRetrievals),
        cc,
        scopeService,
        liveTaxCreditsService
      )

    val sandboxWorkingTaxCreditsController =
      new SandboxWorkingTaxCreditController(
        fakeAuthConnector(myRetrievals),
        cc,
        scopeService,
        sandboxTaxCreditsService
      )

    when(scopeService.getEndPointScopes(any())).thenReturn(scopes)
  }

  "working tax credits controller" when {
    "the live controller" should {
      "the working tax credit function" should {
        "throw an exception" in new Fixture {

          val fakeRequest =
            FakeRequest("GET", s"/working-tax-credits/")

          val result =
            intercept[Exception] {
              await(
                liveWorkingTaxCreditsController.workingTaxCredit(testUUID, testInterval)(fakeRequest))
            }
          assert(result.getMessage == "NOT_IMPLEMENTED")
        }

        "return error when no scopes" in new Fixture {
          when(scopeService.getEndPointScopes(any())).thenReturn(None)

          val fakeRequest =
            FakeRequest("GET", s"/working-tax-credits/")

          val result =
            intercept[Exception] {
              await(
                liveWorkingTaxCreditsController.workingTaxCredit(testUUID, testInterval)(fakeRequest))
            }
          assert(result.getMessage == "No scopes defined")
        }
      }
    }

    "the sandbox controller" should {
      "the working tax credit function" should {
        "throw an exception" in new Fixture {

          val fakeRequest =
            FakeRequest("GET", s"/sandbox/working-tax-credits/")

          val result =
            intercept[Exception] {
              await(
                sandboxWorkingTaxCreditsController.workingTaxCredit(testUUID, testInterval)(
                  fakeRequest))
            }
          assert(result.getMessage == "NOT_IMPLEMENTED")
        }
      }
    }
  }
}
