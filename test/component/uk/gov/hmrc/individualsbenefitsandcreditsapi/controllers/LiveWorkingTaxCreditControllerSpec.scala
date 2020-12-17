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
  BaseSpec,
  IfStub,
  IndividualsMatchingApiStub
}
import play.api.libs.json.Json
import play.api.test.Helpers._
import scalaj.http.Http
import testUtils.TestHelpers

import java.util.UUID

class LiveWorkingTaxCreditControllerSpec extends BaseSpec with TestHelpers {

  val rootScope = List(
    "read:individuals-benefits-and-credits-hmcts-c2",
    "read:individuals-benefits-and-credits-hmcts-c3",
    "read:individuals-benefits-and-credits-laa-c1",
    "read:individuals-benefits-and-credits-laa-c2",
    "read:individuals-benefits-and-credits-laa-c3",
    "read:individuals-benefits-and-credits-lsani-c1",
    "read:individuals-benefits-and-credits-lsani-c3"
  )

  private val matchId = UUID.randomUUID()
  private val nino = "AB123456C"
  private val fromDate = "2017-01-01"
  private val toDate = "2017-09-25"

  private val applications = createValidIfApplicationsMultiple

  feature("Live working tax credit route") {
    scenario("valid request") {
      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      And("a valid record in the matching API")
      IndividualsMatchingApiStub.hasMatchFor(matchId.toString, nino)

      And("IF will return benefits and credits applications")
      IfStub.searchBenefitsAndCredits(nino, fromDate, toDate, applications)

      When("I make a call to working-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/working-tax-credit/?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("The response status should be 200")
      response.code shouldBe OK
    }

    scenario("missing match Id") {

      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      And("IF will return benefits and credits applications")
      IfStub.searchBenefitsAndCredits(nino, fromDate, toDate, applications)

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

      And("IF will return benefits and credits applications")
      IfStub.searchBenefitsAndCredits(nino, fromDate, toDate, applications)

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

    scenario("invalid token") {
      Given("an invalid token")
      AuthStub.willNotAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to working-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/working-tax-credit/?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("the response status should be 401 (unauthorized)")
      response.code shouldBe UNAUTHORIZED
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "UNAUTHORIZED",
        "message" -> "Bearer token is missing or not authorized"
      )
    }

    scenario("toDate earlier than fromDate") {
      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When(
        "the working tax credits endpoint is invoked with an toDate earlier than fromDate")
      val response =
        Http(
          s"$serviceUrl/working-tax-credit/?matchId=$matchId&fromDate=$toDate&toDate=$fromDate")
          .headers(requestHeaders(acceptHeaderP1))
          .asString

      Then("the response status should be 400 (invalid request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "Invalid time period requested"
      )
    }

    scenario("From date requested is earlier than 31st March 2013") {
      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When(
        "the workign tax credits endpoint is invoked with toDate before 31st March 2013")
      val response = Http(
        s"$serviceUrl/working-tax-credit/?matchId=$matchId&fromDate=2012-01-01&toDate=$toDate")
        .headers(requestHeaders(acceptHeaderP1))
        .asString

      Then("the response status should be 400 (invalid request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "fromDate earlier than 31st March 2013"
      )
    }

  }

}
