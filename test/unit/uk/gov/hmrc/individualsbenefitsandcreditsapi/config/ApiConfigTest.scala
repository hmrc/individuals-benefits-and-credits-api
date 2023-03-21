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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.config

import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.individualsbenefitsandcreditsapi.config.ApiConfig
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.service.ScopesConfig
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class ApiConfigTest extends UnitSpec with MockitoSugar with ScopesConfig {

  val apiConfig: ApiConfig = mockConfig.get[ApiConfig]("api-config")

  "ApiConfig" should {

    "parse scopes correctly" in {
      apiConfig.scopes.map(s => s.name).toSet shouldBe Set(mockScopeOne, mockScopeTwo)
      apiConfig
        .getScope(mockScopeOne)
        .map(c => c.fields)
        .getOrElse(List()) shouldBe List("A", "B", "C", "D")
      apiConfig
        .getScope(mockScopeTwo)
        .map(c => c.fields)
        .getOrElse(List()) shouldBe List("E", "F", "G", "H", "I")
    }

    "parse endpoints correctly" in {
      apiConfig.internalEndpoints.map(e => e.name).toSet shouldBe Set(endpointOne, endpointTwo, endpointThree)
      val endpoint1 = apiConfig.getInternalEndpoint(endpointOne).get
      endpoint1.fields.keys shouldBe Set("A", "B", "C")
      endpoint1.fields.values.toSet shouldBe  Set("path/to/a", "path/to/b", "path/to/c")
    }

    "parse endpoint links correctly" in {
      apiConfig.getInternalEndpoint(endpointOne).map(c => c.link).get shouldBe "/internal/1"
      apiConfig.getExternalEndpoint(endpointTwo).map(c => c.link).get shouldBe "/external/2"
    }
  }
}
