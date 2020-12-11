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
import play.api.libs.json.Json
import play.api.mvc.hal._
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{
  LiveTaxCreditsService,
  SandboxTaxCreditsService,
  TaxCreditsService
}

import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

abstract class WorkingTaxCreditController @Inject()(
    cc: ControllerComponents,
    scopeService: ScopesService,
    taxCreditsService: TaxCreditsService
) extends CommonController(cc)
    with PrivilegedAuthentication {

  def workingTaxCredit(matchId: UUID, interval: Interval): Action[AnyContent] =
    Action.async { implicit request =>
      val scopes =
        scopeService.getEndPointScopes("working-tax-credit")

      requiresPrivilegedAuthentication(scopes)
        .flatMap { authScopes =>
          taxCreditsService
            .getWorkingTaxCredits(matchId,
                                  interval,
                                  "working-tax-credit",
                                  authScopes)
            .map(
              applications => {
                throw new Exception("NOT_IMPLEMENTED")
                val selfLink =
                  HalLink("self",
                          urlWithInterval(
                            s"/individuals/employments/paye?matchId=$matchId",
                            interval.getStart))
                val wtcJsonObj = Json.obj("applications" -> applications)
                Ok(state(wtcJsonObj) ++ selfLink)
              }
            )
        }
        .recover(recovery)
    }

}

@Singleton
class LiveWorkingTaxCreditController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    taxCreditsService: LiveTaxCreditsService
) extends WorkingTaxCreditController(cc, scopeService, taxCreditsService) {
  override val environment = Environment.PRODUCTION
}

@Singleton
class SandboxWorkingTaxCreditController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    taxCreditsService: SandboxTaxCreditsService
) extends WorkingTaxCreditController(cc, scopeService, taxCreditsService) {
  override val environment = Environment.SANDBOX
}
