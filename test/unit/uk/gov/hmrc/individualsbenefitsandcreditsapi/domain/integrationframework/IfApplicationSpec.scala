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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.integrationframework

import play.api.libs.json.Json
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfApplication
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class IfApplicationSpec extends UnitSpec {

  val application = IfApplication(
    9,
    Some("2020-08-18"),
    Some("2020-08-18"),
    Some("2019-08-18"),
    None)
  val invalidApplication = IfApplication(
    -42,
    Some("abcdefghijklmnopqrstuvwxyz0123456789"),
    Some("a"),
    Some("as"),
    None)

  "Contact details" should {
    "Write to JSON" in {
      val result = Json.toJson(application)
      val expectedJson = Json.parse("""
                                      |{
                                      |  "id" : 9,
                                      |  "ceasedDate" : "2020-08-18",
                                      |  "entStartDate" : "2020-08-18",
                                      |  "entEndDate" : "2019-08-18"
                                      |}"""".stripMargin)

      result shouldBe expectedJson
    }

    "Validate successfully when reading valid contact details" in {
      val result = Json.toJson(application).validate[IfApplication]
      result.isSuccess shouldBe true
    }

    "Validate unsuccessfully when reading invalid contact details" in {
      val result = Json.toJson(invalidApplication).validate[IfApplication]
      result.isError shouldBe true
    }
  }
}
