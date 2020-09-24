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

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import uk.gov.hmrc.individualsbenefitsandcreditsapi.config.AppConfig
import uk.gov.hmrc.individualsbenefitsandcreditsapi.controllers.actions.IdentifierAction

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MicroserviceHelloWorldController @Inject()(
    appConfig: AppConfig,
    identify: IdentifierAction,
    val authConnector: AuthConnector,
    val environment: String,
    cc: ControllerComponents)
    extends CommonController(cc)
    with PrivilegedAuthentication {

  def hello(): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok("Hello world"))
  }

  def hello2(): Action[AnyContent] = identify.async { implicit request =>
    {
      requiresPrivilegedAuthentication(request.scopes) {
        Future.successful(Ok("Hello world"))
      }.recover(recovery)
    }
  }
}
