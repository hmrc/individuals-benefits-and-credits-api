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

  val ifWorkTaxCredit = IfWorkTaxCredit(amount = Some(22),
                                        entitlementYTD = Some(22),
                                        paidYTD = Some(22))

  val ifChildTaxCredit = IfChildTaxCredit(childCareAmount = Some(22),
                                          ctcChildAmount = Some(22),
                                          familyAmount = Some(22),
                                          babyAmount = Some(22),
                                          entitlementYTD = Some(22),
                                          paidYTD = Some(22))

  def createValidIfApplications: IfApplications = {

    val ifPayments = Seq(
      IfPayment(
        periodStartDate = Some("2020-08-18"),
        periodEndDate = Some("2020-08-18"),
        startDate = Some("2020-08-18"),
        endDate = Some("2020-08-18"),
        status = Some("A"),
        postedDate = Some("2020-08-18"),
        nextDueDate = Some("2020-08-18"),
        frequency = Some(1),
        tcType = Some("ETC"),
        amount = Some(22),
        method = Some("R")
      )
    )
    val ifAwards = IfAward(
      payProfCalcDate = Some("2020-08-18"),
      startDate = Some("2020-08-18"),
      endDate = Some("2020-08-18"),
      totalEntitlement = Some(22),
      workTaxCredit = Some(ifWorkTaxCredit),
      childTaxCredit = Some(ifChildTaxCredit),
      grossTaxYearAmount = Some(22),
      payments = Some(ifPayments)
    )

    val application = IfApplication(
      id = 22,
      ceasedDate = Some("2020-08-18"),
      entStartDate = Some("2020-08-18"),
      entEndDate = Some("2020-08-18"),
      awards = Some(Seq(ifAwards))
    )

    IfApplications(Seq(application))
  }

  def createValidIfApplicationsMultiple: IfApplications = {
    val ifWorkTaxCredit = IfWorkTaxCredit(amount = Some(22),
                                          entitlementYTD = Some(22),
                                          paidYTD = Some(22))

    val ifChildTaxCredit = IfChildTaxCredit(childCareAmount = Some(22),
                                            ctcChildAmount = Some(22),
                                            familyAmount = Some(22),
                                            babyAmount = Some(22),
                                            entitlementYTD = Some(22),
                                            paidYTD = Some(22))

    val ifPayments = Seq(
      IfPayment(
        periodStartDate = Some("2020-08-18"),
        periodEndDate = Some("2020-08-18"),
        startDate = Some("2020-08-18"),
        endDate = Some("2020-08-18"),
        status = Some("A"),
        postedDate = Some("2020-08-18"),
        nextDueDate = Some("2020-08-18"),
        frequency = Some(1),
        tcType = Some("ETC"),
        amount = Some(22),
        method = Some("R")
      )
    )

    val ifAwards = IfAward(
      payProfCalcDate = Some("2020-08-18"),
      startDate = Some("2020-08-18"),
      endDate = Some("2020-08-18"),
      totalEntitlement = Some(22),
      workTaxCredit = Some(ifWorkTaxCredit),
      childTaxCredit = Some(ifChildTaxCredit),
      grossTaxYearAmount = Some(22),
      payments = Some(ifPayments)
    )

    val application = IfApplication(id = 22,
                                    ceasedDate = Some("2020-08-18"),
                                    entStartDate = Some("2020-08-18"),
                                    entEndDate = Some("2020-08-18"),
                                    awards = Some(Seq(ifAwards)))

    IfApplications(Seq(application, application))
  }

  def createEmpyIfApplications: IfApplications = {
    IfApplications(Seq.empty)
  }
}
