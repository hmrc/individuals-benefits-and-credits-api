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
import org.mockito.ArgumentMatchers._
import org.mockito.BDDMockito.`given`
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.Status._
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.{AuthConnector, Enrolment}
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.{
  LiveMicroserviceHelloWorldController,
  SandboxMicroserviceHelloWorldController
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase
import play.api.libs.json.Reads

import scala.concurrent.Future.successful

class MicroserviceHelloWorldControllerSpec extends SpecBase with MockitoSugar {
  implicit lazy val materializer: Materializer = fakeApplication.materializer

  val env = "TEST"

  object testRetrieval extends Retrieval[Unit] {
    val propertyNames = Seq("allEnrolments")

    def reads: Reads[Unit] = Reads.pure(())
  }

  val pred = Enrolment("read:hello-scopes-1") and Enrolment(
    "read:hello-scopes-2")

  trait Fixture {

    val ac = mock[AuthConnector]

    val liveMicroserviceHelloWorldController =
      new LiveMicroserviceHelloWorldController(ac, cc)
    val sandboxMicroserviceHelloWorldController =
      new SandboxMicroserviceHelloWorldController(ac, cc)

    given(ac.authorise(refEq(pred), refEq(testRetrieval))(any(), any()))
      .willReturn(successful(()))
  }

  "hello world" when {

    "hello function" should {

      "return hello world" in new Fixture {

        val fakeRequest =
          FakeRequest("GET", s"/individuals/income/paye")

        val result =
          await(liveMicroserviceHelloWorldController.hello()(fakeRequest))
        status(result) shouldBe OK
        bodyOf(result) shouldBe "Hello world"
      }
    }

    "hello Scopes" should {

      "return hello world" in new Fixture {

        val fakeRequest =
          FakeRequest("GET", s"/individuals/income/paye/2")

        val result =
          await(liveMicroserviceHelloWorldController.helloScopes()(fakeRequest))
        status(result) shouldBe OK
        bodyOf(result) shouldBe "Hello world"
      }
    }
  }
}
