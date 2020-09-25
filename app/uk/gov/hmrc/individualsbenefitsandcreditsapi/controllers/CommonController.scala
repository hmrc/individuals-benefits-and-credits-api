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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers

import javax.inject.Inject
import play.api.mvc.{ControllerComponents, Result}
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.{
  AuthorisationException,
  AuthorisedFunctions,
  Enrolment,
  Enrolments
}
import uk.gov.hmrc.http.{HeaderCarrier, TooManyRequestException}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.domains.{
  ErrorInvalidRequest,
  ErrorNotFound,
  ErrorTooManyRequests,
  ErrorUnauthorized,
  MatchNotFoundException,
  ScopeAuthorisationException
}
import uk.gov.hmrc.play.bootstrap.controller.BackendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class CommonController @Inject()(
    cc: ControllerComponents
) extends BackendController(cc) {

  private[controllers] def recovery: PartialFunction[Throwable, Result] = {
    case _: MatchNotFoundException => ErrorNotFound.toHttpResponse
    case e: AuthorisationException =>
      ErrorUnauthorized(e.getMessage).toHttpResponse
    case _: TooManyRequestException => ErrorTooManyRequests.toHttpResponse
    case e: IllegalArgumentException =>
      ErrorInvalidRequest(e.getMessage).toHttpResponse
  }
}

trait PrivilegedAuthentication extends AuthorisedFunctions {

  val environment: String

  def hasScope(authScopes: List[String],
               endpointScopes: List[String]): Boolean = {
    endpointScopes.map(scope => authScopes.contains(scope)).contains(true)
  }

  def validateScopes(enrolments: Enrolments, endpointScopes: List[String])(
      block: => Future[Result])(implicit hc: HeaderCarrier): Future[Result] = {

    //Rule: If a scope is supplied from auth that matches any one scope in the list of endpoint scopes then
    //the request is authenticated
    val authScopes =
      enrolments.enrolments.map(enrolment => enrolment.key).toList

    if (hasScope(authScopes, endpointScopes))
      block
    else
      throw new ScopeAuthorisationException("Not authorised")
  }

  def requiresPrivilegedAuthentication(authScopes: Enrolments,
                                       endpointScopes: List[String])(
      block: => Future[Result])(implicit hc: HeaderCarrier): Future[Result] = {

    if (environment == Environment.SANDBOX)
      block
    else
      validateScopes _
    block
  }
}

object Environment {
  val SANDBOX = "SANDBOX"
  val PRODUCTION = "PRODUCTION"
}
