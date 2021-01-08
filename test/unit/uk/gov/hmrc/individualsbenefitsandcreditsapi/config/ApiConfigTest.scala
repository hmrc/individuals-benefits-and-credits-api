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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.config

import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.individualsbenefitsandcreditsapi.config.ApiConfig
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class ApiConfigTest extends UnitSpec with MockitoSugar {

  val config: Configuration = Configuration(
    ("api-config.scopes.scope1.fields", List("A", "B")),
    ("api-config.scopes.scope2.fields", List("C")),
    ("api-config.endpoints.endpoint1.endpoint", "a/b/c"),
    ("api-config.endpoints.endpoint1.title", "Endpoint 1"),
    ("api-config.endpoints.endpoint1.fields.A", "field1"),
    ("api-config.endpoints.endpoint2.endpoint", "d/e/f"),
    ("api-config.endpoints.endpoint2.title", "Endpoint 2"),
    ("api-config.endpoints.endpoint2.fields.B", "field2"),
    ("api-config.endpoints.endpoint2.fields.C", "field3"),
  )

  val apiConfig: ApiConfig = config.get[ApiConfig]("api-config")

  "ApiConfig" should {

    "parse scopes correctly" in {
      apiConfig.scopes.map(s => s.name).toSet shouldBe Set("scope1", "scope2")
      apiConfig
        .getScope("scope1")
        .map(c => c.fields)
        .getOrElse(List()) shouldBe List("A", "B")
      apiConfig
        .getScope("scope2")
        .map(c => c.fields)
        .getOrElse(List()) shouldBe List("C")
    }

    "parse endpoints correctly" in {
      apiConfig.endpoints.map(e => e.name).toSet shouldBe Set("endpoint1",
                                                              "endpoint2")
      val endpoint1 = apiConfig.getEndpoint("endpoint1").get
      endpoint1.fields.keys shouldBe Set("A")
      endpoint1.fields.values.toSet shouldBe Set("field1")
      val endpoint2 = apiConfig.getEndpoint("endpoint2").get
      endpoint2.fields.keys shouldBe Set("B", "C")
      endpoint2.fields.values.toSet shouldBe Set("field2", "field3")
    }

    "parse endpoint links correctly" in {
      apiConfig.getEndpoint("endpoint1").map(c => c.link).get shouldBe "a/b/c"
      apiConfig.getEndpoint("endpoint2").map(c => c.link).get shouldBe "d/e/f"
    }
  }
}
