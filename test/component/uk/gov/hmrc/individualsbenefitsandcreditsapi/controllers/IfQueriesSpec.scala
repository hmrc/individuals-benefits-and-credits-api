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

package component.uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import component.uk.gov.hmrc.individualsbenefitsandcreditsapi.stubs.BaseSpec
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesHelper

class IfQueriesSpec extends BaseSpec {

  feature("Query strings for root endpoint") {

    val endpoint = "benefits-and-credits"
    val helper: ScopesHelper = app.injector.instanceOf[ScopesHelper]

    scenario("For read:individuals-benefits-and-credits-laa-c1") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c1"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement))"
    }

    scenario("For read:individuals-benefits-and-credits-laa-c2") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c2"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement))"
    }

    scenario("For read:individuals-benefits-and-credits-laa-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c3"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement))"
    }

    scenario("For read:individuals-benefits-and-credits-hmcts-c2") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-hmcts-c2"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement),id)"
    }

    scenario("For read:individuals-benefits-and-credits-hmcts-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-hmcts-c3"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement),id)"
    }

    scenario("For read:individuals-benefits-and-credits-lsani-c1") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-lsani-c1"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement),id)"
    }

    scenario("For read:individuals-benefits-and-credits-lsani-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-lsani-c3"),
        endpoint)
      queryString shouldBe "applications(awards(payProfCalcDate,totalEntitlement),id)"
    }

  }

  feature("Query strings for working-tax-credit endpoint") {

    val endpoint = "working-tax-credit"
    val helper: ScopesHelper = app.injector.instanceOf[ScopesHelper]

    scenario("For read:individuals-benefits-and-credits-laa-c1") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c1"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

    scenario("For read:individuals-benefits-and-credits-laa-c2") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c2"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

    scenario("For read:individuals-benefits-and-credits-laa-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c3"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

    scenario("For read:individuals-benefits-and-credits-hmcts-c2") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-hmcts-c2"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

    scenario("For read:individuals-benefits-and-credits-hmcts-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-hmcts-c3"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

    scenario("For read:individuals-benefits-and-credits-lsani-c1") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-lsani-c1"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

    scenario("For read:individuals-benefits-and-credits-lsani-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-lsani-c3"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(childCareAmount),payments(amount,endDate,frequency,startDate,tcType),workingTaxCredit(amount,paidYTD)))"
    }

  }

  feature("Query strings for child-tax-credit endpoint") {

    val helper: ScopesHelper = app.injector.instanceOf[ScopesHelper]
    val endpoint = "child-tax-credit"

    scenario("For read:individuals-benefits-and-credits-laa-c1") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c1"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

    scenario("For read:individuals-benefits-and-credits-laa-c2") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c2"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

    scenario("For read:individuals-benefits-and-credits-laa-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-laa-c3"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

    scenario("For read:individuals-benefits-and-credits-hmcts-c2") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-hmcts-c2"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

    scenario("For read:individuals-benefits-and-credits-hmcts-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-hmcts-c3"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

    scenario("For read:individuals-benefits-and-credits-lsani-c1") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-lsani-c1"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

    scenario("For read:individuals-benefits-and-credits-lsani-c3") {
      val queryString = helper.getQueryStringFor(
        Seq("read:individuals-benefits-and-credits-lsani-c3"),
        endpoint)
      queryString shouldBe "applications(awards(childTaxCredit(babyAmount,childCareAmount,ctcChildAmount,familyAmount,paidYTD),payments(amount,endDate,frequency,startDate,tcType)))"
    }

  }
}
