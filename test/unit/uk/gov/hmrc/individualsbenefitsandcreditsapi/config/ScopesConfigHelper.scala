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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.config

import play.api.Configuration

trait ScopesConfigHelper {

  // @formatter:off
  val mockScopesConfig = Configuration(
    (s"api-config.scopes.test-scope.fields", List("A", "D", "M")),

    (s"api-config.endpoints.child-tax-credit.endpoint",
      "/individuals/benefits-and-credits/child-tax-credit?matchId=<matchId>{&fromDate,toDate}"),
    (s"api-config.endpoints.child-tax-credit.title", "Get an individual's child tax credits data"),
    (s"api-config.endpoints.child-tax-credit.fields.A", "applications/id"),
    (s"api-config.endpoints.child-tax-credit.fields.B", "applications/awards/totalEntitlement"),
    (s"api-config.endpoints.child-tax-credit.fields.C", "applications/awards/payProfCalcDate"),
    (s"api-config.endpoints.child-tax-credit.fields.D", "applications/awards/childTaxCredit/childCareAmount"),
    (s"api-config.endpoints.child-tax-credit.fields.E", "applications/awards/childTaxCredit/ctcChildAmount"),
    (s"api-config.endpoints.child-tax-credit.fields.F", "applications/awards/childTaxCredit/familyAmount"),
    (s"api-config.endpoints.child-tax-credit.fields.G", "applications/awards/childTaxCredit/babyAmount"),
    (s"api-config.endpoints.child-tax-credit.fields.H", "applications/awards/childTaxCredit/paidYTD"),
    (s"api-config.endpoints.child-tax-credit.fields.I", "applications/awards/payments/amount"),
    (s"api-config.endpoints.child-tax-credit.fields.J", "applications/awards/payments/frequency"),
    (s"api-config.endpoints.child-tax-credit.fields.K", "applications/awards/payments/startDate"),
    (s"api-config.endpoints.child-tax-credit.fields.L", "applications/awards/payments/endDate"),
    (s"api-config.endpoints.child-tax-credit.fields.T", "applications/awards/payments/tcType"),

    (s"api-config.endpoints.working-tax-credit.endpoint",
      "/individuals/benefits-and-credits/working-tax-credit?matchId=<matchId>{&fromDate,toDate}"),
    (s"api-config.endpoints.working-tax-credit.title", "Get an individual's working tax credits data"),
    (s"api-config.endpoints.working-tax-credit.fields.V", "applications/id"),
    (s"api-config.endpoints.working-tax-credit.fields.W", "applications/awards/totalEntitlement"),
    (s"api-config.endpoints.working-tax-credit.fields.X", "applications/awards/payProfCalcDate"),
    (s"api-config.endpoints.working-tax-credit.fields.M", "applications/awards/workingTaxCredit/amount"),
    (s"api-config.endpoints.working-tax-credit.fields.N", "applications/awards/childTaxCredit/childCareAmount"),
    (s"api-config.endpoints.working-tax-credit.fields.O", "applications/awards/payments/amount"),
    (s"api-config.endpoints.working-tax-credit.fields.P", "applications/awards/payments/frequency"),
    (s"api-config.endpoints.working-tax-credit.fields.Q", "applications/awards/payments/startDate"),
    (s"api-config.endpoints.working-tax-credit.fields.R", "applications/awards/payments/endDate"),
    (s"api-config.endpoints.working-tax-credit.fields.S", "applications/awards/workingTaxCredit/paidYTD"),
    (s"api-config.endpoints.working-tax-credit.fields.U", "applications/awards/payments/tcType")
  )
  // @formatter:on
}
