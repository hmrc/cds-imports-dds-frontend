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

import org.scalatest.{AppendedClues, MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

import scala.xml.Elem

class DeclarationXml_ConsignmentSpec extends WordSpec with MustMatchers with AppendedClues {

  "Consignment data" should {
    "be populated in the XML" when {
      "the consignment is present in the declaration data" in {
        val someConsignment = Some(Consignment(
          containerCode = Some("0"),
          arrivalTransportMeans = Some(ArrivalTransportMeans(Some("10"), Some("1023465738"))),
          goodsLocation = Some(GoodsLocation(Some("FXTFXTFXT"), Some("A"), Some(Address(None, None, Some("GB"), None, None)))),
          loadingLocation = Some(LoadingLocation("GAT"))
        ))

        val declaration = Declaration(goodsShipment = GoodsShipment(
          consignment = someConsignment,
          destination = None,
          exportCountry = None,
          governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(sequenceNumeric = "0", origin = Seq()),
          tradeTerms = None))

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "ContainerCode").head.text mustBe "0"
        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "ArrivalTransportMeans" \ "ID").head.text mustBe "1023465738"
        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "ArrivalTransportMeans" \ "IdentificationTypeCode").head.text mustBe "10"
        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "GoodsLocation" \ "Name").head.text mustBe "FXTFXTFXT"
        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "GoodsLocation" \ "TypeCode").head.text mustBe "A"
        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "GoodsLocation" \ "Address").length mustBe 1 withClue "Expected a GoodsLocation.Address"
        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment" \ "LoadingLocation" \ "ID").head.text mustBe "GAT"
      }
    }

    "be omitted from XML" when {
      "the consignment is not  present in the declaration data" in {
        val declaration = Declaration(goodsShipment = GoodsShipment(
          consignment = None,
          destination = None,
          exportCountry = None,
          governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(sequenceNumeric = "0", origin = Seq()),
          tradeTerms = None))
        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

        (xml \ "Declaration" \ "GoodsShipment" \ "Consignment").length mustBe 0
      }
    }
  }
}
