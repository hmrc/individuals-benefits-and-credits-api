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

import play.api.mvc.hal._
import play.api.hal.HalLink

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.individualsbenefitsandcreditsapi.play.RequestHeaderUtils.extractCorrelationId
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesHelper

import java.util.UUID
import scala.concurrent.ExecutionContext

abstract class RootController @Inject()(
    cc: ControllerComponents,
    scopeService: ScopesService,
    scopesHelper: ScopesHelper,
    taxCreditsService: TaxCreditsService)(implicit val ec: ExecutionContext)
    extends CommonController(cc)
    with PrivilegedAuthentication {

  def root(matchId: UUID): Action[AnyContent] = Action.async {
    implicit request =>
      {
        extractCorrelationId(request)
        requiresPrivilegedAuthentication(scopeService.getAllScopes) {
          authScopes =>
            taxCreditsService.resolve(matchId) map { _ =>
              val selfLink =
                HalLink("self",
                        s"/individuals/benefits-and-credits/?matchId=$matchId")
              Ok(scopesHelper.getHalLinks(matchId, authScopes) ++ selfLink)
            }
        } recover recovery
      }
  }
}

@Singleton
class LiveRootController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    scopesHelper: ScopesHelper,
    liveTaxCreditsService: LiveTaxCreditsService
)(implicit override val ec: ExecutionContext)
    extends RootController(cc,
                           scopeService,
                           scopesHelper,
                           liveTaxCreditsService) {
  override val environment = Environment.PRODUCTION
}

@Singleton
class SandboxRootController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    scopesHelper: ScopesHelper,
    sandboxTaxCreditsService: SandboxTaxCreditsService
)(implicit override val ec: ExecutionContext)
    extends RootController(cc,
                           scopeService,
                           scopesHelper,
                           sandboxTaxCreditsService) {
  override val environment = Environment.SANDBOX
}
