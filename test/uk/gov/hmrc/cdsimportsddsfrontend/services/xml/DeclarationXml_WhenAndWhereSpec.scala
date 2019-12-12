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

class DeclarationXml_WhenAndWhereSpec extends WordSpec with MustMatchers {

  "WhenAndWhere data" should {
    "be populated in the XML at header level" when {
      "the destination country code is present in the data" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(consignment = None,
                                        destination = Some(Destination(countryCode = Some("FR"))),
                                        exportCountry = None,
                                        governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(Seq.empty, Seq.empty, "0", None),
                                        tradeTerms = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "Destination" \ "CountryCode").head.text mustBe "FR"
      }

      "the export country is present in the data" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = Some(ExportCountry(id = "NE")),
            governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(Seq.empty, Seq.empty, "0", None),
            tradeTerms = None
          )
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "ExportCountry" \ "ID").head.text mustBe "NE"
      }

    }

    "be populated in the XML at item level" when {
      "the origin is present in the data" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(
              additionalDocuments = Seq.empty,
              origin = Seq(
                Origin(countryCode = Some("DL"), typeCode = Some("99")),
                Origin(countryCode = Some("GB"), typeCode = Some("33"))),
              sequenceNumeric = "1"),
            tradeTerms = None
          )
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Origin" \ "CountryCode").toList.map(_.text) mustBe Seq("DL", "GB")
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Origin" \ "TypeCode").toList.map(_.text) mustBe Seq("99", "33")
      }
    }

    "be omitted from XML" when {
      "the destination country data is missing" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(Seq.empty, Seq.empty, "0", None),
            tradeTerms = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "Destination" ).length mustBe 0
      }

      "the export country data is missing" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(Seq.empty, Seq.empty, "0", None),
            tradeTerms = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "ExportCountry" ).length mustBe 0
      }

      "the origin data is missing" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            consignment = None,
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(additionalDocuments = Seq.empty, origin = Seq.empty, sequenceNumeric = "1"),
            tradeTerms = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Origin" ).length mustBe 0
      }
    }
  }
}
