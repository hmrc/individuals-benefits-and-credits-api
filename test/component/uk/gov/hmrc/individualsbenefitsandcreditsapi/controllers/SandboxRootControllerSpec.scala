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

import component.uk.gov.hmrc.individualsbenefitsandcreditsapi.stubs.BaseSpec
import play.api.libs.json.Json
import play.api.libs.json.Json.parse
import play.api.test.Helpers._
import scalaj.http.HttpResponse
import uk.gov.hmrc.individualsbenefitsandcreditsapi.sandbox.SandboxData

class SandboxRootControllerSpec extends BaseSpec {

  val rootScope = "read:individuals-benefits-and-credits"

  feature("Sandbox Root Controller") {

    scenario("missing match id") {
      When("the root entry point to the API is invoked with a missing match id")
      val response = invokeEndpoint(s"$serviceUrl/sandbox")

      Then("the response status should be 400 (bad request)")
      assertResponseIs(response, BAD_REQUEST, """
          {
             "code" : "INVALID_REQUEST",
             "message" : "matchId is required"
          }
        """)
    }

    scenario("malformed match id") {
      When(
        "the root entry point to the API is invoked with a malformed match id")
      val response =
        invokeEndpoint(s"$serviceUrl/sandbox?matchId=malformed-match-id-value")

      Then("the response status should be 400 (bad request)")
      response.code shouldBe BAD_REQUEST
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "INVALID_REQUEST",
        "message" -> "matchId format is invalid"
      )
    }

    scenario("invalid match id") {

      When(
        "the root entry point to the API is invoked with an invalid match id")
      val response = invokeEndpoint(
        s"$serviceUrl/sandbox?matchId=0a184ef3-fd75-4d4d-b6a3-f886cc39a366")

      Then("the response status should be 404 (not found)")
      response.code shouldBe NOT_FOUND
      Json.parse(response.body) shouldBe Json.obj(
        "code" -> "NOT_FOUND",
        "message" -> "The resource can not be found"
      )
    }

    scenario("valid request to the sandbox implementation") {
      When("I request the root entry point to the API")
      val response = invokeEndpoint(
        s"$serviceUrl/sandbox?matchId=${SandboxData.sandboxMatchIdString}")

      Then("The response status should be 200 (ok)")
      assertResponseIs(
        response,
        OK,
        Json.stringify(
          Json.obj(
            "_links" -> Json.obj(
              "self" -> Json.obj(
                "href" -> s"/individuals/benefits-and-credits/?matchId=${SandboxData.sandboxMatchIdString}"
              ),
              "working-tax-credit" -> Json.obj(
                "href" -> s"/individuals/benefits-and-credits/working-tax-credit?matchId=${SandboxData.sandboxMatchIdString}{&fromDate,toDate}",
                "title" -> "Get Working Tax Credit details"
              ),
              "child-tax-credit" -> Json.obj(
                "href" -> s"/individuals/benefits-and-credits/child-tax-credit?matchId=${SandboxData.sandboxMatchIdString}{&fromDate,toDate}",
                "title" -> "Get Child Tax Credit details"
              )
            )
          ))
      )
    }
  }

  private def assertResponseIs(httpResponse: HttpResponse[String],
                               expectedResponseCode: Int,
                               expectedResponseBody: String) = {
    httpResponse.code shouldBe expectedResponseCode
    parse(httpResponse.body) shouldBe parse(expectedResponseBody)
  }

}
