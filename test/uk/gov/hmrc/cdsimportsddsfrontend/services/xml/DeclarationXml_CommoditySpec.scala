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

class DeclarationXml_CommoditySpec extends WordSpec with MustMatchers {

  "Commodity data" should {
    "be populated in the XML at item level" when {
      "the commodity data is present in declaration" in {
        val declaration = Declaration(
          commodity = Some(Commodity(description = Some("Tin foil"),
            classification = Seq(Classification(Some("id1"), Some("identificationTypeCode1")),
              Classification(Some("id2"), Some("identificationTypeCode2"))),
            goodsMeasure = Some(GoodsMeasure(netNetWeightMeasure = Some("123"), tariffQuantity = Some("345"), grossMassMeasure = Some("678"))),
            dutyTaxFee = Some(DutyTaxFee(dutyRegimeCode = Some("100"), payment = Some(Payment("E"))))))
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)

        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Description").head.text mustBe "Tin foil"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Classification" \ "ID").head.text mustBe "id1"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Classification" \ "ID").tail.text mustBe "id2"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"\ "Commodity" \ "Classification" \ "IdentificationTypeCode").head.text mustBe "identificationTypeCode1"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"\ "Commodity" \ "Classification" \ "IdentificationTypeCode").tail.text mustBe "identificationTypeCode2"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"\ "Commodity" \ "DutyTaxFee" \ "DutyRegimeCode").head.text mustBe "100"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"\ "Commodity" \ "DutyTaxFee" \ "Payment" \ "MethodCode").head.text mustBe "E"
      }
    }

    "be omitted from XML" when {
      "the no description of goods data exists" in {
        val declaration = Declaration(commodity = Some(Commodity(None, Seq.empty, None, None)))

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Description").length mustBe 0
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Classification" \ "ID").length mustBe 0
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "Classification" \ "IdentificationTypeCode").length mustBe 0
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "DutyRegimeCode").length mustBe 0
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Commodity" \ "DutyTaxFee" \ "Payment" \ "MethodCode").length mustBe 0
      }
    }
  }
}
