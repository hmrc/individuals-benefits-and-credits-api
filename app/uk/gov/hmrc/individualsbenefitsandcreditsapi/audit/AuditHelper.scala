/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.audit

import play.api.mvc.RequestHeader
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.models.*
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.models.childtaxcredits.{ChildTaxApiResponseEventModel, CtcApplicationModel}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.models.workingtaxcredits.{WorkingTaxApiResponseEventModel, WtcApplicationModel}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.integrationframework.IfApplications
import uk.gov.hmrc.play.audit.http.connector.AuditConnector

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class AuditHelper @Inject() (auditConnector: AuditConnector)(implicit ec: ExecutionContext) {

  def auditApiResponse(
    correlationId: String,
    matchId: String,
    scopes: String,
    request: RequestHeader,
    selfLink: String
  )(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "ApiResponseEvent",
      RootApiResponseEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        correlationId = Some(correlationId),
        request.headers.get("X-Application-ID").getOrElse("-"),
        scopes,
        returnLinks = selfLink
      )
    )

  def childTaxCreditAuditApiResponse(
    correlationId: String,
    matchId: String,
    scopes: String,
    request: RequestHeader,
    selfLink: String,
    response: Seq[CtcApplicationModel]
  )(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "ApiResponseEvent",
      ChildTaxApiResponseEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        correlationId = Some(correlationId),
        request.headers.get("X-Application-ID").getOrElse("-"),
        scopes,
        returnLinks = selfLink,
        response
      )
    )

  def workingTaxCreditAuditApiResponse(
    correlationId: String,
    matchId: String,
    scopes: String,
    request: RequestHeader,
    selfLink: String,
    response: Seq[WtcApplicationModel]
  )(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "ApiResponseEvent",
      WorkingTaxApiResponseEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        correlationId = Some(correlationId),
        request.headers.get("X-Application-ID").getOrElse("-"),
        scopes,
        returnLinks = selfLink,
        response
      )
    )

  def auditApiFailure(
    correlationId: Option[String],
    matchId: String,
    request: RequestHeader,
    requestUrl: String,
    msg: String
  )(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "ApiFailureEvent",
      ApiFailureResponseEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        correlationId = correlationId,
        request.headers.get("X-Application-ID").getOrElse("-"),
        requestUrl,
        msg
      )
    )

  def auditIfApiResponse(
    correlationId: String,
    matchId: String,
    request: RequestHeader,
    requestUrl: String,
    integrationFrameworkApplications: IfApplications
  )(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "IntegrationFrameworkApiResponseEvent",
      IfApiResponseEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        correlationId = correlationId,
        request.headers.get("X-Application-ID").getOrElse("-"),
        requestUrl = requestUrl,
        integrationFrameworkApplications
      )
    )

  def auditIfApiFailure(
    correlationId: String,
    matchId: String,
    request: RequestHeader,
    requestUrl: String,
    msg: String
  )(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "IntegrationFrameworkApiFailureEvent",
      ApiFailureResponseEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        correlationId = Some(correlationId),
        request.headers.get("X-Application-ID").getOrElse("-"),
        requestUrl,
        msg
      )
    )

  def auditAuthScopes(matchId: String, scopes: String, request: RequestHeader)(implicit hc: HeaderCarrier): Unit =
    auditConnector.sendExplicitAudit(
      "AuthScopesAuditEvent",
      ScopesAuditEventModel(
        deviceId = hc.deviceID.getOrElse("-"),
        input = s"Request to ${request.path}",
        method = request.method.toUpperCase,
        userAgent = request.headers.get("User-Agent").getOrElse("-"),
        apiVersion = "1.0",
        matchId = matchId,
        request.headers.get("X-Application-ID").getOrElse("-"),
        scopes
      )
    )
}
