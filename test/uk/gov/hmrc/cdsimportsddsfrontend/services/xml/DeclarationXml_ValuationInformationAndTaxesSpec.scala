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
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

import scala.xml.Elem

class DeclarationXml_ValuationInformationAndTaxesSpec extends WordSpec with MustMatchers {

  "ValuationInformationAndTaxes data" should {
    "be populated in the XML" in {
      val declaration = Declaration(
        itemCustomsValuation = Some(ItemCustomsValuation(
          methodCode = Some("1"),
          chargeDeduction = Some(ChargeDeduction("FOO", CurrencyAmount("HUF", "9001")))
        )),
        headerCustomsValuation = Some(HeaderCustomsValuation(
          Some(ChargeDeduction("BAR", CurrencyAmount("CHF", "675"))))
        ),
        goodsShipment = GoodsShipment(None, None, None, GovernmentAgencyGoodsItem(Seq(), "untested", Some(ValuationAdjustment("0000"))),
          Some(TradeTerms(conditionCode = Some("CFR"),
            locationID = Some("GBDVR"),
            locationName = Some("Some location name")))
        )
      )

      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "ConditionCode").head.text mustBe "CFR"
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationID").head.text mustBe "GBDVR"
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationName").head.text mustBe "Some location name"
      (xml \ "Declaration" \ "CurrencyExchange" \ "RateNumeric").head.text mustBe "1.27"

      val goodsShipment = xml \ "Declaration" \ "GoodsShipment"
      (goodsShipment \ "CustomsValuation" \ "ChargeDeduction" \ "OtherChargeDeductionAmount").head.text mustBe "675"
      (goodsShipment \ "CustomsValuation" \ "ChargeDeduction" \ "OtherChargeDeductionAmount" \ "@currencyID").head.text mustBe "CHF"
      (goodsShipment \ "CustomsValuation" \ "ChargeDeduction" \ "ChargesTypeCode").head.text mustBe "BAR"

      val item = xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
      (item \ "ValuationAdjustment" \ "AdditionCode").head.text mustBe "0000"
      (item \ "CustomsValuation" \ "MethodCode").head.text mustBe "1"
      (item \ "CustomsValuation" \ "ChargeDeduction" \ "OtherChargeDeductionAmount").head.text mustBe "9001"
      (item \ "CustomsValuation" \ "ChargeDeduction" \ "OtherChargeDeductionAmount" \ "@currencyID").head.text mustBe "HUF"
      (item \ "CustomsValuation" \ "ChargeDeduction" \ "ChargesTypeCode").head.text mustBe "FOO"
    }

    "omit TradeTerm element when conditionCode, locationID and locationName are all empty" in {
      val declaration = Declaration()
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "TradeTerms").length mustBe 0
    }

    "omit ValuationAdjustment element when additionCode is empty" in {
      val declaration = Declaration()
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "ValuationAdjustment" \ "AdditionCode").length mustBe 0
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

    "omit ChargeDeduction element when item chargeDeduction is not supplied" in {
      val declaration = Declaration()
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "CustomsValuation" \ "ChargeDeduction").length mustBe 0
    }

    "omit ChargeDeduction element when header chargeDeduction is not supplied" in {
      val declaration = Declaration()
      val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      (xml \ "Declaration" \ "GoodsShipment" \ "CustomsValuation" \ "ChargeDeduction").length mustBe 0
    }
  }
}
