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
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{
  IfAwards
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class IfAwardsSpec extends UnitSpec {

  val awards = IfAwards(Some("2020-08-18"),
                        Some("2020-08-18"),
                        Some("2020-08-18"),
                        Some(20),
                        None,
                        None,
                        Some(20),
                        None)
  val invalidAwards = IfAwards(Some("asd"),
                               Some("abcdefghijklmnopqrstuvwxyz0123456789"),
                               Some("a"),
                               Some(0),
                               None,
                               None,
                               None,
                               None)

  "Contact details" should {
    "Write to JSON" in {
      val result = Json.toJson(awards)
      val expectedJson = Json.parse("""
                                      |{
                                      |  "payProfCalcDate" : "2020-08-18",
                                      |  "startDate" : "2020-08-18",
                                      |  "endDate" : "2020-08-18",
                                      |  "totalEntitlement" : 20,
                                      |  "grossTaxYearAmount" : 20
                                      |}"""".stripMargin)

      result shouldBe expectedJson
    }

    "Validate successfully when reading valid contact details" in {
      val result = Json.toJson(awards).validate[IfAwards]
      result.isSuccess shouldBe true
    }

    "Validate unsuccessfully when reading invalid contact details" in {
      val result = Json.toJson(invalidAwards).validate[IfAwards]
      result.isError shouldBe true
    }
  }
}