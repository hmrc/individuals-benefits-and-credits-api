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

package component.uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import component.uk.gov.hmrc.individualsbenefitsandcreditsapi.stubs.AuthStub
import play.api.libs.json.Json
import play.api.test.Helpers._
import scalaj.http.Http
import uk.gov.hmrc.individualsbenefitsandcreditsapi.sandbox.SandboxData

class SandboxChildTaxCreditControllerSpec extends CommonControllerSpec {

  val rootScope = List("read:individuals-benefits-and-credits-child-tax-credit")

  val endpoint = "sandbox/child-tax-credit"
  val matchId = SandboxData.sandboxMatchId
  val fromDate = "2017-01-01"
  val toDate = "2017-09-25"

  feature("Sandbox Child Tax Credit Controller") {

    scenario("Valid Request to child-tax-credits endpoint") {

      Given("A valid auth token")

      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to child-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/$endpoint?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("The response status should be 200 (ok)")
      response.code shouldBe OK
      Json.parse(response.body) shouldBe Json.obj(
        "_links" -> Json.obj(
          "self" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/child-tax-credits?matchId=$matchId&fromDate=$fromDate&toDate=$toDate"
          )
        ),
        "applications" -> Json.toJson(
          SandboxData.ChildTaxCredits.Applications.applications)
      )
    }
  }
}
