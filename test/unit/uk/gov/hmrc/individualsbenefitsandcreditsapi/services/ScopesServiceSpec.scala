/*
 * Copyright 2023 HM Revenue & Customs
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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services

import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.ScopesService
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class ScopesServiceSpec extends UnitSpec with Matchers with ScopesConfig {

  val scopesService = new ScopesService(mockConfig)

  "Gets correct internal endpoints from configuration for first scope" in {
    val endpoints = scopesService.getInternalEndpoints(Seq(mockScopeOne))
    endpoints.size shouldBe 2
    endpoints.map(_.link) shouldBe Seq("/internal/1", "/internal/2")
    endpoints.map(_.title) shouldBe Seq("Get the first endpoint", "Get the second endpoint")
  }

  "Gets correct internal endpoints from configuration for second scope" in {
    val endpoints = scopesService.getInternalEndpoints(Seq(mockScopeTwo))
    endpoints.map(_.link) shouldBe Seq("/internal/2", "/internal/3")
    endpoints.map(_.title) shouldBe Seq("Get the second endpoint", "Get the third endpoint")
  }

  "Gets all scopes correctly" in {
    val scopes = scopesService.getAllScopes
    scopes shouldBe Seq(mockScopeOne, mockScopeTwo)
  }
}
