/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.utils

import play.api.mvc.QueryStringBindable

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateQueryStringBinder extends QueryStringBindable[LocalDate] {
  private val localDatePattern = "yyyy-MM-dd"

  private val dateTimeFormatter =
    DateTimeFormatter.ofPattern(localDatePattern)

  override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDate]] =
    getParam(params, key) match {
      case Right(date) => Some(Right(date))
      case Left(msg) => Some(Left(msg))
    }

  override def unbind(key: String, value: LocalDate): String =
    s"$key=${value.format(dateTimeFormatter)}"

  private def getParam(
                        params: Map[String, Seq[String]],
                        paramName: String,
                        default: Option[LocalDate] = None): Either[String, LocalDate] =
    try {
      params.get(paramName).flatMap(_.headOption) match {
        case Some(date) => Right(LocalDate.parse(date, dateTimeFormatter))
        case None =>
          default.map(Right(_)).getOrElse(Left(s"$paramName is required"))
      }
    } catch {
      case _: Throwable => Left(s"$paramName: invalid date format")
    }
}
