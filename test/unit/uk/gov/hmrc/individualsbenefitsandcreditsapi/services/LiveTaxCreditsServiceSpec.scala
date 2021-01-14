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

import com.typesafe.config.Config
import org.joda.time.{Interval, LocalDate}
import org.scalatestplus.mockito.MockitoSugar
import testUtils.TestHelpers
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors.{
  IfConnector,
  IndividualsMatchingApiConnector
}
import org.mockito.ArgumentMatchers.{any, refEq, eq => eqTo}
import org.mockito.Mockito.{verifyNoInteractions, when}
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Format
import play.api.{Application, Configuration}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.cache.CacheConfiguration
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.MatchedCitizen
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfApplication
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.LiveTaxCreditsService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.{
  CacheId,
  CacheIdBase,
  CacheService
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesConfig
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

import scala.concurrent.{ExecutionContext, Future}

class LiveTaxCreditsServiceSpec
    extends UnitSpec
    with MockitoSugar
    with TestHelpers
    with ScopesConfig {

  trait Setup {

    val cacheService = new CacheService(null, null)(null) {
      override def get[T: Format](cacheId: CacheIdBase,
                                  functionToCache: => Future[T]) =
        functionToCache
    }

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

    when(scopeService.getValidFieldsForCacheKey(any(), any()))
      .thenReturn("test")
    when(scopesHelper.getQueryStringFor(any(), any())).thenReturn("(ABC)")
    when(matchingConnector.resolve(eqTo(testMatchId))(any()))
      .thenReturn(Future.successful(MatchedCitizen(testMatchId, nino)))

  }

  "Live Tax Credits Service" should {

    "return empty list of working tax credits when no records exists for the given matchId" in new Setup {
      when(ifConnector.fetchTaxCredits(any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(createEmpyIfApplications.applications))
      val response = await(
        taxCreditsService
          .getWorkingTaxCredits(testMatchId, testInterval, Seq("testScope")))
      response.isEmpty shouldBe true
    }

    "return list of working tax credits when records exists for the given matchId" in new Setup {
      when(
        ifConnector
          .fetchTaxCredits(eqTo(nino), eqTo(testInterval), any())(any(), any()))
        .thenReturn(
          Future.successful(createValidIfApplicationsMultiple.applications))
      val response = await(
        taxCreditsService
          .getWorkingTaxCredits(testMatchId, testInterval, Seq("testScope")))
      response.isEmpty shouldBe false
    }

    "return empty list of child tax credits when no records exists for the given matchId" in new Setup {
      when(ifConnector.fetchTaxCredits(any(), any(), any())(any(), any()))
        .thenReturn(Future.successful(createEmpyIfApplications.applications))
      val response = await(
        taxCreditsService
          .getChildTaxCredits(testMatchId, testInterval, Seq("testScope")))
      response.isEmpty shouldBe true
    }

    "return list of child tax credits when records exists for the given matchId" in new Setup {
      when(
        ifConnector
          .fetchTaxCredits(eqTo(nino), eqTo(testInterval), any())(any(), any()))
        .thenReturn(
          Future.successful(createValidIfApplicationsMultiple.applications))
      val response = await(
        taxCreditsService
          .getChildTaxCredits(testMatchId, testInterval, Seq("testScope")))
      response.isEmpty shouldBe false
    }
  }
}
