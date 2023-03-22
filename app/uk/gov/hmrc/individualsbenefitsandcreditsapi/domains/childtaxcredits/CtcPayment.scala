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

import org.joda.time.LocalDate
import uk.gov.hmrc.http.controllers.RestFormats.localDateFormats
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath, Json}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfPayment

case class CtcPayment(
    startDate: Option[LocalDate],
    endDate: Option[LocalDate],
    postedDate: Option[LocalDate],
    frequency: Option[Int],
    tcType: Option[String],
    amount: Option[Double]
)

object CtcPayment {
  def create(ifPayment: IfPayment): CtcPayment = {
    CtcPayment(
      ifPayment.startDate.map(LocalDate.parse),
      ifPayment.endDate.map(LocalDate.parse),
      ifPayment.postedDate.map(LocalDate.parse),
      ifPayment.frequency,
      ifPayment.tcType,
      ifPayment.amount
    )
  }

  implicit val paymentFormat: Format[CtcPayment] = Json.format[CtcPayment]
}
