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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.workingtaxcredits

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfAward
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.WtcAward
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.DomainHelpers
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec
import org.joda.time.LocalDate

class WtcAwardSpec extends UnitSpec with DomainHelpers {
  "Creates correctly from IfAward" in {
    val ifAward = IfAward(
      Some("2017-08-08"),
      Some("2017-09-09"),
      Some("2017-10-10"),
      Some(10.0),
      Some(createValidIfWorkingTaxCredit),
      Some(createValidIfChildTaxCredit()),
      Some(20.0),
      Some(Seq(createValidIfWtcPayment()))
    )

    val result = WtcAward.create(ifAward)

    result.payProfCalcDate shouldBe Some(LocalDate.parse("2017-08-08"))
    result.totalEntitlement shouldBe Some(10.0)
    result.payments.isDefined shouldBe true
    result.payments.get.size shouldBe 1
    result.payments.get.head.tcType shouldBe Some("ETC")
  }

  "filters out WTC payments" in {
    val ifAward = IfAward(
      Some("2017-08-08"),
      Some("2017-09-09"),
      Some("2017-10-10"),
      Some(10.0),
      Some(createValidIfWorkingTaxCredit),
      Some(createValidIfChildTaxCredit()),
      Some(20.0),
      Some(Seq(createValidIfCtcPayment(), createValidIfWtcPayment()))
    )

    val result = WtcAward.create(ifAward)

    result.payProfCalcDate shouldBe Some(LocalDate.parse("2017-08-08"))
    result.totalEntitlement shouldBe Some(10.0)
    result.payments.isDefined shouldBe true
    result.payments.get.size shouldBe 1
    result.payments.get.head.tcType shouldBe Some("ETC")
  }
}
