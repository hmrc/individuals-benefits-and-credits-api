package unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.services

import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.individualsbenefitsandcreditsapi.connectors.{IfConnector, IndividualsMatchingApiConnector}
import unit.uk.gov.hmrc.individualsbenefitsandcreditsapi.utils.UnitSpec
import uk.gov.hmrc.individualsbenefitsandcreditsapi.service._
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.LiveTaxCreditsService
import uk.gov.hmrc.individualsbenefitsandcreditsapi.services.cache.CacheService

class LiveTaxCreditsServiceSpec extends UnitSpec with MockitoSugar {
  "Live Tax Credits Service" should {
    "return an empty list when no applications received from IF" in {
      val cacheService = mock[CacheService]()
      val ifConnector = mock[IfConnector]()
      val scopeService = mock[ScopesService]()
      val scopesHelper = mock[ScopesHelper]()
      val matchingConnector = mock[IndividualsMatchingApiConnector]()

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
