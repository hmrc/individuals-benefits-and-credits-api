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

import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, JsPath}

case class Applications(id: Int, awards: Seq[Award])

object Applications {
  implicit val applicationFormat : Format[Applications] = Format(
    (
      (JsPath \ "id").read[Int] and
        (JsPath \ "awards").read[Seq[Award]]
      )(Applications.apply _),
    (
      (JsPath \ "id").write[Int] and
        (JsPath \ "awards").write[Seq[Award]]
      )(unlift(Applications.unapply))
  )
}
