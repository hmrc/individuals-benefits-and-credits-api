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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors

import org.joda.time.Interval
import play.api.Logger
import play.api.mvc.RequestHeader
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{HeaderCarrier, HeaderNames, HttpClient, InternalServerException, JsValidationException, NotFoundException, TooManyRequestException, Upstream4xxResponse, Upstream5xxResponse}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.AuditHelper
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.{IfApplication, IfApplications}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.play.RequestHeaderUtils
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import uk.gov.hmrc.http.HttpReads.Implicits._

class IfConnector @Inject()(servicesConfig: ServicesConfig,
                            http: HttpClient,
                            val auditHelper: AuditHelper) {

  private val logger = Logger(getClass)

  private val integrationFrameworkBearerToken =
    servicesConfig.getString(
      "microservice.services.integration-framework.authorization-token")
  private val integrationFrameworkEnvironment =
    servicesConfig.getString(
      "microservice.services.integration-framework.environment")

  val serviceUrl = servicesConfig.baseUrl("integration-framework")

  def fetchTaxCredits(nino: Nino,
                      interval: Interval,
                      filter: Option[String],
                      matchId: String)
                     (implicit hc: HeaderCarrier, request: RequestHeader, ec: ExecutionContext): Future[Seq[IfApplication]] = {

    val endpoint = "IfConnector::fetchTaxCredits"
    val startDate = interval.getStart.toLocalDate
    val endDate = interval.getEnd.toLocalDate

    val url = s"$serviceUrl/individuals/tax-credits/nino/$nino?" +
      s"startDate=$startDate&endDate=$endDate${filter.map(f => s"&fields=$f").getOrElse("")}"

    call(url, endpoint, matchId)

  }

  private def extractCorrelationId(requestHeader: RequestHeader) =
    RequestHeaderUtils.validateCorrelationId(requestHeader).toString

  def setHeaders(requestHeader: RequestHeader) = Seq(
    HeaderNames.authorisation -> s"Bearer $integrationFrameworkBearerToken",
    "Environment"             -> integrationFrameworkEnvironment,
    "CorrelationId"           -> extractCorrelationId(requestHeader)
  )

  private def call(url: String, endpoint: String, matchId: String)
                  (implicit hc: HeaderCarrier, request: RequestHeader, ec: ExecutionContext) =
    recover[IfApplication](http.GET[IfApplications](url, headers = setHeaders(request)) map {
      response =>
        auditHelper.auditIfApiResponse(extractCorrelationId(request),
          matchId, request, url, response)

        response.applications
      },
      extractCorrelationId(request), matchId, request, url)

  private def recover[A](x: Future[Seq[A]],
                         correlationId: String,
                         matchId: String,
                         request: RequestHeader,
                         requestUrl: String)
                        (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Seq[A]] = x.recoverWith {
    case validationError: JsValidationException => {
      logger.warn("Integration Framework JsValidationException encountered")
      auditHelper.auditIfApiFailure(correlationId, matchId, request, requestUrl, s"Error parsing IF response: ${validationError.errors}")
      Future.failed(new InternalServerException("Something went wrong."))
    }
    case Upstream4xxResponse(msg, 404, _, _) => {
      auditHelper.auditIfApiFailure(correlationId, matchId, request, requestUrl, msg)
      
      msg.contains("NO_DATA_FOUND") match {
        case true => Future.successful(Seq.empty)
        case _    => {
          logger.warn("Integration Framework NotFoundException encountered")
          Future.failed(new NotFoundException(msg))
        }
      }
    }
    case Upstream5xxResponse(msg, code, _, _) => {
      logger.warn(s"Integration Framework Upstream5xxResponse encountered: $code")
      auditHelper.auditIfApiFailure(correlationId, matchId, request, requestUrl, s"Internal Server error: $msg")
      Future.failed(new InternalServerException("Something went wrong."))
    }
    case Upstream4xxResponse(msg, 429, _, _) => {
      logger.warn(s"Integration Framework Rate limited: $msg")
      auditHelper.auditIfApiFailure(correlationId, matchId, request, requestUrl, s"IF Rate limited: $msg")
      Future.failed(new TooManyRequestException(msg))
    }
    case Upstream4xxResponse(msg, code, _, _) => {
      logger.warn(s"Integration Framework Upstream4xxResponse encountered: $code")
      auditHelper.auditIfApiFailure(correlationId, matchId, request, requestUrl, msg)
      Future.failed(new InternalServerException("Something went wrong."))
    }
    case e: Exception => {
      logger.warn(s"Integration Framework Exception encountered")
      auditHelper.auditIfApiFailure(correlationId, matchId, request, requestUrl, e.getMessage)
      Future.failed(new InternalServerException("Something went wrong."))
    }
  }
}
