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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits

import org.joda.time.LocalDate
import uk.gov.hmrc.http.controllers.RestFormats
// Required for JodaDate parsers to function
import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfAward

case class WtcAward(
  payProfCalcDate: Option[LocalDate],
  totalEntitlement: Option[Double],
  workingTaxCredit: Option[WtcWorkingTaxCredit],
  childTaxCredit: Option[WtcChildTaxCredit],
  payments: Option[Seq[WtcPayment]]
)

object WtcAward {
  def create(ifAward: IfAward): WtcAward = {

    val wtc = ifAward.workingTaxCredit.map(WtcWorkingTaxCredit.create)
    val ctc = ifAward.childTaxCredit.map(WtcChildTaxCredit.create)

    WtcAward(
      ifAward.payProfCalcDate.map(LocalDate.parse),
      ifAward.totalEntitlement,
      wtc,
      ctc,
      ifAward.payments
        .map(_.filter(_.tcType.exists(s => s.equals("ETC"))))
        .map(_.map(WtcPayment.create))
    )
  }

  implicit val dateFormat: Format[LocalDate] = RestFormats.localDateFormats
  implicit val awardFormat: Format[WtcAward] = Json.format[WtcAward]
}
