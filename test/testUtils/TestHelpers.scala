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

package testUtils

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{
  IfApplication,
  IfApplications,
  IfAward,
  IfChildTaxCredit,
  IfPayment,
  IfWorkTaxCredit
}

import scala.util.Random

trait TestHelpers {

  def generateString(length: Int): String = {
    val chars = "abcdefghijklmnopqrstuvwxyz123456789"

    def generate(string: String): String = {
      if (string.length < length)
        generate(
          string.concat(
            chars.charAt(Random.nextInt(chars.length - 1)).toString))
      else
        string
    }

    generate("")
  }

  def createValidIfApplications: IfApplications = {
    val ifWorkTaxCredit = IfWorkTaxCredit(Some(22), Some(22), Some(22))
    val ifChildTaxCredit = IfChildTaxCredit(Some(22),
                                            Some(22),
                                            Some(22),
                                            Some(22),
                                            Some(22),
                                            Some(22))
    val ifPayments = Seq(
      IfPayment(
        Some("2020-08-18"),
        Some("2020-08-18"),
        Some("2020-08-18"),
        Some("2020-08-18"),
        Some("A"),
        Some("2020-08-18"),
        Some("2020-08-18"),
        Some(1),
        Some("ETC"),
        Some(22),
        Some("R")
      )
    )
    val ifAwards = IfAward(Some("2020-08-18"),
                           Some("2020-08-18"),
                           Some("2020-08-18"),
                           Some(22),
                           Some(ifWorkTaxCredit),
                           Some(ifChildTaxCredit),
                           Some(22),
                           Some(ifPayments))

    val application = IfApplication(22,
                                    Some("2020-08-18"),
                                    Some("2020-08-18"),
                                    Some("2020-08-18"),
                                    Some(Seq(ifAwards)))

    IfApplications(Seq(application))
  }
}
