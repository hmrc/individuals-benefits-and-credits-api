/*
 * Copyright 2022 HM Revenue & Customs
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
  IfStub,
  IndividualsMatchingApiStub
}
import play.api.libs.json.Json
import play.api.test.Helpers._
import scalaj.http.Http
import testUtils.TestHelpers

class ChildTaxCreditControllerSpec
    extends CommonControllerWithIfRequestSpec
    with TestHelpers {

  val rootScope = List(
    "read:individuals-benefits-and-credits-hmcts-c2",
    "read:individuals-benefits-and-credits-hmcts-c3",
    "read:individuals-benefits-and-credits-laa-c1",
    "read:individuals-benefits-and-credits-laa-c2",
    "read:individuals-benefits-and-credits-laa-c3",
    "read:individuals-benefits-and-credits-lsani-c1",
    "read:individuals-benefits-and-credits-lsani-c3"
  )

  val endpoint = "child-tax-credit"
  val matchId = UUID.randomUUID()
  val fromDate = "2017-01-01"
  val toDate = "2017-09-25"


  Feature("Live Child Tax Credit Controller") {

    Scenario("Valid request to child-tax-credits endpoint") {

      Given("A valid auth token")

      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      And("a valid record in the matching API")
      IndividualsMatchingApiStub.hasMatchFor(matchId.toString, nino)

      And("IF will return benefits and credits applications")
      IfStub.searchBenefitsAndCredits(nino, fromDate, toDate, apps)

      When("I make a call to child-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/$endpoint?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeader1))
          .asString

      Then("The response status should be 200 (ok)")
      response.code shouldBe OK

      val expectedData =
        """[ {
          |    "id" : 22,
          |    "awards" : [ {
          |      "payProfCalcDate" : "2020-08-18",
          |      "totalEntitlement" : 22,
          |      "childTaxCredit" : {
          |        "childCareAmount" : 22,
          |        "ctcChildAmount" : 22,
          |        "familyAmount" : 22,
          |        "babyAmount" : 22,
          |        "paidYTD" : 22
          |      },
          |      "payments" : [ {
          |        "startDate" : "2020-08-17",
          |        "endDate" : "2020-08-17",
          |        "frequency" : 1,
          |        "tcType" : "ICC",
          |        "amount" : 22
          |      } ]
          |    } ]
          |  }, {
          |    "id" : 22,
          |    "awards" : [ {
          |      "payProfCalcDate" : "2020-08-18",
          |      "totalEntitlement" : 22,
          |      "childTaxCredit" : {
          |        "childCareAmount" : 22,
          |        "ctcChildAmount" : 22,
          |        "familyAmount" : 22,
          |        "babyAmount" : 22,
          |        "paidYTD" : 22
          |      },
          |      "payments" : [ {
          |        "startDate" : "2020-08-17",
          |        "endDate" : "2020-08-17",
          |        "frequency" : 1,
          |        "tcType" : "ICC",
          |        "amount" : 22
          |      } ]
          |    } ]
          |  } ]
          |""".stripMargin

      Json.parse(response.body) shouldBe Json.obj(
        "_links" -> Json.obj(
          "self" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/child-tax-credits?matchId=$matchId&fromDate=$fromDate&toDate=$toDate"
          )
        ),
        "applications" -> Json.parse(expectedData)
      )

    }

    Scenario(
      "Valid request to child-tax-credits endpoint when there are no rewards") {

      Given("A valid auth token")

      AuthStub.willAuthorizePrivilegedAuthToken(authToken, rootScope)

      And("a valid record in the matching API")
      IndividualsMatchingApiStub.hasMatchFor(matchId.toString, nino)

      And("IF will return benefits and credits applications")
      IfStub.searchBenefitsAndCredits(nino,
                                      fromDate,
                                      toDate,
                                      createIfApplicationsWithEmptyRewards())

      When("I make a call to child-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/$endpoint?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeader1))
          .asString

      Then("The response status should be 200 (ok)")
      response.code shouldBe OK

      val expectedData =
        """[ {
          |    "id" : 22,
          |    "awards": []
          |  }, {
          |    "id" : 22,
          |    "awards": []
          |  } ]""".stripMargin

      Json.parse(response.body) shouldBe Json.obj(
        "_links" -> Json.obj(
          "self" -> Json.obj(
            "href" -> s"/individuals/benefits-and-credits/child-tax-credits?matchId=$matchId&fromDate=$fromDate&toDate=$toDate"
          )
        ),
        "applications" -> Json.parse(expectedData)
      )
    }

    Scenario("Invalid token") {
      Given("an invalid token")
      AuthStub.willNotAuthorizePrivilegedAuthToken(authToken, rootScope)

      When("I make a call to child-tax-credit endpoint")
      val response =
        Http(
          s"$serviceUrl/$endpoint?matchId=$matchId&fromDate=$fromDate&toDate=$toDate")
          .headers(requestHeaders(acceptHeader1))
          .asString

      Then("the response status should be 401 (unauthorized)")
      response.code shouldBe UNAUTHORIZED
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "UNAUTHORIZED",
        "message" -> "Bearer token is missing or not authorized"
      )
    }
  }
}
