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

class DeclarationXml_GoodsMeasureSpec extends WordSpec with MustMatchers {

  "GoodsMeasure data" should {
    "be populated in the XML at item level" when {
      "the NetNetWeightMeasure is present in the data" in {
        val declaration = Declaration(
          commodity = Some(Commodity(description = None, classification = Seq.empty, goodsMeasure = Some(GoodsMeasure(netNetWeightMeasure = Some("123"), tariffQuantity = Some("345"), grossMassMeasure = Some("678"))), dutyTaxFee = None))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "GoodsMeasure" \ "NetNetWeightMeasure").head.text mustBe "123"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "GoodsMeasure" \ "NetNetWeightMeasure" \ "@unitCode").head.text mustBe "KGM"
      }


      "the GrossMassMeasure is present in the data" in {
        val declaration = Declaration(
          commodity = Some(Commodity(description = None, classification = Seq.empty, goodsMeasure = Some(GoodsMeasure(netNetWeightMeasure = Some("123"), tariffQuantity = Some("345"), grossMassMeasure = Some("678"))), dutyTaxFee = None))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "GoodsMeasure" \ "GrossMassMeasure").head.text mustBe "678"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "GoodsMeasure" \ "GrossMassMeasure" \ "@unitCode").head.text mustBe "KGM"
      }

      "the TariffQuantity is present in the data" in {
        val declaration = Declaration(
          commodity = Some(Commodity(description = None, classification = Seq.empty, goodsMeasure = Some(GoodsMeasure(netNetWeightMeasure = Some("123"), tariffQuantity = Some("345"), grossMassMeasure = Some("678"))), dutyTaxFee = None))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "GoodsMeasure" \ "TariffQuantity").head.text mustBe "345"
      }
    }

    "be omitted from XML" when {
      "the no GoodsMeasure data exists" in {
        val declaration = Declaration(
          commodity = Some(Commodity(description = None, classification = Seq.empty, goodsMeasure = None, dutyTaxFee = None))
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "GoodsMeasure").length mustBe 0
      }
    }
  }
}

