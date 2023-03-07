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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain

import org.joda.time.LocalDate
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits.{
  CtcApplication,
  CtcAward,
  CtcChildTaxCredit,
  CtcPayment
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{
  IfAward,
  IfChildTaxCredit,
  IfPayment,
  IfWorkTaxCredit
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.{
  WtcApplication,
  WtcAward,
  WtcChildTaxCredit,
  WtcPayment,
  WtcWorkingTaxCredit
}

trait DomainHelpers {
  def createValidIfChildTaxCredit(): IfChildTaxCredit = {
    IfChildTaxCredit(
      Some(10.0),
      Some(20.0),
      Some(30.0),
      Some(40.0),
      Some(50.0),
      Some(60.0)
    )
  }

  def createValidIfWorkingTaxCredit: IfWorkTaxCredit = {
    IfWorkTaxCredit(Some(10.0), Some(20.0), Some(30.0))
  }

  def createValidIfWtcPayment(): IfPayment = {
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
      Some(2),
      Some("R")
    )
  }


  def createValidIfCtcPayment(): IfPayment = {
    IfPayment(
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some("A"),
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some(1),
      Some("ICC"),
      Some(2),
      Some("R")
    )
  }

  def createValidIfAward(payments: Seq[IfPayment]): IfAward = {
    IfAward(
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some("2020-08-18"),
      Some(10.0),
      Some(createValidIfWorkingTaxCredit),
      Some(createValidIfChildTaxCredit()),
      Some(20.0),
      Some(payments)
    )
  }

  def createValidWtcPayment(): WtcPayment = {
    WtcPayment(
      Some(LocalDate.parse("2016-05-01")),
      Some(LocalDate.parse("2016-06-01")),
      Some(7),
      Some("ETC"),
      Some(80.0)
    )
  }

  def createValidWtcAward(): WtcAward = {
    new WtcAward(
      Some(LocalDate.parse("2016-05-01")),
      Some(10.0),
      Some(WtcWorkingTaxCredit(Some(20.0), Some(30.0))),
      Some(WtcChildTaxCredit(Some(40.0))),
      Some(Seq(createValidWtcPayment()))
    )
  }

  def createValidWtcApplication(): WtcApplication = {
    new WtcApplication(Some(123), Seq(createValidWtcAward()))
  }

  def createValidCtcPayment(): CtcPayment = {
    CtcPayment(
      Some(LocalDate.parse("2016-05-01")),
      Some(LocalDate.parse("2016-06-01")),
      Some(7),
      Some("ETC"),
      Some(80.0)
    )
  }

  def createValidCtcAward(): CtcAward = {
    new CtcAward(
      Some(LocalDate.parse("2016-05-01")),
      Some(10.0),
      Some(
        CtcChildTaxCredit(Some(40.0),
                          Some(50.0),
                          Some(60.0),
                          Some(70.0),
                          Some(80.0))),
      Some(Seq(createValidCtcPayment()))
    )
  }

  def createValidCtcApplication(): CtcApplication = {
    new CtcApplication(Some(123), Seq(createValidCtcAward()))
  }

}
