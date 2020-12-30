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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors

import javax.inject.Inject
import org.joda.time.Interval
import play.api.Logger
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.http.{
  HeaderCarrier,
  HttpClient,
  NotFoundException,
  TooManyRequestException,
  Upstream4xxResponse
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{
  IfApplication,
  IfApplications
}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.{ExecutionContext, Future}

class IfConnector @Inject()(servicesConfig: ServicesConfig, http: HttpClient)(
    implicit ec: ExecutionContext) {

  private val integrationFrameworkBearerToken =
    servicesConfig.getString(
      "microservice.services.integration-framework.authorization-token")
  private val integrationFrameworkEnvironment =
    servicesConfig.getString(
      "microservice.services.integration-framework.environment")

  val serviceUrl = servicesConfig.baseUrl("integration-framework")

  def fetchTaxCredits(nino: Nino, interval: Interval, filter: Option[String])(
      implicit hc: HeaderCarrier,
      ec: ExecutionContext): Future[Seq[IfApplication]] = {
    val startDate = interval.getStart.toLocalDate
    val endDate = interval.getEnd.toLocalDate
    val payeUrl = s"$serviceUrl/individuals/tax-credits/nino/$nino?" +
      s"startDate=$startDate&endDate=$endDate${filter.map(f => s"&fields=$f").getOrElse("")}"
    recover[IfApplication](
      http
        .GET[IfApplications](payeUrl)(implicitly, header(), ec)
        .map(_.applications))
  }

  private def header(extraHeaders: (String, String)*)(
      implicit hc: HeaderCarrier) =
    hc.copy(
        authorization =
          Option(Authorization(s"Bearer $integrationFrameworkBearerToken")))
      .withExtraHeaders(
        Seq("Environment" -> integrationFrameworkEnvironment) ++ extraHeaders: _*)

  def recover[A](x: Future[Seq[A]]): Future[Seq[A]] = x.recoverWith {
    case _: NotFoundException => Future.successful(Seq.empty)
    case Upstream4xxResponse(msg, 429, _, _) => {
      Logger.warn(s"IF Rate limited: $msg")
      Future.failed(new TooManyRequestException(msg))
    }
  }

}
