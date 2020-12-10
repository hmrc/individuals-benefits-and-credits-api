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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.Payment
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfPayment
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class PaymentSpec extends UnitSpec {
  "Create correctly from If Payment" in {
    val ifPaymemnt = IfPayment(
      Some("test1"),
      Some("test2"),
      Some("test3"),
      Some("test4"),
      Some("test5"),
      Some("test6"),
      Some("test7"),
      Some(10),
      Some("test8"),
      Some(10.0),
      Some("test9")
    )

    val result = Payment.create(ifPaymemnt)

    result.startDate shouldBe Some("test3")
    result.endDate shouldBe Some("test4")
    result.amount shouldBe Some(10.0)
    result.frequency shouldBe Some(10)
    result.tcType shouldBe Some("test8")
  }
}
