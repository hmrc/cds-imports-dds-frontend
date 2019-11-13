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

import org.scalatest.{MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Declaration, ValuationInformationAndTaxes}

import scala.xml.{Elem, Node, NodeSeq}

class DeclarationXml_DocumentationSpec extends WordSpec with MustMatchers {

  "Documentation data" should {
    "be populated in the XML" in {
      val declaration = Declaration()

      val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) must contain("Y")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) must contain("DCR")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) must contain("9GB201909014000")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) must contain("1")

      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "CategoryCode").toList.map(_.text) mustBe List("N", "C", "C", "I")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "TypeCode").toList.map(_.text) mustBe List("935", "514", "506", "004")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "ID").toList.map(_.text) mustBe List("12345/30.09.2019", "GBEIR201909014000", "GBDPO1909241", "GBCPI000001-0001")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "LPCOExemptionCode").toList.map(_.text) mustBe List("AC", "AE")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "Name").toList.map(_.text) mustBe List("DocumentName1")

      (xmlElement \ "Declaration" \ "FunctionalReferenceID").head.text mustBe "Test1234"
      (xmlElement \ "Declaration" \ "AdditionalDocument" \ "ID").head.text mustBe "1909241"
      (xmlElement \ "Declaration" \ "AdditionalDocument" \ "CategoryCode").head.text mustBe "1"
      (xmlElement \ "Declaration" \ "AdditionalDocument" \ "TypeCode").head.text mustBe "DAN"

      (xmlElement \ "Declaration" \ "AdditionalInformation" \ "StatementCode").text mustBe "TSP01"
      (xmlElement \ "Declaration" \ "AdditionalInformation" \ "StatementDescription").text mustBe "TSP"
    }
  }

}
