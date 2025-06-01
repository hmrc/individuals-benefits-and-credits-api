/*
 * Copyright 2025 HM Revenue & Customs
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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.handlers

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import play.api.http.Status
import play.api.test.FakeRequest
import uk.gov.hmrc.individualsbenefitsandcreditsapi.handlers.CustomErrorHandler
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent

import scala.concurrent.ExecutionContext

class CustomErrorHandlerSpec extends AsyncWordSpec with Matchers with MockitoSugar with ScalaFutures {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  private val mockAuditConnector = mock[AuditConnector]
  private val mockHttpAuditEvent = mock[HttpAuditEvent]
  private val configuration = mock[Configuration]

  private val errorHandler =
    new CustomErrorHandler(mockAuditConnector, mockHttpAuditEvent, configuration)

  "CustomErrorHandler" should {
    "return NOT_FOUND response for 404 error" in {
      val fakeRequest = FakeRequest("GET", "/some/resource")
      errorHandler.onClientError(fakeRequest, Status.NOT_FOUND, "Not Found").map { result =>
        result.header.status shouldBe Status.NOT_FOUND
      }
    }

    "return BAD_REQUEST response for 400 error" in {
      val fakeRequest = FakeRequest("POST", "/some/resource")
      val errorMessage = "Invalid request format"
      errorHandler
        .onClientError(fakeRequest, Status.BAD_REQUEST, errorMessage)
        .map { result =>
          result.header.status shouldBe Status.BAD_REQUEST
        }
    }

    "handle other client errors" in {
      val fakeRequest = FakeRequest("GET", "/some/resource")
      val statusCode = 444
      val message = "I'm a teapot"
      errorHandler
        .onClientError(fakeRequest, statusCode, message)
        .map { result =>
          result.header.status shouldBe statusCode
        }
    }
  }
}
