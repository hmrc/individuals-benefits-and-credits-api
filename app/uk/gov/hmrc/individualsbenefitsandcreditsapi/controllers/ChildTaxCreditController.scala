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

import org.joda.time.Interval
import play.api.hal.Hal._
import play.api.hal.HalLink
import play.api.mvc.hal._
import play.api.libs.json.Json

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{
  LiveTaxCreditsService,
  SandboxTaxCreditsService,
  TaxCreditsService
}

import java.util.UUID
import scala.concurrent.ExecutionContext

abstract class ChildTaxCreditController @Inject()(
    cc: ControllerComponents,
    scopeService: ScopesService,
    taxCreditsService: TaxCreditsService
)(implicit val ec: ExecutionContext)
    extends CommonController(cc)
    with PrivilegedAuthentication {

  def childTaxCredit(matchId: UUID, interval: Interval) =
    Action.async { implicit request =>
      val scopes =
        scopeService.getEndPointScopes("working-tax-credit")

      requiresPrivilegedAuthentication(scopes) { authScopes =>
        taxCreditsService
          .getWorkingTaxCredits(matchId,
                                interval,
                                "working-tax-credit",
                                authScopes)
          .map(
            applications => {
              val selfLink =
                HalLink(
                  "self",
                  urlWithInterval(
                    s"/individuals/benefits-and-credits/child-tax-credits?matchId=$matchId",
                    interval.getStart))
              val wtcJsonObj =
                Json.obj("applications" -> Json.toJson(applications))
              Ok(state(wtcJsonObj) ++ selfLink)
            }
          )
      }.recover(recovery)
    }

}

@Singleton
class LiveChildTaxCreditController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    liveTaxCreditsService: LiveTaxCreditsService
)(implicit override val ec: ExecutionContext)
    extends ChildTaxCreditController(cc, scopeService, liveTaxCreditsService) {
  override val environment = Environment.PRODUCTION
}

@Singleton
class SandboxChildTaxCreditController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    sandboxTaxCreditsService: SandboxTaxCreditsService
)(implicit override val ec: ExecutionContext)
    extends ChildTaxCreditController(cc, scopeService, sandboxTaxCreditsService) {
  override val environment = Environment.SANDBOX
}
