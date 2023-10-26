/*
 * Copyright 2023 HM Revenue & Customs
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

import component.uk.gov.hmrc.individualsbenefitsandcreditsapi.stubs.{AuthStub, BaseSpec}
import play.api.libs.json.Json
import play.api.test.Helpers._
import scalaj.http.Http

import java.util.UUID

trait CommonControllerSpec extends BaseSpec {

  val rootScope: List[String]
  val endpoint: String

  val matchId: UUID
  val fromDate: String
  val toDate: String

  Feature("Common Controller Methods") {

    Scenario("Missing match id") {

      When("the root entry point to the API is invoked with a missing match id")
      val response = invokeEndpoint(s"$serviceUrl/$endpoint")

      Then("the response status should be 400 (bad request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "matchId is required"
      )
    }

    Scenario("malformed match id") {

      When(
        "the root entry point to the API is invoked with a malformed match id")
      val response =
        invokeEndpoint(
          s"$serviceUrl/${endpoint}?matchId=malformed-match-id-value")

      Then("the response status should be 400 (bad request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "matchId format is invalid"
      )
    }

    Scenario("invalid match id") {

      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When(
        "the root entry point to the API is invoked with an invalid match id")
      val response = invokeEndpoint(
        s"$serviceUrl/${endpoint}?matchId=0a184ef3-fd75-4d4d-b6a3-f886cc39a366&fromDate=$fromDate")

      Then("the response status should be 404 (not found)")
      response.code shouldBe NOT_FOUND
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "NOT_FOUND",
        "message" -> "The resource can not be found"
      )
    }

    Scenario("missing from date") {

      Given("A valid auth token ")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When(s"I make a call to ${endpoint} endpoint")
      val response =
        Http(s"$serviceUrl/${endpoint}?matchId=$matchId&toDate=$toDate")
          .headers(requestHeaders(acceptHeader1))
          .asString

      Then("The response status should be 400")

      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "fromDate is required"
      )
    }

    Scenario("toDate earlier than fromDate") {

      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When(
        s"the ${endpoint} endpoint is invoked with an toDate earlier than fromDate")
      val response =
        Http(
          s"$serviceUrl/${endpoint}?matchId=$matchId&fromDate=$toDate&toDate=$fromDate")
          .headers(requestHeaders(acceptHeader1))
          .asString

      Then("the response status should be 400 (invalid request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "Invalid time period requested"
      )
    }

    Scenario("From date requested is earlier than 31st March 2013") {

      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      When(
        s"the ${endpoint} endpoint is invoked with toDate before 31st March 2013")
      val response = Http(
        s"$serviceUrl/${endpoint}?matchId=$matchId&fromDate=2012-01-01&toDate=$toDate")
        .headers(requestHeaders(acceptHeader1))
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
