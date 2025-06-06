/*
 * Copyright 2025 HM Revenue & Customs
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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.config

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.individualsbenefitsandcreditsapi.config.AppConfig

class AppConfigSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "AppConfig" should {

    "return the correct authBaseUrl" in {
      val mockServicesConfig = mock[ServicesConfig]
      when(mockServicesConfig.baseUrl("auth")).thenReturn("http://auth-service")

      val mockConfig = mock[Configuration]
      when(mockConfig.get[Boolean]("auditing.enabled")).thenReturn(true)
      when(mockConfig.get[String]("microservice.metrics.graphite.host")).thenReturn("graphite-host")

      val appConfig = new AppConfig(mockConfig, mockServicesConfig)

      appConfig.authBaseUrl shouldBe "http://auth-service"
    }

    "return the correct auditingEnabled value" in {
      val mockServicesConfig = mock[ServicesConfig]

      val mockConfig = mock[Configuration]
      when(mockConfig.get[Boolean]("auditing.enabled")).thenReturn(true)
      when(mockConfig.get[String]("microservice.metrics.graphite.host")).thenReturn("graphite-host")

      val appConfig = new AppConfig(mockConfig, mockServicesConfig)

      appConfig.auditingEnabled shouldBe true
    }

    "return the correct graphiteHost value" in {
      val mockServicesConfig = mock[ServicesConfig]

      val mockConfig = mock[Configuration]
      when(mockConfig.get[Boolean]("auditing.enabled")).thenReturn(true)
      when(mockConfig.get[String]("microservice.metrics.graphite.host")).thenReturn("graphite-host")

      val appConfig = new AppConfig(mockConfig, mockServicesConfig)

      appConfig.graphiteHost shouldBe "graphite-host"
    }
  }
}
