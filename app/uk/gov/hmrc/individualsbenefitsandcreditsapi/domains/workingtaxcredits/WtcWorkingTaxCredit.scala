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

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfWorkTaxCredit

case class WtcWorkingTaxCredit(amount: Option[Double], paidYTD: Option[Double])

object WtcWorkingTaxCredit {

  def create(ifWorkTaxCredit: IfWorkTaxCredit): WtcWorkingTaxCredit = {
    WtcWorkingTaxCredit(ifWorkTaxCredit.amount, ifWorkTaxCredit.paidYTD)
  }

  implicit val workingTaxCreditFormat: Format[WtcWorkingTaxCredit] = Format(
    (
      (JsPath \ "amount").readNullable[Double] and
        (JsPath \ "paidYTD").readNullable[Double]
    )(WtcWorkingTaxCredit.apply _),
    (
      (JsPath \ "amount").writeNullable[Double] and
        (JsPath \ "paidYTD").writeNullable[Double]
    )(unlift(WtcWorkingTaxCredit.unapply))
  )
}
