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

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.{ScopesHelper, ScopesService}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class ScopesHelperSpec
  extends UnitSpec
    with ScopesConfig
    with BeforeAndAfterEach
    with Matchers {

  "Scopes helper" should {
    val scopesService = new ScopesService(mockConfig)
    val scopesHelper = new ScopesHelper(scopesService)

    "return correct query string" in {
      val result = scopesHelper.getQueryStringFor(List(mockScopeOne, mockScopeTwo), List(endpointOne, endpointTwo, endpointThree))
      result shouldBe "path(to(a,b,c,d,e,f,g,h,i))"
    }
  }
}
