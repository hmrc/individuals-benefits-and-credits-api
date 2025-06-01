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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import org.apache.pekko.stream.Materializer
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.Status
import play.api.test.Helpers.*
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.APIDocumentationController
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import scala.concurrent.ExecutionContext

class APIDocumentationControllerSpec extends SpecBase with MockitoSugar {
  implicit val materializer: Materializer = app.materializer
  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  val controller: APIDocumentationController = app.injector.instanceOf[APIDocumentationController]

  "APIDocumentationController" should {
    "return 200 and correct definition content for the definition endpoint" in {
      val result = controller.definition()(FakeRequest())
      status(result) shouldBe Status.OK
      headers(result).get(CONTENT_TYPE) shouldBe Some("application/json;charset=utf-8")
      contentAsString(result) should include("BETA")
    }

    "return status OK when documentation is requested" in {
      val result =
        controller.documentation("1.0", "test endpoint")(FakeRequest("GET", "/api/documentation/1.0/test-endpoint.xml"))
      status(result) mustBe OK
    }

    "return OK status when yaml is requested" in {
      val result = controller.yaml("1.0", "application.yaml")(FakeRequest("GET", "/api/conf/1.0/application.yaml"))
      status(result) mustBe OK
    }
  }
}
