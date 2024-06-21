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

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework._

import scala.util.Random

trait TestHelpers {

  def generateString(length: Int): String = {

    val chars = "abcdefghijklmnopqrstuvwxyz123456789"

    def generate(string: String): String =
      if (string.length < length)
        generate(string.concat(chars.charAt(Random.nextInt(chars.length - 1)).toString))
      else
        string

    generate("")
  }

  val ifWorkTaxCredit = IfWorkTaxCredit(amount = Some(22), entitlementYTD = Some(22), paidYTD = Some(22))

  val ifChildTaxCredit = IfChildTaxCredit(
    childCareAmount = Some(22),
    ctcChildAmount = Some(22),
    familyAmount = Some(22),
    babyAmount = Some(22),
    entitlementYTD = Some(22),
    paidYTD = Some(22)
  )

  val etcPayment = IfPayment(
    periodStartDate = Some("2020-08-18"),
    periodEndDate = Some("2020-08-18"),
    startDate = Some("2020-08-18"),
    endDate = Some("2020-08-18"),
    status = Some("A"),
    postedDate = Some("2020-08-28"),
    nextDueDate = Some("2020-08-18"),
    frequency = Some(1),
    tcType = Some("ETC"),
    amount = Some(22),
    method = Some("R")
  )

  val iccPayment = IfPayment(
    periodStartDate = Some("2020-08-17"),
    periodEndDate = Some("2020-08-17"),
    startDate = Some("2020-08-17"),
    endDate = Some("2020-08-17"),
    status = Some("A"),
    postedDate = Some("2020-08-28"),
    nextDueDate = Some("2020-08-17"),
    frequency = Some(1),
    tcType = Some("ICC"),
    amount = Some(22),
    method = Some("R")
  )

  def createIfAward(wtc: IfWorkTaxCredit, ctc: IfChildTaxCredit, payments: Seq[IfPayment]): IfAward =
    IfAward(
      payProfCalcDate = Some("2020-08-18"),
      startDate = Some("2020-08-18"),
      endDate = Some("2020-08-18"),
      totalEntitlement = Some(22),
      workingTaxCredit = Some(wtc),
      childTaxCredit = Some(ctc),
      grossTaxYearAmount = Some(22),
      payments = Some(payments)
    )

  def createValidIfApplications: IfApplications = {

    val ifPayments = Seq(
      etcPayment
    )

    val ifAwards = createIfAward(ifWorkTaxCredit, ifChildTaxCredit, ifPayments)

    val application = IfApplication(
      id = Some(22),
      ceasedDate = Some("2020-08-18"),
      entStartDate = Some("2020-08-18"),
      entEndDate = Some("2020-08-18"),
      awards = Some(Seq(ifAwards))
    )

    IfApplications(Seq(application))
  }

  def createInvalidIfApplications: IfApplications = {

    val ifPayments = Seq(
      etcPayment.copy(startDate = Some("Invalid date"))
    )

    val ifAwards = createIfAward(ifWorkTaxCredit, ifChildTaxCredit, ifPayments)

    val application = IfApplication(
      id = Some(22),
      ceasedDate = Some("2020-08-18"),
      entStartDate = Some("2020-08-18"),
      entEndDate = Some("2020-08-18"),
      awards = Some(Seq(ifAwards))
    )

    IfApplications(Seq(application))
  }

  def createValidIfApplicationsMultiple: IfApplications = {

    val ifPayments = Seq(
      etcPayment,
      iccPayment
    )

    val ifAwards = createIfAward(ifWorkTaxCredit, ifChildTaxCredit, ifPayments)

    val application = IfApplication(
      id = Some(22),
      ceasedDate = Some("2020-08-18"),
      entStartDate = Some("2020-08-18"),
      entEndDate = Some("2020-08-18"),
      awards = Some(Seq(ifAwards))
    )

    IfApplications(Seq(application, application))
  }

  def createIfApplicationsWithEmptyRewards() = {
    val application = IfApplication(
      id = Some(22),
      ceasedDate = Some("2020-08-18"),
      entStartDate = Some("2020-08-18"),
      entEndDate = Some("2020-08-18"),
      None
    )

    IfApplications(Seq(application, application))
  }

  def createEmptyIfApplications: IfApplications =
    IfApplications(Seq.empty)
}
