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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfApplication
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.WtcApplication
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.DomainHelpers
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class WtcApplicationSpec extends UnitSpec with DomainHelpers {
  "Creates correctly from IfApplication" in {
    val ifApplication = IfApplication(
      Some(123),
      Some("2017-08-08"),
      Some("2017-09-09"),
      Some("2017-10-10"),
      Some(Seq(createValidIfAward(Seq(createValidIfWtcPayment()))))
    )

    val result = WtcApplication.create(ifApplication)
    result.id shouldBe Some(123)
    result.awards.size shouldBe 1
    result.awards.head.payments.isDefined shouldBe true
    result.awards.head.payments.get.size shouldBe 1
    result.awards.head.payments.get.head.tcType shouldBe Some("ETC")
  }

  "filters out CTC payments" in {
    val ifApplication = IfApplication(
      Some(123),
      Some("2017-08-08"),
      Some("2017-09-09"),
      Some("2017-10-10"),
      Some(Seq(createValidIfAward(Seq(createValidIfCtcPayment(), createValidIfWtcPayment()))))
    )

    val result = WtcApplication.create(ifApplication)
    result.id shouldBe Some(123)
    result.awards.size shouldBe 1
    result.awards.head.payments.isDefined shouldBe true
    result.awards.head.payments.get.size shouldBe 1
    result.awards.head.payments.get.head.tcType shouldBe Some("ETC")
  }
}
