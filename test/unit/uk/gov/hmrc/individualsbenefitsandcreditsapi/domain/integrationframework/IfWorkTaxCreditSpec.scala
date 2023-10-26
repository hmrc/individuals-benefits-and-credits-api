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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.integrationframework

import play.api.libs.json.Json
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfWorkTaxCredit
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class IfWorkTaxCreditSpec extends UnitSpec {

  val workTaxCredit: IfWorkTaxCredit =
    IfWorkTaxCredit(Some(20), Some(20), Some(20))
  val invalidWorkTaxCredit: IfWorkTaxCredit =
    IfWorkTaxCredit(Some(-20), Some(-20), Some(-20))

  "Contact details" should {
    "Write to JSON" in {
      val result = Json.toJson(workTaxCredit)
      val expectedJson = Json.parse("""
                                      |{
                                      |  "amount" : 20,
                                      |  "entitlementYTD" : 20,
                                      |  "paidYTD" : 20
                                      |}"""".stripMargin)

      result shouldBe expectedJson
    }

    "Validate successfully when reading valid contact details" in {
      val result = Json.toJson(workTaxCredit).validate[IfWorkTaxCredit]
      result.isSuccess shouldBe true
    }

    "Validate unsuccessfully when reading invalid contact details" in {
      val result = Json.toJson(invalidWorkTaxCredit).validate[IfWorkTaxCredit]
      result.isError shouldBe true
    }
  }
}
