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

import java.util.UUID

import component.uk.gov.hmrc.individualsbenefitsandcreditsapi.stubs.{
  AuthStub,
  BaseSpec,
  IndividualsMatchingApiStub
}
import play.api.libs.json.Json
import play.api.test.Helpers._

class LiveRootControllerSpec extends BaseSpec {

  private val matchId = UUID.randomUUID().toString
  private val nino = "AB123456C"

  val allScopes = List(
    "read:individuals-benefits-and-credits-hmcts-c2",
    "read:individuals-benefits-and-credits-hmcts-c3",
    "read:individuals-benefits-and-credits-laa-c1",
    "read:individuals-benefits-and-credits-laa-c2",
    "read:individuals-benefits-and-credits-laa-c3",
    "read:individuals-benefits-and-credits-lsani-c1",
    "read:individuals-benefits-and-credits-lsani-c3"
  )

  feature("Root (hateoas) entry point is accessible") {

    scenario("invalid token") {
      Given("an invalid token")
      AuthStub.willNotAuthorizePrivilegedAuthToken(authToken, allScopes)

      When("the root entry point to the API is invoked")
      val response = invokeEndpoint(s"$serviceUrl/?matchId=$matchId")

      Then("the response status should be 401 (unauthorized)")
      response.code shouldBe UNAUTHORIZED
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "UNAUTHORIZED",
        "message" -> "Bearer token is missing or not authorized"
      )
    }

    scenario("missing match id") {
      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, allScopes)

      When("the root entry point to the API is invoked with a missing match id")
      val response = invokeEndpoint(serviceUrl)

      Then("the response status should be 400 (bad request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "matchId is required"
      )
    }

    scenario("malformed match id") {
      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, allScopes)

      When(
        "the root entry point to the API is invoked with a malformed match id")
      val response =
        invokeEndpoint(s"$serviceUrl/?matchId=malformed-match-id-value")

      Then("the response status should be 400 (bad request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "matchId format is invalid"
      )
    }

    scenario("invalid match id") {
      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, allScopes)

      When(
        "the root entry point to the API is invoked with an invalid match id")
      val response = invokeEndpoint(s"$serviceUrl/?matchId=$matchId")

      Then("the response status should be 404 (not found)")
      response.code shouldBe NOT_FOUND
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "NOT_FOUND",
        "message" -> "The resource can not be found"
      )
    }

    scenario("valid request to the live root endpoint implementation") {
      Given("a valid privileged Auth bearer token")
      AuthStub.willAuthorizePrivilegedAuthToken(authToken, allScopes)

      And("a valid record in the matching API")
      IndividualsMatchingApiStub.hasMatchFor(matchId, nino)

      When("the root entry point to the API is invoked with a valid match id")
      val response = invokeEndpoint(s"$serviceUrl/?matchId=$matchId")

      Then("the response status should be 200 (ok)")
      response.code shouldBe OK
      Json.parse(response.body) shouldBe Json.obj(
        "_links" -> Json.obj(
          "self" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/?matchId=$matchId"
          ),
          "working-tax-credit" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/working-tax-credit?matchId=$matchId{&fromDate,toDate}",
            "title" -> "Get an individual's working tax credits data"
          ),
          "child-tax-credit" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/child-tax-credit?matchId=$matchId{&fromDate,toDate}",
            "title" -> "Get an individual's child tax credits data"
          )
        )
      )
    }
  }
}
