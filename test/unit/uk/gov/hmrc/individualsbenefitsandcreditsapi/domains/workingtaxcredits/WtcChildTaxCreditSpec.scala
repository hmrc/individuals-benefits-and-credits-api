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

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfChildTaxCredit
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.WtcChildTaxCredit
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class WtcChildTaxCreditSpec extends UnitSpec {
  "Creates correctly from IF Child Tax Credit" in {
    val ifChildTaxCredit: IfChildTaxCredit = IfChildTaxCredit(
      Some(10.0),
      Some(20.0),
      Some(30.0),
      Some(40.0),
      Some(50.0),
      Some(60.0)
    )

    val result = WtcChildTaxCredit.create(ifChildTaxCredit)
    result.childCareAmount shouldBe Some(10.0)
  }
}
