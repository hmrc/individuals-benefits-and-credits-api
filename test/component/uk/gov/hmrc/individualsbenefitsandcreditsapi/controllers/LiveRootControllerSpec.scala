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

package component.uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import component.uk.gov.hmrc.individualsbenefitsandcreditsapi.stubs.{
  AuthStub,
  BaseSpec
}
import scalaj.http.Http
import play.api.test.Helpers._

class LiveRootControllerSpec extends BaseSpec {

  val rootScope = List(
    "read:individuals-benefits-and-credits-hmcts-c2",
    "read:individuals-benefits-and-credits-hmcts-c3",
    "read:individuals-benefits-and-credits-laa-c1",
    "read:individuals-benefits-and-credits-laa-c2",
    "read:individuals-benefits-and-credits-laa-c3",
    "read:individuals-benefits-and-credits-lsani-c1",
    "read:individuals-benefits-and-credits-lsani-c3"
  )

  feature("Live Root Controller") {
    scenario("root route") {
      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to root endpoint")
      val response =
        Http(s"$serviceUrl/").headers(requestHeaders(acceptHeaderP1)).asString

      Then("The response tatus should be 500")
      response.code shouldBe INTERNAL_SERVER_ERROR

    }
  }

}
