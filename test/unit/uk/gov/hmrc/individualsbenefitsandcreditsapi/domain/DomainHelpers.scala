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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.domain

import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.{
  Application,
  Award,
  ChildTaxCredit,
  Payment,
  WorkingTaxCredit
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{
  IfAward,
  IfChildTaxCredit,
  IfPayment,
  IfWorkTaxCredit
}

trait DomainHelpers {
  def createValidIfChildTaxCredit() = {
    IfChildTaxCredit(
      Some(10.0),
      Some(20.0),
      Some(30.0),
      Some(40.0),
      Some(50.0),
      Some(60.0)
    )
  }

  def createValidIfWorkingTaxCredit = {
    IfWorkTaxCredit(Some(10.0), Some(20.0), Some(30.0))
  }

  def createValidIfPayment() = {
    IfPayment(
      Some("test1"),
      Some("test2"),
      Some("test3"),
      Some("test4"),
      Some("test5"),
      Some("test6"),
      Some("test7"),
      Some(10),
      Some("test8"),
      Some(10.0),
      Some("test9")
    )
  }

  def createValidIfAward() = {
    IfAward(
      Some("test1"),
      Some("test2"),
      Some("test3"),
      Some(10.0),
      Some(createValidIfWorkingTaxCredit),
      Some(createValidIfChildTaxCredit()),
      Some(20.0),
      Some(Seq(createValidIfPayment()))
    )
  }

  def createValidPayment(): Payment = {
    Payment(
      Some("2016-05-01"),
      Some("2016-06-01"),
      Some(7),
      Some("ETC"),
      Some(80.0)
    )
  }

  def createValidAward(): Award = {
    new Award(
      Some("2016-05-01"),
      Some(10.0),
      Some(WorkingTaxCredit(Some(20.0), Some(30.0))),
      Some(ChildTaxCredit(Some(40.0))),
      Some(50.0),
      Some(Seq(createValidPayment()))
    )
  }

  def createValidApplication(): Application = {
    new Application(123, Seq(createValidAward()))
  }

}
