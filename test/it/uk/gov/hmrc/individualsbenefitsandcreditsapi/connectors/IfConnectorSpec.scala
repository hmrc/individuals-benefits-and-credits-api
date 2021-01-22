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

package it.uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.{
  aResponse,
  configureFor,
  equalTo,
  get,
  stubFor,
  urlPathMatching
}
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.BeforeAndAfterEach
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import testUtils.{TestDates, TestHelpers}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.http.{
  BadRequestException,
  HeaderCarrier,
  Upstream5xxResponse
}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors.IfConnector
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import scala.concurrent.ExecutionContext

class IfConnectorSpec
    extends SpecBase
    with BeforeAndAfterEach
    with TestHelpers
    with TestDates {
  val stubPort = sys.env.getOrElse("WIREMOCK", "11122").toInt
  val stubHost = "localhost"
  val wireMockServer = new WireMockServer(wireMockConfig().port(stubPort))
  val integrationFrameworkAuthorizationToken = "IF_TOKEN"
  val integrationFrameworkEnvironment = "IF_ENVIRONMENT"
  val clientId = "CLIENT_ID"

  def externalServices: Seq[String] = Seq.empty

  override lazy val fakeApplication = new GuiceApplicationBuilder()
    .bindings(bindModules: _*)
    .configure(
      "microservice.services.integration-framework.host" -> "localhost",
      "microservice.services.integration-framework.port" -> "11122",
      "microservice.services.integration-framework.authorization-token" -> integrationFrameworkAuthorizationToken,
      "microservice.services.integration-framework.environment" -> integrationFrameworkEnvironment
    )
    .build()

  implicit val ec: ExecutionContext =
    fakeApplication.injector.instanceOf[ExecutionContext]

  trait Setup {
    val matchId = "80a6bb14-d888-436e-a541-4000674c60aa"

    implicit val hc = HeaderCarrier()

    val underTest = fakeApplication.injector.instanceOf[IfConnector]
  }

  override def beforeEach() {
    wireMockServer.start()
    configureFor(stubHost, stubPort)
  }

  override def afterEach() {
    wireMockServer.stop()
  }

  val applicationsData = createValidIfApplications
  val idType = "nino"
  val idValue = "NA000799C"

  "fetch details" should {

    val startDate = "2016-01-01"
    val endDate = "2017-03-01"
    val interval = toInterval(startDate, endDate)
    val nino = Nino("NA000799C")

    "Fail when IF returns an error" in new Setup {
      stubFor(
        get(urlPathMatching(s"/individuals/tax-credits/$idType/$idValue"))
          .withQueryParam("startDate", equalTo(startDate))
          .withQueryParam("endDate", equalTo(endDate))
          .willReturn(aResponse().withStatus(500)))

      intercept[Upstream5xxResponse] {
        await(underTest.fetchTaxCredits(nino, interval, None, matchId))
      }
    }

    "Fail when IF returns a bad request" in new Setup {
      stubFor(
        get(urlPathMatching(s"/individuals/tax-credits/$idType/$idValue"))
          .withQueryParam("startDate", equalTo(startDate))
          .withQueryParam("endDate", equalTo(endDate))
          .willReturn(aResponse().withStatus(400)))

      intercept[BadRequestException] {
        await(underTest.fetchTaxCredits(nino, interval, None, matchId))
      }
    }

    "for standard response" in new Setup {
      stubFor(
        get(urlPathMatching(s"/individuals/tax-credits/$idType/$idValue"))
          .withQueryParam("startDate", equalTo(startDate))
          .withQueryParam("endDate", equalTo(endDate))
          .withHeader(
            "Authorization",
            equalTo(s"Bearer $integrationFrameworkAuthorizationToken"))
          .withHeader("Environment", equalTo(integrationFrameworkEnvironment))
          .willReturn(aResponse()
            .withStatus(200)
            .withBody(Json.toJson(applicationsData).toString())))

      val result =
        await(underTest.fetchTaxCredits(nino, interval, None, matchId))
      result shouldBe applicationsData.applications
    }
  }
}
