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
          goodsShipment = GoodsShipment(destination = Some(Destination(countryCode = Some("FR"))),
                                        exportCountry = None,
                                        governmentAgencyGoodsItem = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "Destination" \ "CountryCode").head.text mustBe "FR"
      }

      "the export country is present in the data" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            destination = None,
            exportCountry = Some(ExportCountry(id = Some("NE"))),
            governmentAgencyGoodsItem = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "ExportCountry" \ "ID").head.text mustBe "NE"
      }

      "the origin is present in the data" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = Some(GovernmentAgencyGoodsItem(origin = Some(Origin(countryCode = Some("DL"), typeCode = Some("99")))))
          )
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Origin" \ "CountryCode").head.text mustBe "DL"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Origin" \ "TypeCode").head.text mustBe "99"
      }
    }

    "be omitted from XML" when {
      "the destination country data is missing" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(destination = None, exportCountry = None, governmentAgencyGoodsItem = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "Destination" ).length mustBe 0
      }

      "the export country data is missing" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            destination = None,
            exportCountry = Some(ExportCountry(id = None)),
            governmentAgencyGoodsItem = None)
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "ExportCountry" ).length mustBe 0
      }

      "the origin data is missing" in {
        val declaration = Declaration(
          goodsShipment = GoodsShipment(
            destination = None,
            exportCountry = None,
            governmentAgencyGoodsItem = Some(GovernmentAgencyGoodsItem(origin = None))
          )
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Origin" ).length mustBe 0
      }
    }
  }
}
