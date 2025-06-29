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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits

import java.time.LocalDate
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.models.childtaxcredits.CtcAwardModel
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfAward

object CtcAward {
  def create(ifAward: IfAward): CtcAwardModel =
    CtcAwardModel(
      ifAward.payProfCalcDate.map(LocalDate.parse),
      ifAward.totalEntitlement,
      ifAward.childTaxCredit.map(CtcChildTaxCredit.create),
      ifAward.payments
        .map(_.filter(_.tcType.exists(s => s.equals("ICC"))))
        .map(_.map(CtcPayment.create))
    )

}
