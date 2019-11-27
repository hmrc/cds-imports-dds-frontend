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

class DeclarationXml_Packaging extends WordSpec with MustMatchers {

  "Packaging data" should {
    "be populated in the XML at item level" when {
      "the MarksNumbersID is present in the data" in {
        val declaration = Declaration(
          packaging = Some(Packaging(marksNumberId = Some("MK123")))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Packaging" \ "MarksNumbersID").head.text mustBe "MK123"
      }

      "the QuantityQuantity is present in the data" in {
        val declaration = Declaration(
          packaging = Some(Packaging(quantityQuantity = Some("44")))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Packaging" \ "QuantityQuantity").head.text mustBe "44"
      }

      "the TypeCode is present in the data" in {
        val declaration = Declaration(
          packaging = Some(Packaging(typeCode = Some("TC1")))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Packaging" \ "TypeCode").head.text mustBe "TC1"
      }

    }

    "be omitted from XML" when {
      "the no packaging data exists" in {
        val declaration = Declaration(packaging = None)

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Packaging" ).length mustBe 0

      }

    }
  }
}
