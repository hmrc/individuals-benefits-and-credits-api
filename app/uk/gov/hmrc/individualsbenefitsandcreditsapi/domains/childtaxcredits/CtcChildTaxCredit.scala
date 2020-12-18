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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfChildTaxCredit

case class CtcChildTaxCredit(
    childCareAmount: Option[Double],
    ctcChildAmount: Option[Double],
    familyAmount: Option[Double],
    babyAmount: Option[Double],
    paidYTD: Option[Double]
)

object CtcChildTaxCredit {
  def create(ifChildTaxCredit: IfChildTaxCredit): CtcChildTaxCredit = {
    CtcChildTaxCredit(
      ifChildTaxCredit.childCareAmount,
      ifChildTaxCredit.ctcChildAmount,
      ifChildTaxCredit.familyAmount,
      ifChildTaxCredit.babyAmount,
      ifChildTaxCredit.paidYTD
    )
  }

  implicit val childTaxCreditFormat: Format[CtcChildTaxCredit] = Format(
    (
      (JsPath \ "childCareAmount").readNullable[Double] and
        (JsPath \ "ctcChildAmount").readNullable[Double] and
        (JsPath \ "familyAmount").readNullable[Double] and
        (JsPath \ "babyAmount").readNullable[Double] and
        (JsPath \ "paidYTD").readNullable[Double]
    )(CtcChildTaxCredit.apply _),
    (
      (JsPath \ "childCareAmount").writeNullable[Double] and
        (JsPath \ "ctcChildAmount").writeNullable[Double] and
        (JsPath \ "familyAmount").writeNullable[Double] and
        (JsPath \ "babyAmount").writeNullable[Double] and
        (JsPath \ "paidYTD").writeNullable[Double]
    )(unlift(CtcChildTaxCredit.unapply))
  )
}
