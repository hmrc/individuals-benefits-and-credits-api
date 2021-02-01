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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits

import org.joda.time.{LocalDate}
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfAward

case class WtcAward(
    payProfCalcDate: Option[LocalDate],
    totalEntitlement: Option[Double],
    workingTaxCredit: Option[WtcWorkingTaxCredit],
    childTaxCredit: Option[WtcChildTaxCredit],
    payments: Option[Seq[WtcPayment]]
)

object WtcAward {
  def create(ifAward: IfAward) = {

    val wtc = ifAward.workTaxCredit.map(WtcWorkingTaxCredit.create)
    val ctc = ifAward.childTaxCredit.map(WtcChildTaxCredit.create)

    WtcAward(
      ifAward.payProfCalcDate.map(LocalDate.parse),
      ifAward.totalEntitlement,
      wtc,
      ctc,
      ifAward.payments.map(_.filter(_.tcType.exists(s => s.equals("ETC")))).map(_.map(WtcPayment.create))
    )
  }

  implicit val awardFormat: Format[WtcAward] = Format(
    (
      (JsPath \ "payProfCalcDate").readNullable[LocalDate] and
        (JsPath \ "totalEntitlement").readNullable[Double] and
        (JsPath \ "workingTaxCredit").readNullable[WtcWorkingTaxCredit] and
        (JsPath \ "childTaxCredit").readNullable[WtcChildTaxCredit] and
        (JsPath \ "payments").readNullable[Seq[WtcPayment]]
    )(WtcAward.apply _),
    (
      (JsPath \ "payProfCalcDate").writeNullable[LocalDate] and
        (JsPath \ "totalEntitlement").writeNullable[Double] and
        (JsPath \ "workingTaxCredit").writeNullable[WtcWorkingTaxCredit] and
        (JsPath \ "childTaxCredit").writeNullable[WtcChildTaxCredit] and
        (JsPath \ "payments").writeNullable[Seq[WtcPayment]]
    )(unlift(WtcAward.unapply))
  )
}
