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

class DeclarationXml_GovernmentAgencyGoodsItemSpec extends WordSpec with MustMatchers {

  "GovernmentAgencyGoodsItem top-level nodes" should {
    "be populated in the XML" when {
      "the item level data is present in the Declaration" in {
        val governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(
          origin = Seq(),
          sequenceNumeric = "3",
          valuationAdjustment = None,
          transactionNatureCode = Some("2"),
          statisticalValue = Some(CurrencyAmount("ZAR", "2020"))
        )

        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = governmentAgencyGoodsItem,
            tradeTerms = None
          )
        )

        val declarationXml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val item = declarationXml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
        (item \ "SequenceNumeric").head.text mustBe "3"
        (item \ "TransactionNatureCode").head.text mustBe "2"
        (item \ "StatisticalValueAmount").head.text mustBe "2020"
        (item \ "StatisticalValueAmount" \ "@currencyID").head.text mustBe "ZAR"
      }
    }

    "be omitted from XML" when {
      "the item level data is not present in the Declaration" in {
        val governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(
          origin = Seq(),
          sequenceNumeric = "3",
          valuationAdjustment = None,
          transactionNatureCode = None,
          statisticalValue = None
        )

        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = governmentAgencyGoodsItem,
            tradeTerms = None
          )
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "TransactionNatureCode").length mustBe 0
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "StatisticalValueAmount").length mustBe 0
      }
    }
  }
}
