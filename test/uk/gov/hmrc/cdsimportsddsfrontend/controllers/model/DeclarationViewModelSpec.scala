/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.model

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

class DeclarationViewModelSpec extends WordSpec with Matchers {

  "DeclarationViewModel" should {
    "convert to Declaration" in {
      val viewModel = DeclarationViewModel(
        declarationType = DeclarationTypeViewModel(
          "untested", "untested", "77", "untested", "untested", "untested", "untested"
        ),
        valuationInformationAndTaxesViewModel = ValuationInformationAndTaxesViewModel(
          customsValuationMethodCode = Some("1"),
          headerChargeDeduction = Some(ChargeDeduction(typeCode = "header type",
                                                currencyAmount = CurrencyAmount(currency = "XYZ", amount = "87"))),
          itemChargeDeduction = Some(ChargeDeduction(typeCode = "item type",
                                                currencyAmount = CurrencyAmount(currency = "GBP", amount = "65"))),
          additionCode = Some("7890"),
          itemChargeAmount = Some(CurrencyAmount("EUR", "45"))
        ),
        whenAndWhereViewModel = WhenAndWhereViewModel(
          exportCountry = Some("DE"),
          preferentialOriginCountryCode = Some("GB"),
          preferentialOriginTypeCode = Some("2"),
          goodsLocationName = Some("FOO"),
          goodsLocationType = Some("A"),
          goodsLocationAddress = Some(AddressViewModel(
            Some("1 Street Lane"), Some("Cityville"), Some("US"), Some("90210"), Some("U")))
          ),
        miscellaneousViewModel = MiscellaneousViewModel(
          quotaOrderNumber = Some("123"),
          natureOfTransaction = Some("3"),
          statisticalValue = Some(CurrencyAmount("DKK", "8080"))
        )
      )

      val declaration = viewModel.toDeclaration

      declaration.commodity shouldBe Some(Commodity(Some("TSP no description required"),
        List(Classification(Some("76071111"), Some("TSP")), Classification(Some("10"), Some("TRC")),
          Classification(Some("1234"), Some("TRA")), Classification(Some("VATZ"), Some("GN"))),
        Some(GoodsMeasure(Some("50"), Some("100"), Some("60"))),
        Some(DutyTaxFee(Some("100"), Some("123"), Some(Payment("E")))),
        Some(InvoiceLine(Some(CurrencyAmount("EUR", "45"))))))


      declaration.packaging shouldBe Some(Packaging(Some("BF"), Some("1"), Some("TSP not required")))

      declaration.borderTransportMeans shouldBe Some(BorderTransportMeans(Some("US"), Some("4")))

      declaration.goodsShipment.consignment shouldBe Some(Consignment(
          Some("0"),
          Some(ArrivalTransportMeans(Some("10"), Some("1023465738"))),
          Some(GoodsLocation(Some("FOO"),
            Some("A"),
            Some(Address(Some("1 Street Lane"), Some("Cityville"), Some("US"), Some("90210"), Some("U"))))),
          Some(LoadingLocation("JFK"))
        )
      )

      declaration.headerCustomsValuation shouldBe Some(HeaderCustomsValuation(
        chargeDeduction = Some(ChargeDeduction(typeCode = "header type", currencyAmount = CurrencyAmount(currency = "XYZ", amount = "87")))
      ))

      declaration.itemCustomsValuation shouldBe Some(ItemCustomsValuation(
        chargeDeduction = Some(ChargeDeduction(typeCode = "item type", currencyAmount = CurrencyAmount(currency = "GBP", amount = "65"))),
        methodCode = Some("1")))

      declaration.goodsShipment.destination shouldBe Some(Destination(countryCode = Some("GB")))
      declaration.goodsShipment.exportCountry shouldBe Some(ExportCountry("DE"))

      declaration.goodsShipment.governmentAgencyGoodsItem shouldBe GovernmentAgencyGoodsItem(
        origin = Seq(Origin(countryCode = Some("FR"), typeCode = Some("1")),
                     Origin(countryCode = Some("GB"), typeCode = Some("2"))),
        sequenceNumeric = "77",
        valuationAdjustment = Some(ValuationAdjustment(additionCode = "7890")),
        transactionNatureCode = Some("3"),
        statisticalValue = Some(CurrencyAmount("DKK", "8080"))
      )

      declaration.obligationGuarantee shouldBe Some(ObligationGuarantee(None, None, None, None, None, None))
    }
  }
}
