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

import org.joda.time.Interval
import play.api.hal.Hal._
import play.api.hal.HalLink
import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import play.api.mvc.hal._
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.individualsbenefitsandcreditsapi.audit.AuditHelper
import uk.gov.hmrc.individualsbenefitsandcreditsapi.play.RequestHeaderUtils.{maybeCorrelationId, validateCorrelationId}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services._

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

class WorkingTaxCreditController @Inject()(
    val authConnector: AuthConnector,
    cc: ControllerComponents,
    scopeService: ScopesService,
    implicit val auditHelper: AuditHelper,
    taxCreditsService: TaxCreditsService
) extends CommonController(cc)
    with PrivilegedAuthentication {

  def workingTaxCredit(matchId: UUID, interval: Interval) =
    Action.async { implicit request =>

      val scopes = scopeService.getEndPointScopes("working-tax-credit")

      authenticate(scopes, matchId.toString) { authScopes =>
        val correlationId = validateCorrelationId(request)

        taxCreditsService.getWorkingTaxCredits(matchId, interval, authScopes)
          .map(
            applications => {
              val selfLink = HalLink("self", urlWithInterval(s"/individuals/benefits-and-credits/working-tax-credits?matchId=$matchId", interval.getStart))
              val response = Json.obj("applications" -> Json.toJson(applications))

              auditHelper.workingTaxCreditAuditApiResponse(correlationId.toString, matchId.toString, authScopes.mkString(","),
                request, selfLink.toString, applications)

              Ok(state(response) ++ selfLink)
            }
          )
      } recover withAudit(maybeCorrelationId(request), matchId.toString, "/individuals/benefits-and-credits/working-tax-credits")
    }
}
