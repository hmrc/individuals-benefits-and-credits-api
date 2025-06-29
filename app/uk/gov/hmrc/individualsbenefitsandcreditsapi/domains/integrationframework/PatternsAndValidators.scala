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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework

import play.api.libs.functional.syntax.*
import play.api.libs.json.Reads
import play.api.libs.json.Reads.{max, min, verifying}

trait PatternsAndValidators {

  val datePattern =
    ("^(((19|20)([2468][048]|[13579][26]|0[48])|2000)[-]02[-]29|((19|20)[0-9]{2}[-](0[469]|11)[-]" +
      "(0[1-9]|1[0-9]|2[0-9]|30)|(19|20)[0-9]{2}[-](0[13578]|1[02])[-](0[1-9]|[12][0-9]|3[01])|(19|20)[0-9]{2}[-]02[-]" +
      "(0[1-9]|1[0-9]|2[0-8])))$").r
  val minPaymentValue = 0
  val maxPaymentValue = 1000000000000000.0
  val statusPattern = "^([ADSCX])$".r
  val methodPattern = "^([ROM])$".r
  val tcTypePattern = "^(ETC|ICC)$".r

  def isMultipleOfPointZeroOne(value: Double): Boolean =
    (BigDecimal(value) * 100.0) % 1 == 0

  def isMultipleOfOne(value: Double): Boolean = BigDecimal(value) % 1 == 0

  def applicationIdValidator: Reads[Double] =
    min[Double](0) andKeep max[Double](999999999999.0) andKeep verifying[Double](isMultipleOfOne)

  def paymentAmountValidator: Reads[Double] =
    min[Double](minPaymentValue) andKeep max[Double](maxPaymentValue) andKeep verifying[Double](
      isMultipleOfPointZeroOne
    )

}
