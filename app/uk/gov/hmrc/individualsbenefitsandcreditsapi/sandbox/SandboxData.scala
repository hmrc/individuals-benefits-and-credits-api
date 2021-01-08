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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.sandbox

import org.joda.time.LocalDate
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits.{
  CtcApplication,
  CtcAward,
  CtcChildTaxCredit,
  CtcPayment
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.{
  WtcApplication,
  WtcAward,
  WtcChildTaxCredit,
  WtcPayment,
  WtcWorkingTaxCredit
}

import java.util.UUID

object SandboxData {
  val sandboxMatchIdString = "57072660-1df9-4aeb-b4ea-cd2d7f96e430"
  val sandboxMatchId = UUID.fromString(sandboxMatchIdString)

  val sandboxNinoString = "NA000799C"
  val sandboxNino = Nino(sandboxNinoString)

  object WorkingTaxCredits {

    object Applications {
      val firstApplication = WtcApplication(
        123,
        Seq(
          WtcAward(
            Some(LocalDate.parse("2016-10-10")),
            Some(100.12),
            Some(WtcWorkingTaxCredit(Some(200.34), Some(300.56))),
            Some(WtcChildTaxCredit(Some(400.78))),
            Some(
              Seq(
                WtcPayment(
                  Some(LocalDate.parse("2016-10-10")),
                  Some(LocalDate.parse("2016-11-11")),
                  Some(7),
                  Some("ETC"),
                  Some(123.45)
                ),
                WtcPayment(
                  Some(LocalDate.parse("2016-12-12")),
                  Some(LocalDate.parse("2017-01-01")),
                  Some(7),
                  Some("EEC"),
                  Some(123.45)
                )
              ))
          )
        )
      )

      val secondApplication = WtcApplication(
        456,
        Seq(
          WtcAward(
            Some(LocalDate.parse("2016-10-10")),
            Some(100.12),
            Some(WtcWorkingTaxCredit(Some(200.34), Some(300.56))),
            Some(WtcChildTaxCredit(Some(400.78))),
            Some(
              Seq(
                WtcPayment(
                  Some(LocalDate.parse("2017-10-10")),
                  Some(LocalDate.parse("2017-11-11")),
                  Some(7),
                  Some("ETC"),
                  Some(123.45)
                ),
                WtcPayment(
                  Some(LocalDate.parse("2017-12-12")),
                  Some(LocalDate.parse("2018-01-01")),
                  Some(7),
                  Some("EEC"),
                  Some(123.45)
                )
              ))
          ),
          WtcAward(
            Some(LocalDate.parse("2016-10-10")),
            Some(100.12),
            Some(WtcWorkingTaxCredit(Some(200.34), Some(300.56))),
            Some(WtcChildTaxCredit(Some(400.78))),
            Some(
              Seq(
                WtcPayment(
                  Some(LocalDate.parse("2018-10-10")),
                  Some(LocalDate.parse("2018-11-11")),
                  Some(7),
                  Some("ETC"),
                  Some(123.45)
                ),
                WtcPayment(
                  Some(LocalDate.parse("2018-12-12")),
                  Some(LocalDate.parse("2019-01-01")),
                  Some(7),
                  Some("EEC"),
                  Some(123.45)
                )
              ))
          )
        )
      )

      val applications = Seq(firstApplication, secondApplication)
    }
  }

  object ChildTaxCredits {

    object Applications {
      val firstApplication = CtcApplication(
        123,
        Some(
          Seq(
            CtcAward(
              Some(LocalDate.parse("2016-10-10")),
              Some(100.12),
              Some(
                CtcChildTaxCredit(Some(400.78),
                                  Some(500.12),
                                  Some(600.34),
                                  Some(700.67),
                                  Some(800.89))),
              Some(Seq(
                CtcPayment(
                  Some(LocalDate.parse("2016-10-10")),
                  Some(LocalDate.parse("2016-11-11")),
                  Some(7),
                  Some("ETC"),
                  Some(123.45)
                ),
                CtcPayment(
                  Some(LocalDate.parse("2016-12-12")),
                  Some(LocalDate.parse("2017-01-01")),
                  Some(7),
                  Some("EEC"),
                  Some(123.45)
                )
              ))
            )
          ))
      )

      val secondApplication = CtcApplication(
        456,
        Some(
          Seq(
            CtcAward(
              Some(LocalDate.parse("2016-10-10")),
              Some(100.12),
              Some(
                CtcChildTaxCredit(Some(400.78),
                                  Some(500.12),
                                  Some(600.34),
                                  Some(700.67),
                                  Some(800.89))),
              Some(Seq(
                CtcPayment(
                  Some(LocalDate.parse("2016-10-10")),
                  Some(LocalDate.parse("2016-11-11")),
                  Some(7),
                  Some("ETC"),
                  Some(123.45)
                ),
                CtcPayment(
                  Some(LocalDate.parse("2016-12-12")),
                  Some(LocalDate.parse("2017-01-01")),
                  Some(7),
                  Some("EEC"),
                  Some(123.45)
                )
              ))
            ),
            CtcAward(
              Some(LocalDate.parse("2016-10-10")),
              Some(100.12),
              Some(
                CtcChildTaxCredit(Some(400.78),
                                  Some(500.12),
                                  Some(600.34),
                                  Some(700.67),
                                  Some(800.89))),
              Some(Seq(
                CtcPayment(
                  Some(LocalDate.parse("2016-10-10")),
                  Some(LocalDate.parse("2016-11-11")),
                  Some(7),
                  Some("ETC"),
                  Some(123.45)
                ),
                CtcPayment(
                  Some(LocalDate.parse("2016-12-12")),
                  Some(LocalDate.parse("2017-01-01")),
                  Some(7),
                  Some("EEC"),
                  Some(123.45)
                )
              ))
            )
          ))
      )

      val applications = Seq(firstApplication, secondApplication)
    }
  }
}
