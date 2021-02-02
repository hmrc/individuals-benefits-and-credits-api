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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import javax.inject.Inject
import org.joda.time.DateTime
import play.api.mvc.{ControllerComponents, Request, RequestHeader, Result}
import uk.gov.hmrc.auth.core.AuthorisationException
import uk.gov.hmrc.http.{BadRequestException, TooManyRequestException}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.AuditHelper
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.{ErrorInvalidRequest, ErrorNotFound, ErrorTooManyRequests, ErrorUnauthorized, MatchNotFoundException}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.Dates.toFormattedLocalDate
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

abstract class CommonController @Inject()(
    cc: ControllerComponents
) extends BackendController(cc) {

  private def getQueryParam[T](name: String)(implicit request: Request[T]) =
    request.queryString.get(name).flatMap(_.headOption)

  private[controllers] def urlWithInterval[T](url: String, fromDate: DateTime)(
      implicit request: Request[T]) = {
    val urlWithFromDate = s"$url&fromDate=${toFormattedLocalDate(fromDate)}"
    getQueryParam("toDate") map (toDate => s"$urlWithFromDate&toDate=$toDate") getOrElse urlWithFromDate
  }

  private[controllers] def recovery: PartialFunction[Throwable, Result] = {
    case _: MatchNotFoundException    => ErrorNotFound.toHttpResponse
    case e: AuthorisationException    => ErrorUnauthorized(e.getMessage).toHttpResponse
    case tmr: TooManyRequestException => ErrorTooManyRequests.toHttpResponse
    case e: IllegalArgumentException  => ErrorInvalidRequest(e.getMessage).toHttpResponse
  }

  private[controllers] def withAudit(correlationId: Option[String], matchId: String, url: String)
                                   (implicit request: RequestHeader,
                                    auditHelper: AuditHelper): PartialFunction[Throwable, Result] = {
    case _: MatchNotFoundException   => {
      auditHelper.auditApiFailure(correlationId, matchId, request, url, "Not Found")
      ErrorNotFound.toHttpResponse
    }
    case e: AuthorisationException   => {
      auditHelper.auditApiFailure(correlationId, matchId, request, url, e.getMessage)
      ErrorUnauthorized(e.getMessage).toHttpResponse
    }
    case tmr: TooManyRequestException  => {
      auditHelper.auditApiFailure(correlationId, matchId, request, url, tmr.getMessage)
      ErrorTooManyRequests.toHttpResponse
    }
    case br: BadRequestException  => {
      auditHelper.auditApiFailure(correlationId, matchId, request, url, br.getMessage)
      ErrorInvalidRequest(br.getMessage).toHttpResponse
    }
    case e: IllegalArgumentException => {
      auditHelper.auditApiFailure(correlationId, matchId, request, url, e.getMessage)
      ErrorInvalidRequest(e.getMessage).toHttpResponse
    }
  }
}

object Environment {
  val SANDBOX = "SANDBOX"
  val PRODUCTION = "PRODUCTION"
}
