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

// TODO - WIP - Option one is a single Identifier Action that will auth based on confidence level and return all scopes for further validation on the endpoint.

package uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.actions

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.models.IdentifierRequest
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

@com.google.inject.ImplementedBy(classOf[AuthenticatedIdentifierAction])
trait IdentifierAction extends ActionBuilder[IdentifierRequest, AnyContent]

final case class authException(msg: String = "Not Authorised")
  extends AuthorisationException(msg)

class AuthenticatedIdentifierAction @Inject()(
                                               override val authConnector: AuthConnector,
                                               val parser: BodyParsers.Default
                                             )(implicit val executionContext: ExecutionContext)
  extends IdentifierAction
    with AuthorisedFunctions
    with ActionRefiner[Request, IdentifierRequest] {

  // $COVERAGE-OFF$
  def exceptionLogger(aex: AuthorisationException) = {
    Logger.debug(s"AuthenticatedIdentifierAction:Refine - ${aex.getClass}:",
      aex)
  }

  def enrolmentMessage(message: String, parameters: Option[Enrolments]) = {
    Logger.debug(message + parameters.getOrElse(""))
  }

  // $COVERAGE-ON$

  // Simple auth action based on confidence level to pull back all scopes (enrolments)
  override protected def refine[A](
                                    request: Request[A]): Future[Either[Result, IdentifierRequest[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter
      .fromHeadersAndSession(request.headers, Some(request.session))

    // TODO May need to check for SANDBOX here and skip auth accordingly
    authorised(ConfidenceLevel.L300)
      .retrieve(
        Retrievals.allEnrolments
      ) {
        case scopes =>
          Future.successful(Right(IdentifierRequest(request, scopes)))
      }
      .recover {
        case e: AuthorisationException => {
          Logger.debug(
            s"IdentifierAction calling authorised(ConfidenceLevel.L300 - fail: " + e)
          throw e
        }
      }
  }
}
