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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits

import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfApplication

case class WtcApplication(id: Option[Double], awards: Seq[WtcAward])

object WtcApplication {

  def create(ifApplication: IfApplication) =
    WtcApplication(
      ifApplication.id,
      ifApplication.awards.map(x => x.map(WtcAward.create)).getOrElse(Seq.empty)
    )

  implicit val applicationFormat: Format[WtcApplication] =
    Json.format[WtcApplication]
}
