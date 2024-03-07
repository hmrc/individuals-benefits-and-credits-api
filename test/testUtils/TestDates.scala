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

package testUtils

import uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.{Dates, Interval}

import java.time.{LocalDateTime, ZoneId}
import java.time.LocalDateTime.parse

trait TestDates {

  protected def toInterval(fromDate: String, toDate: String): Interval =
    //    TODO: BELOW CODE WORKS FOR UNBIND UNIT TEST, ORIGINAL CODE THROWS ERROR - WHY???
//    val dateTimeFormatter = Dates.localDatePattern
//    val fromDateLDT: LocalDateTime = dateTimeFormatter.parse(fromDate).toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime
//    val toDateLDT: LocalDateTime = dateTimeFormatter.parse(toDate).toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime
//    Interval(fromDateLDT, toDateLDT)
    toInterval(parse(fromDate), parse(toDate))

  protected def toInterval(fromDate: LocalDateTime, toDate: LocalDateTime): Interval =
    Interval(fromDate, toDate)

}
