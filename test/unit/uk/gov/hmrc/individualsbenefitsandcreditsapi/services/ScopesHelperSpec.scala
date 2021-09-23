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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.service

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.{ScopesHelper, ScopesService}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class ScopesHelperSpec
  extends UnitSpec
    with ScopesConfig
    with BeforeAndAfterEach
    with Matchers {

  "Scopes helper" should {

    val scopesService = new ScopesService(mockConfig)
    val scopesHelper = new ScopesHelper(scopesService)

    "return correct query string" in {
      val result = scopesHelper.getQueryStringFor(List(mockScopeOne, mockScopeTwo), List(endpointOne, endpointTwo, endpointThree))
      result shouldBe "path(to(a,b,c,d,e,f,g,h,i))"
    }

    "generate Hal response" in {

      val mockData = Json.obj(
        "employments" -> Json.obj(
          "field1" -> Json.toJson("value1"),
          "field2" -> Json.toJson("value2")
        )
      )

      val response = scopesHelper.getHalResponse(
        endpoint = endpointOne,
        scopes = List(mockScopeOne),
        data = Some(mockData)
      )

      response.links.links.size shouldBe 5

      response.links.links.exists(halLink =>
        halLink.rel == endpointOne && halLink.href == "/internal/1") shouldBe true

      response.links.links.exists(halLink =>
        halLink.rel == "self" && halLink.href == "/internal/1") shouldBe true

      val response2 = scopesHelper.getHalResponse(
        endpoint = endpointTwo,
        scopes = List(mockScopeOne, mockScopeTwo),
        data = Some(mockData)
      )

      response2.links.links.size shouldBe 7

      response2.links.links.exists(halLink =>
        halLink.rel == endpointTwo && halLink.href == "/external/2") shouldBe true

      response2.links.links.exists(halLink =>
        halLink.rel == endpointThree && halLink.href == "/external/3") shouldBe true

      response2.links.links.exists(halLink => halLink.rel == "self" && halLink.href == "/internal/2") shouldBe true

    }
  }
}