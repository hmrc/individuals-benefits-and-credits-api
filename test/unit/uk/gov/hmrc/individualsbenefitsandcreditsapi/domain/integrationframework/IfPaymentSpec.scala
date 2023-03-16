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
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfPayment
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class IfPaymentSpec extends UnitSpec {

  val payments: IfPayment = IfPayment(
    Some("2020-08-18"),
    Some("2020-08-18"),
    Some("2020-08-18"),
    Some("2020-08-18"),
    Some("A"),
    Some("2020-08-19"),
    Some("2020-08-18"),
    Some(1),
    Some("ETC"),
    Some(2),
    Some("R")
  )

  val invalidPayments: IfPayment = IfPayment(
    Some("asd"),
    Some("abcdefghijklmnopqrstuvwxyz0123456789"),
    Some("a"),
    Some("20202-08-01"),
    Some("TEST"),
    Some("ASD"),
    Some("date"),
    Some(-2),
    Some("AAAAAAA"),
    Some(-2),
    Some("A")
  )

  "Contact details" should {
    "Write to JSON" in {
      val result = Json.toJson(payments)
      val expectedJson = Json.parse("""
                                      |{
                                      |  "periodStartDate" : "2020-08-18",
                                      |  "periodEndDate" : "2020-08-18",
                                      |  "startDate" : "2020-08-18",
                                      |  "endDate" : "2020-08-18",
                                      |  "status" : "A",
                                      |  "postedDate" : "2020-08-19",
                                      |  "nextDueDate" : "2020-08-18",
                                      |  "frequency" : 1,
                                      |  "tcType" : "ETC",
                                      |  "amount" : 2,
                                      |  "method" : "R"
                                      |}"""".stripMargin)

      result shouldBe expectedJson
    }

    "Validate successfully when reading valid contact details" in {
      val result = Json.toJson(payments).validate[IfPayment]
      result.isSuccess shouldBe true
    }

    "Validate unsuccessfully when reading invalid contact details" in {
      val result = Json.toJson(invalidPayments).validate[IfPayment]
      result.isError shouldBe true
    }
  }
}
