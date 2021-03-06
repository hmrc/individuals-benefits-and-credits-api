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

import org.joda.time.LocalDate
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfPayment

case class WtcPayment(
    startDate: Option[LocalDate],
    endDate: Option[LocalDate],
    frequency: Option[Int],
    tcType: Option[String],
    amount: Option[Double]
)

object WtcPayment {
  def create(ifPayment: IfPayment) = {
    WtcPayment(
      ifPayment.startDate.map(LocalDate.parse),
      ifPayment.endDate.map(LocalDate.parse),
      ifPayment.frequency,
      ifPayment.tcType,
      ifPayment.amount
    )
  }

  implicit val paymentFormat: Format[WtcPayment] = Format(
    (
      (JsPath \ "startDate").readNullable[LocalDate] and
        (JsPath \ "endDate").readNullable[LocalDate] and
        (JsPath \ "frequency").readNullable[Int] and
        (JsPath \ "tcType").readNullable[String] and
        (JsPath \ "amount").readNullable[Double]
    )(WtcPayment.apply _),
    (
      (JsPath \ "startDate").writeNullable[LocalDate] and
        (JsPath \ "endDate").writeNullable[LocalDate] and
        (JsPath \ "frequency").writeNullable[Int] and
        (JsPath \ "tcType").writeNullable[String] and
        (JsPath \ "amount").writeNullable[Double]
    )(unlift(WtcPayment.unapply))
  )
}
