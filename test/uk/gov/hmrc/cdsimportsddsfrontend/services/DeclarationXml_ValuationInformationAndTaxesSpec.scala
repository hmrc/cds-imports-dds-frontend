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

import com.gu.scalatest.JsoupShouldMatchers
import org.scalatest.MustMatchers
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Declaration
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}

import scala.xml.{Elem, Node, NodeSeq, UnprefixedAttribute}

class DeclarationXml_ValuationInformationAndTaxesSpec extends CdsImportsSpec with MustMatchers with AuthenticationBehaviours with JsoupShouldMatchers {

  "ValuationInformationAndTaxes data" should {
    "be populated in the XML" in {
      val declaration = Declaration()

      val xmlElement: Elem = DeclarationXml.fromImportDeclaration("EORI", declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "ConditionCode").head.text mustBe "CFR"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationID").head.text mustBe "GBDVR"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "LocationName").head.text mustBe "Great Britain Dover"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "Payment" \ "MethodCode").head.text mustBe "E"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "ValuationAdjustment" \ "AdditionCode").head.text mustBe "0000"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "InvoiceLine" \ "ItemChargeAmount").head.text mustBe "100"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "InvoiceLine" \ "ItemChargeAmount").head.attribute("currencyID").get.head.text mustBe "GBP"
      (xmlElement \ "Declaration" \ "CurrencyExchange" \ "RateNumeric").head.text mustBe "1.27"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "CustomsValuation" \ "MethodCode").head.text mustBe "1"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "DutyRegimeCode").head.text mustBe "100"

    }
  }
}
