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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.audit

import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import org.mockito.Mockito.{times, verify}
import org.mockito.{ArgumentCaptor, Mockito}
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.AuditHelper
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.models.{ApiFailureResponseEventModel, ChildTaxApiResponseEventModel, IfApiResponseEventModel, ScopesAuditEventModel, WorkingTaxApiResponseEventModel}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits.{CtcApplication, CtcAward}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{IfApplication, IfApplications}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.{WtcApplication, WtcAward}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.ExtendedDataEvent
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

import scala.concurrent.ExecutionContext.Implicits.global

class AuditHelperSpec extends UnitSpec with MockitoSugar {

  implicit val hc = HeaderCarrier()

  val nino = "CS700100A"
  val correlationId = "test"
  val scopes = "test"
  val matchId = "80a6bb14-d888-436e-a541-4000674c60aa"
  val request = FakeRequest()
  val response = Json.toJson("some" -> "json")
  val ifUrl =
    s"host/individuals/benefits-and-credits/child-tax-credit/nino/$nino?startDate=2019-01-01&endDate=2020-01-01&fields=some(vals(val1),val2)"
  val endpoint = "/test"

  val auditConnector = mock[AuditConnector]

  val workingTaxCreditResponse = Seq(WtcApplication(None, Seq(WtcAward(None, None, None, None, None))))
  val childTaxCreditResponse = Seq(CtcApplication(None, Seq(CtcAward(None, None, None, None))))
  val ifResponse = IfApplications(Seq(IfApplication(None, None, None, None, None)))

  val auditHelper = new AuditHelper(auditConnector)

  "Auth helper" should {

    "auditAuthScopes" in {

      Mockito.reset(auditConnector)

      val captor = ArgumentCaptor.forClass(classOf[ScopesAuditEventModel])

      auditHelper.auditAuthScopes(matchId, scopes, request)

      verify(auditConnector, times(1)).sendExplicitAudit(eqTo("AuthScopesAuditEvent"),
        captor.capture())(any(), any(), any())

      val capturedEvent = captor.getValue.asInstanceOf[ScopesAuditEventModel]
      capturedEvent.apiVersion shouldEqual "1.0"
      capturedEvent.matchId shouldEqual matchId
      capturedEvent.scopes shouldBe scopes

    }

    "childTaxApiResponseEvent" in {

      Mockito.reset(auditConnector)

      val captor = ArgumentCaptor.forClass(classOf[ChildTaxApiResponseEventModel])

      auditHelper.childTaxCreditAuditApiResponse(correlationId, matchId, scopes, request, endpoint, childTaxCreditResponse)

      verify(auditConnector, times(1)).sendExplicitAudit(eqTo("ApiResponseEvent"),
        captor.capture())(any(), any(), any())

      val capturedEvent = captor.getValue.asInstanceOf[ChildTaxApiResponseEventModel]
      capturedEvent.matchId shouldEqual matchId
      capturedEvent.correlationId shouldEqual Some(correlationId)
      capturedEvent.scopes shouldBe scopes
      capturedEvent.returnLinks shouldBe endpoint
      capturedEvent.childTaxCredit shouldBe childTaxCreditResponse
      capturedEvent.apiVersion shouldBe "1.0"

    }

    "workingTaxApiResponseEvent" in {

      Mockito.reset(auditConnector)

      val captor = ArgumentCaptor.forClass(classOf[ChildTaxApiResponseEventModel])

      auditHelper.workingTaxCreditAuditApiResponse(correlationId, matchId, scopes, request, endpoint, workingTaxCreditResponse)

      verify(auditConnector, times(1)).sendExplicitAudit(eqTo("ApiResponseEvent"),
        captor.capture())(any(), any(), any())

      val capturedEvent = captor.getValue.asInstanceOf[WorkingTaxApiResponseEventModel]
      capturedEvent.matchId shouldEqual matchId
      capturedEvent.correlationId shouldEqual Some(correlationId)
      capturedEvent.scopes shouldBe scopes
      capturedEvent.returnLinks shouldBe endpoint
      capturedEvent.workingTaxCredit shouldBe workingTaxCreditResponse
      capturedEvent.apiVersion shouldBe "1.0"

    }

    "auditApiFailure" in {

      Mockito.reset(auditConnector)

      val msg = "Something went wrong"

      val captor = ArgumentCaptor.forClass(classOf[ApiFailureResponseEventModel])

      auditHelper.auditApiFailure(Some(correlationId), matchId, request, "/test", msg)

      verify(auditConnector, times(1)).sendExplicitAudit(eqTo("ApiFailureEvent"),
        captor.capture())(any(), any(), any())

      val capturedEvent = captor.getValue.asInstanceOf[ApiFailureResponseEventModel]
      capturedEvent.matchId shouldEqual matchId
      capturedEvent.correlationId shouldEqual Some(correlationId)
      capturedEvent.requestUrl shouldEqual endpoint
      capturedEvent.response shouldEqual msg

    }

    "auditIfApiResponse" in {

      Mockito.reset(auditConnector)

      val captor = ArgumentCaptor.forClass(classOf[IfApiResponseEventModel])

      auditHelper.auditIfApiResponse(correlationId, matchId, request, ifUrl, ifResponse)

      verify(auditConnector, times(1)).sendExplicitAudit(eqTo("IfApiResponseEvent"),
        captor.capture())(any(), any(), any())

      val capturedEvent = captor.getValue.asInstanceOf[IfApiResponseEventModel]
      capturedEvent.matchId shouldEqual matchId
      capturedEvent.correlationId shouldEqual correlationId
      capturedEvent.requestUrl shouldBe ifUrl
      capturedEvent.ifApplications shouldBe ifResponse

    }

    "auditIfApiFailure" in {

      Mockito.reset(auditConnector)

      val msg = "Something went wrong"

      val captor = ArgumentCaptor.forClass(classOf[ApiFailureResponseEventModel])

      auditHelper.auditIfApiFailure(correlationId, matchId, request, ifUrl, msg)

      verify(auditConnector, times(1)).sendExplicitAudit(eqTo("IfApiFailureEvent"),
        captor.capture())(any(), any(), any())

      val capturedEvent = captor.getValue.asInstanceOf[ApiFailureResponseEventModel]
      capturedEvent.matchId shouldEqual matchId
      capturedEvent.correlationId shouldEqual Some(correlationId)
      capturedEvent.requestUrl shouldEqual ifUrl
      capturedEvent.response shouldEqual msg

    }
  }
}
