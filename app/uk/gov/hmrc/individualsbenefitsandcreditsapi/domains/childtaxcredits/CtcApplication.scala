/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.libs.json.{Format, JsPath}
import play.api.libs.functional.syntax._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfApplication

case class CtcApplication(id: Option[Double], awards: Seq[CtcAward])

object CtcApplication {
  def create(ifApplication: IfApplication): CtcApplication = {
    CtcApplication(
      ifApplication.id,
      ifApplication.awards.map(x => x.map(CtcAward.create)).getOrElse(Seq.empty)
    )
  }

  implicit val applicationFormat: Format[CtcApplication] = Format(
    (
      (JsPath \ "id").readNullable[Double] and
        (JsPath \ "awards").read[Seq[CtcAward]]
    )(CtcApplication.apply _),
    (
      (JsPath \ "id").writeNullable[Double] and
        (JsPath \ "awards").write[Seq[CtcAward]]
    )(unlift(CtcApplication.unapply))
  )
}
