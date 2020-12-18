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
import play.api.libs.json.Json
import play.api.test.Helpers._
import scalaj.http.Http

import java.util.UUID

class SandboxWorkingTaxCreditControllerSpec extends BaseSpec {

  val rootScope = "read:individuals-benefits-and-credits-working-tax-credit"
  private val matchId = UUID.randomUUID()
  private val fromDate = "2017-01-01"
  private val toDate = "2017-09-25"

  feature("Sandbox Working Tax Credit Controller") {
    scenario("Valid Request to working-tax-credits endpoint") {
      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to working-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/sandbox/working-tax-credit?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("The response status should be 500")
      response.code shouldBe OK
    }
    scenario("missing match Id") {

      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to working-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/working-tax-credit/?fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("The response status should be 400")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "matchId is required"
      )
    }

    scenario("missing from date") {

      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to working-tax-credit endpoint")
      val response =
        Http(s"$serviceUrl/working-tax-credit/?matchId=$matchId&toDate=$toDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("The response status should be 400")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "fromDate is required"
      )
    }
  }

}
