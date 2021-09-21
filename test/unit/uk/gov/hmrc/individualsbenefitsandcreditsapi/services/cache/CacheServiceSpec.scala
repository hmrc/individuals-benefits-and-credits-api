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

package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache

import org.mockito.ArgumentMatchers.{eq => eqTo, _}
import org.mockito.BDDMockito.given
import org.mockito.Mockito.{verify, verifyNoInteractions}
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.individualsbenefitsandcreditsapi.cache.{CacheRepository, CacheRepositoryConfiguration}
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.{CacheIdBase, CacheService}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.SpecBase

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CacheServiceSpec
    extends SpecBase
    with MockitoSugar
    with Matchers
    with BeforeAndAfterEach {

  val cacheId = TestCacheId("someid")
  val cachedValue = TestClass("cached value")
  val newValue = TestClass("new value")

  trait Setup {

    val mockClient = mock[CacheRepository]
    val mockCacheConfig = mock[CacheRepositoryConfiguration]
    val cacheService = new CacheService(mockClient, mockCacheConfig)

    implicit val hc: HeaderCarrier = HeaderCarrier()

    given(mockCacheConfig.cacheEnabled).willReturn(true)

  }

  "cacheService.get" should {

    "ignore the cache when caching is not enabled" in new Setup {

      given(mockCacheConfig.cacheEnabled).willReturn(false)
      await(cacheService.get[TestClass](cacheId, Future.successful(newValue))) shouldBe newValue
      verifyNoInteractions(mockClient)

    }
  }
}

case class TestCacheId(id: String) extends CacheIdBase

case class TestClass(value: String)

object TestClass {

  implicit val format: OFormat[TestClass] = Json.format[TestClass]

}
