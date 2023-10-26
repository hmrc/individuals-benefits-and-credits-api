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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain.workingtaxcredits

import org.joda.time.LocalDate
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfPayment
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.WtcPayment
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class WtcPaymentSpec extends UnitSpec {
  "Create correctly from If Payment" in {
    val ifPayment = IfPayment(
      Some("2017-09-09"),
      Some("2017-10-10"),
      Some("2017-08-08"),
      Some("2017-09-09"),
      Some("test5"),
      Some("2017-10-10"),
      Some("test7"),
      Some(10),
      Some("test8"),
      Some(10.0),
      Some("test9")
    )

    val result = WtcPayment.create(ifPayment)

    result.startDate shouldBe Some(LocalDate.parse("2017-08-08"))
    result.endDate shouldBe Some(LocalDate.parse("2017-09-09"))
    result.postedDate shouldBe Some(LocalDate.parse("2017-10-10"))
    result.amount shouldBe Some(10.0)
    result.frequency shouldBe Some(10)
    result.tcType shouldBe Some("test8")
  }
}
