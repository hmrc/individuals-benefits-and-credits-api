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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.config

import play.api.Configuration

trait ScopesConfigHelper {

  // @formatter:off
  val mockSco\pesConfig: Configuration = Configuration(
    (s"api-config.scopes.test-scope.fields", List("A", "D", "M")),

    (s"api-config.endpoints.internal.child-tax-credit.endpoint",
      "/individuals/benefits-and-credits/child-tax-credit?matchId=<matchId>{&fromDate,toDate}"),
    (s"api-config.endpoints.internal.child-tax-credit.title", "Get Child Tax Credit details"),
    (s"api-config.endpoints.internal.child-tax-credit.fields", List("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "T")),
    (s"api-config.fields.A", "applications/id"),
    (s"api-config.fields.B", "applications/awards/totalEntitlement"),
    (s"api-config.fields.C", "applications/awards/payProfCalcDate"),
    (s"api-config.fields.D", "applications/awards/childTaxCredit/childCareAmount"),
    (s"api-config.fields.E", "applications/awards/childTaxCredit/ctcChildAmount"),
    (s"api-config.fields.F", "applications/awards/childTaxCredit/familyAmount"),
    (s"api-config.fields.G", "applications/awards/childTaxCredit/babyAmount"),
    (s"api-config.fields.H", "applications/awards/childTaxCredit/paidYTD"),
    (s"api-config.fields.I", "applications/awards/payments/amount"),
    (s"api-config.fields.J", "applications/awards/payments/frequency"),
    (s"api-config.fields.K", "applications/awards/payments/startDate"),
    (s"api-config.fields.L", "applications/awards/payments/endDate"),
    (s"api-config.fields.T", "applications/awards/payments/tcType"),

    (s"api-config.endpoints.internal.working-tax-credit.endpoint",
      "/individuals/benefits-and-credits/working-tax-credit?matchId=<matchId>{&fromDate,toDate}"),
    (s"api-config.endpoints.internal.working-tax-credit.title", "Get Working Tax Credit details"),
    (s"api-config.endpoints.internal.working-tax-credit.fields", List("V", "W", "X", "M", "N", "O", "P", "Q", "R", "S", "U")),
    (s"api-config.fields.V", "applications/id"),
    (s"api-config.fields.W", "applications/awards/totalEntitlement"),
    (s"api-config.fields.X", "applications/awards/payProfCalcDate"),
    (s"api-config.fields.M", "applications/awards/workingTaxCredit/amount"),
    (s"api-config.fields.N", "applications/awards/childTaxCredit/childCareAmount"),
    (s"api-config.fields.O", "applications/awards/payments/amount"),
    (s"api-config.fields.P", "applications/awards/payments/frequency"),
    (s"api-config.fields.Q", "applications/awards/payments/startDate"),
    (s"api-config.fields.R", "applications/awards/payments/endDate"),
    (s"api-config.fields.S", "applications/awards/workingTaxCredit/paidYTD"),
    (s"api-config.fields.U", "applications/awards/payments/tcType")
  )
  // @formatter:on
}
