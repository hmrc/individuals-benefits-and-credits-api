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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.integrationframework

import play.api.libs.json.Json
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{
  IfChildTaxCredit
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class IfChildTaxCreditSpec extends UnitSpec {

  val childTaxCredit =
    IfChildTaxCredit(Some(20), Some(20), Some(20), Some(20), Some(20), Some(20))
  val invalidChildTaxCredit =
    IfChildTaxCredit(Some(-20),
                     Some(-20),
                     Some(-20),
                     Some(-20),
                     Some(-20),
                     Some(-20))

  "Contact details" should {
    "Write to JSON" in {
      val result = Json.toJson(childTaxCredit)
      val expectedJson = Json.parse("""
                                      |{
                                      |  "childCareAmount" : 20,
                                      |  "ctcChildAmount" : 20,
                                      |  "familyAmount" : 20,
                                      |  "babyAmount" : 20,
                                      |  "entitlementYTD" : 20,
                                      |  "paidYTD" : 20
                                      |}"""".stripMargin)

      result shouldBe expectedJson
    }

    "Validate successfully when reading valid contact details" in {
      val result = Json.toJson(childTaxCredit).validate[IfChildTaxCredit]
      result.isSuccess shouldBe true
    }

    "Validate unsuccessfully when reading invalid contact details" in {
      val result = Json.toJson(invalidChildTaxCredit).validate[IfChildTaxCredit]
      result.isError shouldBe true
    }
  }
}
