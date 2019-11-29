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

package uk.gov.hmrc.cdsimportsddsfrontend.services.xml

import org.scalatest.{MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ChargeDeduction, CurrencyAmount, Declaration, ItemCustomsValuation, ValuationInformationAndTaxes}

import scala.xml.Elem

class DeclarationXml_ValuationInformationAndTaxesSpec extends WordSpec with MustMatchers {

  "ValuationInformationAndTaxes data" should {
    "be populated in the XML" in {
      val declaration = Declaration(
        valuationInformationAndTaxes = ValuationInformationAndTaxes(
          locationName = Some("Some location name")
        ),
        itemCustomsValuation = Some(ItemCustomsValuation(
          methodCode = Some("1"),
          chargeDeduction = Some(ChargeDeduction("FOO", CurrencyAmount("HUF", "9001")))
        ))
      )

      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "ConditionCode").head.text mustBe "CFR"
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationID").head.text mustBe "GBDVR"
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationName").head.text mustBe "Some location name"
      (xml \ "Declaration" \ "CurrencyExchange" \ "RateNumeric").head.text mustBe "1.27"

      val item = (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (item \ "Commodity" \ "DutyTaxFee" \ "DutyRegimeCode").head.text mustBe "100"
      (item \ "Commodity" \ "DutyTaxFee" \ "Payment" \ "MethodCode").head.text mustBe "E"
      (item \ "ValuationAdjustment" \ "AdditionCode").head.text mustBe "0000"
      (item \ "Commodity" \ "InvoiceLine" \ "ItemChargeAmount").head.text mustBe "100"
      (item \ "Commodity" \ "InvoiceLine" \ "ItemChargeAmount" \ "@currencyID").head.text mustBe "GBP"
      (item \ "CustomsValuation" \ "MethodCode").head.text mustBe "1"
      (item \ "CustomsValuation" \ "ChargeDeduction" \ "OtherChargeDeductionAmount").head.text mustBe "9001"
      (item \ "CustomsValuation" \ "ChargeDeduction" \ "OtherChargeDeductionAmount" \ "@currencyID").head.text mustBe "HUF"
      (item \ "CustomsValuation" \ "ChargeDeduction" \ "ChargesTypeCode").head.text mustBe "FOO"
      (item \ "Commodity" \ "DutyTaxFee" \ "DutyRegimeCode").head.text mustBe "100"
    }

    "omit DutyTaxFee element when dutyRegimeCode and paymentMethodCode are both empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(dutyRegimeCode = None, paymentMethodCode = None))
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee").length mustBe 0
    }

    "omit Payment element when paymentMethodCode is empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(paymentMethodCode = None))
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "Payment").length mustBe 0
    }

    "omit TradeTerm element when conditionCode, locationID and locationName are all empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(conditionCode = None, locationID = None, locationName = None))
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms").length mustBe 0
    }

    "omit ValuationAdjustment element when additionCode is empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(additionCode=None))
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "ValuationAdjustment" \ "AdditionCode").length mustBe 0
    }

    "omit InvoiceLine element when currencyID and itemChargeAmount are both empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(currencyID=None, itemChargeAmount=None))
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "InvoiceLine").length mustBe 0
    }

    "omit CurrencyExchange element when rateNumeric is empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(rateNumeric=None))
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "CurrencyExchange").length mustBe 0
    }

    "omit CustomsValuation element when customsValuationMethodCode and chargeDeduction are empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes())
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "CustomsValuation").length mustBe 0
    }

    "omit ChargeDeduction element when chargeDeduction is not supplied" in {
      val declaration = Declaration()
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "CustomsValuation" \ "ChargeDeduction").length mustBe 0
    }
  }
}
