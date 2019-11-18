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

package uk.gov.hmrc.cdsimportsddsfrontend.services

import org.scalatest.{MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Declaration, ValuationInformationAndTaxes}

import scala.xml.Elem

class DeclarationXml_ValuationInformationAndTaxesSpec extends WordSpec with MustMatchers {

  "ValuationInformationAndTaxes data" should {
    "be populated in the XML" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(locationName = Some("Some location name")))

      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "ConditionCode").head.text mustBe "CFR"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationID").head.text mustBe "GBDVR"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationName").head.text mustBe "Some location name"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "Payment" \ "MethodCode").head.text mustBe "E"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "ValuationAdjustment" \ "AdditionCode").head.text mustBe "0000"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "InvoiceLine" \ "ItemChargeAmount").head.text mustBe "100"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "InvoiceLine" \ "ItemChargeAmount").head.attribute("currencyID").get.head.text mustBe "GBP"
      (xmlElement \ "Declaration" \ "CurrencyExchange" \ "RateNumeric").head.text mustBe "1.27"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "CustomsValuation" \ "MethodCode").head.text mustBe "1"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "DutyRegimeCode").head.text mustBe "100"

    }

    "omit DutyTaxFee element when dutyRegimeCode and paymentMethodCode are both empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(dutyRegimeCode = None, paymentMethodCode = None))
      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee").length mustBe 0

    }

    "omit TradeTerm element when conditionCode, locationID and locationName are all empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(conditionCode = None, locationID = None, locationName = None))
      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms").length mustBe 0
    }

    "omit ValuationAdjustment element when additionCode is empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(additionCode=None))
      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "ValuationAdjustment" \ "AdditionCode").length mustBe 0
    }

    "omit InvoiceLine element when currencyID and itemChargeAmount are both empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(currencyID=None, itemChargeAmount=None))
      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "InvoiceLine").length mustBe 0
    }

    "omit CurrencyExchange element when rateNumeric is empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(rateNumeric=None))
      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "CurrencyExchange").length mustBe 0
    }

    "omit CustomsValuation element when customsValuationMethodCode is empty" in {
      val declaration = Declaration(valuationInformationAndTaxes = ValuationInformationAndTaxes(customsValuationMethodCode=None))
      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "CustomsValuation").length mustBe 0
    }
  }
}
