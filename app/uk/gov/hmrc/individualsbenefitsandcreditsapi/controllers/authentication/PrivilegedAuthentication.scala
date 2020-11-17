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

import uk.gov.hmrc.auth.core.{AuthorisedFunctions, Enrolment}
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PrivilegedAuthentication extends AuthorisedFunctions {

  val environment: String

  def authPredicate(scopes: Iterable[String]): Predicate = {
    scopes.map(Enrolment(_): Predicate).reduce(_ and _)
  }

  def requiresPrivilegedAuthentication(endpointScopes: Iterable[String])(
      implicit hc: HeaderCarrier): Future[List[String]] = {

    if (endpointScopes.isEmpty) throw new Exception("No scopes defined")

    if (environment == Environment.SANDBOX)
      Future.successful(endpointScopes.toList)
    else {
      authorised(authPredicate(endpointScopes))
        .retrieve(Retrievals.allEnrolments) {
          case scopes =>
            Future.successful(scopes.enrolments.map(e => e.key).toList)
        }
    }
  }
}