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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits

import org.joda.time.LocalDate
import uk.gov.hmrc.http.controllers.RestFormats.localDateFormats
import play.api.libs.json.{Format, JsPath}
import play.api.libs.functional.syntax._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfAward

case class CtcAward(
    payProfCalcDate: Option[LocalDate],
    totalEntitlement: Option[Double],
    childTaxCredit: Option[CtcChildTaxCredit],
    payments: Option[Seq[CtcPayment]]
)

object CtcAward {
  def create(ifAward: IfAward): CtcAward = {
    CtcAward(
      ifAward.payProfCalcDate.map(LocalDate.parse),
      ifAward.totalEntitlement,
      ifAward.childTaxCredit.map(CtcChildTaxCredit.create),
      ifAward.payments.map(_.filter(_.tcType.exists(s => s.equals("ICC")))).map(_.map(CtcPayment.create))
    )
  }

  implicit val awardFormat: Format[CtcAward] = Format(
    (
      (JsPath \ "payProfCalcDate").readNullable[LocalDate] and
      (JsPath \ "totalEntitlement").readNullable[Double] and
      (JsPath \ "childTaxCredit").readNullable[CtcChildTaxCredit] and
      (JsPath \ "payments").readNullable[Seq[CtcPayment]]
    )(CtcAward.apply _),
    (
      (JsPath \ "payProfCalcDate").writeNullable[LocalDate] and
      (JsPath \ "totalEntitlement").writeNullable[Double] and
      (JsPath \ "childTaxCredit").writeNullable[CtcChildTaxCredit] and
      (JsPath \ "payments").writeNullable[Seq[CtcPayment]]
    )(unlift(CtcAward.unapply))
  )
}
