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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.services

import org.joda.time.Interval
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors.{
  IfConnector,
  IndividualsMatchingApiConnector
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.childtaxcredits.CtcApplication
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.workingtaxcredits.WtcApplication
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.{
  MatchNotFoundException,
  MatchedCitizen
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.sandbox.SandboxData._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.{
  ScopesHelper,
  ScopesService
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.{
  CacheId,
  CacheService
}

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.Future.{failed, successful}
import scala.concurrent.{ExecutionContext, Future}

trait TaxCreditsService {

  def resolve(matchId: UUID)(implicit hc: HeaderCarrier): Future[MatchedCitizen]

  def getWorkingTaxCredits(matchId: UUID,
                           interval: Interval,
                           endpoint: String,
                           scopes: Iterable[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[WtcApplication]]

  def getChildTaxCredits(matchId: UUID,
                         interval: Interval,
                         endpoint: String,
                         scopes: Iterable[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[CtcApplication]]
}

class LiveTaxCreditsService @Inject()(
    cacheService: CacheService,
    ifConnector: IfConnector,
    scopesService: ScopesService,
    scopesHelper: ScopesHelper,
    individualsMatchingApiConnector: IndividualsMatchingApiConnector
) extends TaxCreditsService {
  override def getWorkingTaxCredits(matchId: UUID,
                                    interval: Interval,
                                    endpoint: String,
                                    scopes: Iterable[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[WtcApplication]] = {

    val cacheid = CacheId(
      matchId,
      interval,
      scopesService.getValidFieldsForCacheKey(scopes.toList,
                                              List("working-tax-credit")))

    cacheService
      .get(
        cacheid, {
          resolve(matchId)
            .flatMap(ninoMatch => {
              val fieldsQuery =
                scopesHelper.getQueryStringFor(scopes.toList, endpoint)
              ifConnector
                .fetchTaxCredits(ninoMatch.nino,
                                 interval,
                                 Option(fieldsQuery).filter(_.nonEmpty))
            })
        }
      )
      .map(applications => applications.map(WtcApplication.create))
  }

  override def resolve(matchId: UUID)(
      implicit hc: HeaderCarrier): Future[MatchedCitizen] =
    individualsMatchingApiConnector.resolve(matchId)

  override def getChildTaxCredits(matchId: UUID,
                                  interval: Interval,
                                  endpoint: String,
                                  scopes: Iterable[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[CtcApplication]] = {

    val cacheid = CacheId(
      matchId,
      interval,
      scopesService.getValidFieldsForCacheKey(scopes.toList,
                                              List("child-tax-credit")))

    cacheService
      .get(
        cacheid, {
          resolve(matchId)
            .flatMap(ninoMatch => {
              val fieldsQuery =
                scopesHelper.getQueryStringFor(scopes.toList, endpoint)
              ifConnector.fetchTaxCredits(
                ninoMatch.nino,
                interval,
                Option(fieldsQuery).filter(_.nonEmpty))
            })
        }
      )
      .map(applications => applications.map(CtcApplication.create))
  }
}

class SandboxTaxCreditsService @Inject()() extends TaxCreditsService {

  override def resolve(matchId: UUID)(
      implicit hc: HeaderCarrier): Future[MatchedCitizen] =
    if (matchId.equals(sandboxMatchId))
      successful(MatchedCitizen(sandboxMatchId, sandboxNino))
    else failed(new MatchNotFoundException)

  override def getWorkingTaxCredits(matchId: UUID,
                                    interval: Interval,
                                    endpoint: String,
                                    scopes: Iterable[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[WtcApplication]] = {

    resolve(matchId).flatMap(
      _ =>
        Future.successful(
          WorkingTaxCredits.Applications.applications
      ))

  }

  override def getChildTaxCredits(matchId: UUID,
                                  interval: Interval,
                                  endpoint: String,
                                  scopes: Iterable[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[CtcApplication]] = {
    resolve(matchId).flatMap(
      _ =>
        Future.successful(
          ChildTaxCredits.Applications.applications
      ))
  }
}