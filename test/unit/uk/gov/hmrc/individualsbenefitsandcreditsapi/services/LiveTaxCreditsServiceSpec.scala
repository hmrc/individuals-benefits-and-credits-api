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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services

import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors.{
  IfConnector,
  IndividualsMatchingApiConnector
}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.LiveTaxCreditsService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.CacheService

class LiveTaxCreditsServiceSpec extends UnitSpec with MockitoSugar {
  "Live Tax Credits Service" should {
    "return an empty list when no applications received from IF" in {
      val cacheService = mock[CacheService]
      val ifConnector = mock[IfConnector]
      val scopeService = mock[ScopesService]
      val scopesHelper = mock[ScopesHelper]
      val matchingConnector = mock[IndividualsMatchingApiConnector]

      val taxCreditsService =
        new LiveTaxCreditsService(
          cacheService,
          ifConnector,
          scopeService,
          scopesHelper,
          matchingConnector
        )
    }
  }
}
