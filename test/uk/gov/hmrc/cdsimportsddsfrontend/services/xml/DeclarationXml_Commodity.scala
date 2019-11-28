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

class DeclarationXml_Commodity extends WordSpec with MustMatchers {

  "Description data" should {
    "be populated in the XML at item level" when {
      "the description of goods is present in the data" in {
        val declaration = Declaration(
          commodity = Some(Commodity(description = Some("Tin foil")))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Description").head.text mustBe "Tin foil"
      }
    }

    "be omitted from XML" when {
      "the no description of goods data exists" in {
        val declaration = Declaration(commodity = Some(Commodity(description = None)))

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Description").length mustBe 0

      }

    }
  }
}
