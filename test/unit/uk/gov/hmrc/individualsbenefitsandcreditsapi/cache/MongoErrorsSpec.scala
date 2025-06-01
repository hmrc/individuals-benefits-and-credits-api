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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.cache

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.individualsbenefitsandcreditsapi.cache.MongoErrors
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services.ScopesConfig
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

class MongoErrorsSpec extends UnitSpec with MockitoSugar with ScopesConfig with Matchers {

  "MongoErrors.Duplicate" should {

    "match exceptions containing 'E11000' in the message" in {
      val exception = new Exception("E11000 duplicate key error collection")
      MongoErrors.Duplicate.unapply(exception) shouldBe Some(exception)
    }

    "not match exceptions without 'E11000' in the message" in {
      val exception = new Exception("Some other error")
      MongoErrors.Duplicate.unapply(exception) shouldBe None
    }
  }
}
