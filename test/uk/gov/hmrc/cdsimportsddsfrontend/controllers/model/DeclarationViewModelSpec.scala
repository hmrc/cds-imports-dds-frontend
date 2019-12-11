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

import java.util.UUID

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

class DeclarationViewModelSpec extends WordSpec with Matchers {

  "DeclarationViewModel" should {
    "convert to Declaration" in {
      val viewModel = DeclarationViewModel(
        declarationType = DeclarationType(
          "untested", "untested", "77", "untested", "untested", "untested", "untested"
        ),
        documentationAndReferences = DocumentationAndReferencesViewModel(
          Seq(
            PreviousDocument(Some("Y"),Some("20191101"), Some("CLE"),Some("1")),
            PreviousDocument(Some("Y"),Some("9GB201909014000"), Some("DCR"),Some("1")),
            PreviousDocument(Some("Y"),Some("20191101"), Some("CLE"),Some("1")),
            PreviousDocument(Some("Y"),Some("9GB201909014000"), Some("DCR"),Some("1"))
          ),
          Seq(
            PreviousDocument(Some("Y"),Some("20191101"), Some("CLE"),Some("1")),
            PreviousDocument(Some("Y"),Some("9GB201909014000"), Some("DCR"),Some("1")),
            PreviousDocument(Some("Z"),Some("20191103"), Some("ZZZ"),Some("1")),
            PreviousDocument(Some("Z"),Some("9GB201909014002"), Some("235"),Some("1")),
            PreviousDocument(Some("Z"),Some("9GB201909014003"), Some("ZZZ"),Some("1")),
            PreviousDocument(Some("Z"),Some("9GB201909014004"), Some("270"),Some("1"))
          ),
          AdditionalInformation(Some("TSP01"), Some("TSP")),
          Nil,
          Seq(
            AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), Some("DocumentName1")),
            AdditionalDocumentViewModel(Some("C"), Some("514"), Some("GBEIR201909014000"), None, None),
            AdditionalDocumentViewModel(Some("C"), Some("506"), Some("GBDPO1909241"), None, None),
            AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.07.2019"), Some("AC"), None),
            AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.08.2019"), Some("AC"), None),
            AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), None)
          ),
          Some(UUID.randomUUID().toString.replaceAll("-","").take(20)),
          Seq(
            AdditionalPaymentType(Some("1909241"), Some("1"), Some("DAN")),
            AdditionalPaymentType(Some("1909241"), Some("1"), Some("DAN"))
          )
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
            preferentialOriginCountryCode = Some("GB"),
            preferentialOriginTypeCode = Some("2")),
        miscellaneousViewModel = MiscellaneousViewModel(
          natureOfTransaction = Some("3"),
          statisticalValue = Some(CurrencyAmount("DKK", "8080")),
          writeOffViewModel = Seq(
            WriteOffViewModel(
              issuingAuthority = Some("Auth"),
              dateOfValidity = Some("20201212000000Z"),
              quantity = Some("1"),
              measurementUnitAndQualifier = Some("Qualifies")
            ),
            WriteOffViewModel(None,None,None,None),
            WriteOffViewModel(None,None,None,None),
            WriteOffViewModel(None,None,None,None),
            WriteOffViewModel(None,None,None,None),
            WriteOffViewModel(None,None,None,None)
          )
        )
      )

      val declaration = viewModel.toDeclaration

      declaration.commodity shouldBe Some(Commodity(Some("TSP no description required"),
        List(Classification(Some("76071111"), Some("TSP")), Classification(Some("10"), Some("TRC")),
          Classification(Some("1234"), Some("TRA")), Classification(Some("VATZ"), Some("GN"))),
        Some(GoodsMeasure(Some("50"), Some("100"), Some("60"))),
        Some(DutyTaxFee(Some("100"), Some(Payment("E")))),
        Some(InvoiceLine(Some(CurrencyAmount("EUR", "45"))))))


      declaration.packaging shouldBe Some(Packaging(Some("BF"), Some("1"), Some("TSP not required")))

      declaration.borderTransportMeans shouldBe Some(BorderTransportMeans(Some("US"), Some("4")))

      declaration.goodsShipment.consignment shouldBe Some(Consignment(
          Some("0"),
          Some(ArrivalTransportMeans(Some("10"), Some("1023465738"))),
          Some(GoodsLocation(Some("FXTFXTFXT"),
            Some("A"),
            Some(Address(None, None, Some("GB"),
              None, Some("U"))))),
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
      declaration.goodsShipment.exportCountry shouldBe Some(ExportCountry(id = Some("FR")))

      declaration.goodsShipment.governmentAgencyGoodsItem shouldBe GovernmentAgencyGoodsItem(
        additionalDocuments = Seq(
          AdditionalDocument(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), Some("DocumentName1"),
            Some(Submitter(name = Some("Auth"))), Some("20201212000000Z"), Some(WriteOff(quantityQuantity = Some("1"),
              unitCode = Some("Qualifies")))),
          AdditionalDocument(Some("C"), Some("514"), Some("GBEIR201909014000"), None, None, None, None, None),
          AdditionalDocument(Some("C"), Some("506"), Some("GBDPO1909241"), None, None, None, None, None),
          AdditionalDocument(Some("N"), Some("935"), Some("12345/30.07.2019"), Some("AC"), None, None, None, None),
          AdditionalDocument(Some("N"), Some("935"), Some("12345/30.08.2019"), Some("AC"), None, None, None, None),
          AdditionalDocument(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), None, None, None, None)
        ),
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
