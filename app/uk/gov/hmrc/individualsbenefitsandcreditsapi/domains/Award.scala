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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfAward
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

case class Award(
    payProfCalcDate: Option[String],
    totalEntitlement: Option[Double],
    workingTaxCredit: Option[WorkingTaxCredit],
    childTaxCredit: Option[ChildTaxCredit],
    grossTaxYearAmount: Option[Double],
    payments: Option[Seq[Payment]]
)

object Award {
  def create(ifAward: IfAward) = {

    val wtc = ifAward.workTaxCredit.map(WorkingTaxCredit.create)
    val ctc = ifAward.childTaxCredit.map(ChildTaxCredit.create)

    Award(
      ifAward.payProfCalcDate,
      ifAward.totalEntitlement,
      wtc,
      ctc,
      ifAward.grossTaxYearAmount,
      ifAward.payments.map(x => x.map(Payment.create))
    )
  }

  implicit val awardFormat: Format[Award] = Format(
    (
      (JsPath \ "payProfCalcDate").readNullable[String] and
        (JsPath \ "totalEntitlement").readNullable[Double] and
        (JsPath \ "workingTaxCredit").readNullable[WorkingTaxCredit] and
        (JsPath \ "childTaxCredit").readNullable[ChildTaxCredit] and
        (JsPath \ "grossTaxYearAmount").readNullable[Double] and
        (JsPath \ "payments").readNullable[Seq[Payment]]
    )(Award.apply _),
    (
      (JsPath \ "payProfCalcDate").writeNullable[String] and
        (JsPath \ "totalEntitlement").writeNullable[Double] and
        (JsPath \ "workingTaxCredit").writeNullable[WorkingTaxCredit] and
        (JsPath \ "childTaxCredit").writeNullable[ChildTaxCredit] and
        (JsPath \ "grossTaxYearAmount").writeNullable[Double] and
        (JsPath \ "payments").writeNullable[Seq[Payment]]
    )(unlift(Award.unapply))
  )
}
