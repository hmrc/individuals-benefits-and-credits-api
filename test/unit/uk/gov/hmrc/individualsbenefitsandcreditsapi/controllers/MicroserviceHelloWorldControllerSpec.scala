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
import org.scalatest.{BeforeAndAfterEach, Matchers}
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.Status._
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.config.AppConfig
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.MicroserviceHelloWorldController
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.actions.IdentifierAction
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

class MicroserviceHelloWorldControllerSpec
    extends SpecBase
    with MockitoSugar
    with Matchers
    with BeforeAndAfterEach {
  implicit lazy val materializer: Materializer = fakeApplication.materializer

  trait Fixture {
    implicit val hc = HeaderCarrier()
  }

  val env = "TEST"
  val conf = mock[AppConfig]
  val ia = mock[IdentifierAction]
  val ac = mock[AuthConnector]
  val mockMicroserviceHelloWorldController =
    new MicroserviceHelloWorldController(conf, ia, ac, env, cc)

  "hello function" should {

    "return hello" in new Fixture {
      val result = await(mockMicroserviceHelloWorldController.hello()(any()))
      status(result) shouldBe OK
      bodyOf(result) shouldBe "Hello world"
    }
  }

}
