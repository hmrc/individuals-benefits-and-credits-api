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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache

import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.CacheId
import uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.Interval
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec

import java.time.LocalDateTime
import java.util.UUID

class CacheIdBaseSpec extends UnitSpec with Matchers {

  "CacheIdBase " should {
    "construct id correctly" in {
      val matchId = UUID.randomUUID()
      val interval = Interval(LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-12-31T00:00:00"))
      val fields = "field1,field2"

      val cacheId = CacheId(matchId, interval, fields)
      cacheId.id shouldEqual s"$matchId-${interval.from}-${interval.to}-$fields"
    }
  }

  "CacheIdBase " should {
    "return id as its toString representation" in {
      val matchId = UUID.randomUUID()
      val interval = Interval(LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-12-31T00:00:00"))
      val fields = "field1,field2"

      val cacheId = CacheId(matchId, interval, fields)
      cacheId.toString shouldEqual cacheId.id
    }
  }
}
