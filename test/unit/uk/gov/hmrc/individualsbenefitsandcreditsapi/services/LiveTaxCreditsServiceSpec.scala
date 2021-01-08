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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services

import java.util.UUID

import org.joda.time.{Interval, LocalDate}
import org.scalatestplus.mockito.MockitoSugar
import testUtils.TestHelpers
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors.{
  IfConnector,
  IndividualsMatchingApiConnector
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.LiveTaxCreditsService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.CacheService
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesConfig
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

import scala.concurrent.ExecutionContext

class LiveTaxCreditsServiceSpec
    extends UnitSpec
    with MockitoSugar
    with TestHelpers
    with ScopesConfig {

  trait Setup {
    val cacheService = mock[CacheService]
    val ifConnector = mock[IfConnector]
    val scopeService = mock[ScopesService]
    val scopesHelper = mock[ScopesHelper]
    val matchingConnector = mock[IndividualsMatchingApiConnector]

    val taxCreditsService =
      new LiveTaxCreditsService(
        cacheService,
        ifConnector,
        scopeService,
        scopesHelper,
        matchingConnector
      )

    val nino = Nino("AB123456C")
    private val fromDate = new LocalDate("2017-03-02").toDateTimeAtStartOfDay
    private val toDate = new LocalDate("2017-05-31").toDateTimeAtStartOfDay
    val testInterval = new Interval(fromDate, toDate)
    val testMatchId = UUID.fromString("be2dbba5-f650-47cf-9753-91cdaeb16ebe")

    implicit val ec = ExecutionContext.global
    implicit val hc = HeaderCarrier()

  }

  "Live Tax Credits Service" should {
    "return an empty list when no applications received from IF" in new Setup {
//
//      when(scopeService.getValidFieldsForCacheKey(any())).thenReturn("test")
//      when(matchingConnector.resolve(eqTo(testMatchId))(any()))
//        .thenReturn(Future.successful(MatchedCitizen(testMatchId, nino)))
//      when(
//        scopesHelper.getQueryStringFor(eqTo(List("testScope")),
//                                       eqTo("working-tax-credits")))
//        .thenReturn("(ABC)")
//
////      when(
////        cacheService.get(
////          eqTo(CacheId(testMatchId, testInterval, "test")(any())))
////      ).thenReturn(Future.successful(createEmpyIfApplications.applications))
//
//      val response =
//        await(
//          taxCreditsService.getWorkingTaxCredits(testMatchId,
//                                                 testInterval,
//                                                 "working-tax-credits",
//                                                 Seq("testScope")))
//
//      when(scopeService.getValidFieldsForCacheKey(any())).thenReturn("test")
//      when(matchingConnector.resolve(eqTo(testMatchId))(any()))
//        .thenReturn(Future.successful(MatchedCitizen(testMatchId, nino)))
//      when(
//        scopesHelper.getQueryStringFor(eqTo(List("testScope")),
//                                       eqTo("working-tax-credits")))
//        .thenReturn("(ABC)")
//
////      when(
////        cacheService.get(
////          eqTo(CacheId(testMatchId, testInterval, "test")(any())))
////      ).thenReturn(Future.successful(createEmpyIfApplications.applications))
//
//      val response =
//        await(
//          taxCreditsService.getWorkingTaxCredits(testMatchId,
//                                                 testInterval,
//                                                 "working-tax-credits",
//                                                 Seq("testScope")))

      //response.isEmpty shouldBe true
    }
  }
}
