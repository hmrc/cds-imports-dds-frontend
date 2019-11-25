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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{AdditionalInformation, Declaration, DocumentationType, PreviousDocument}

import scala.xml.Elem

class DeclarationXml_DocumentationSpec extends WordSpec with MustMatchers {

  "Documentation data" should {
    "be populated in the XML" in {
      val declaration = Declaration()

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"
      val governmentAgencyGoodsItem = goodsShipment \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("Y", "Y", "Z", "Z", "Z", "Z")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("CLE", "DCR", "ZZZ", "235", "ZZZ", "270")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("20191101", "9GB201909014000", "20191103", "9GB201909014002", "9GB201909014003", "9GB201909014004")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("1", "1", "1", "1", "1", "1")

      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "CategoryCode").toList.map(_.text) mustBe List("N", "C", "C", "N", "N", "N")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "TypeCode").toList.map(_.text) mustBe List("935", "514", "506", "935", "935", "935")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "ID").toList.map(_.text) mustBe List("12345/30.09.2019", "GBEIR201909014000", "GBDPO1909241", "12345/30.07.2019", "12345/30.08.2019", "12345/30.09.2019")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "LPCOExemptionCode").toList.map(_.text) mustBe List("AC", "AC", "AC", "AC")
      (governmentAgencyGoodsItem \ "AdditionalDocument" \ "Name").toList.map(_.text) mustBe List("DocumentName1")

      (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("Y", "Y", "Y", "Y")
      (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("CLE", "DCR", "CLE", "DCR")
      (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("20191101", "9GB201909014000", "20191101", "9GB201909014000")
      (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("1", "1", "1", "1")

      (xmlElement \ "Declaration" \ "FunctionalReferenceID").head.text mustBe declaration.documentationType.localReferenceNumber.getOrElse("#unexpected")
      (xmlElement \ "Declaration" \ "AdditionalDocument" \ "ID").head.text mustBe "1909241"
      (xmlElement \ "Declaration" \ "AdditionalDocument" \ "CategoryCode").head.text mustBe "1"
      (xmlElement \ "Declaration" \ "AdditionalDocument" \ "TypeCode").head.text mustBe "DAN"

      (xmlElement \ "Declaration" \ "AdditionalInformation" \ "StatementCode").text mustBe "TSP01"
      (xmlElement \ "Declaration" \ "AdditionalInformation" \ "StatementDescription").text mustBe "TSP"
    }

    "omit item-level additional information if empty" in {
      val declaration = Declaration()

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (governmentAgencyGoodsItem \ "AdditionalInformation").toList mustBe Nil
    }

    "render a single item level additional information element" in {
      val additionalInformation = AdditionalInformation(Some("foo"), Some("bar"))
      val documentation = DocumentationType().copy(itemAdditionalInformation = Seq(additionalInformation))
      val declaration = Declaration().copy(documentationType = documentation)

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (governmentAgencyGoodsItem \ "AdditionalInformation" \ "StatementCode").toList.map(_.text) mustBe List("foo")
      (governmentAgencyGoodsItem \ "AdditionalInformation" \ "StatementDescription").toList.map(_.text) mustBe List("bar")
    }

    "render six item level additional information elements" in {
      val additionalInformation1 = AdditionalInformation(Some("foo1"), Some("bar1"))
      val additionalInformation2 = AdditionalInformation(Some("foo2"), Some("bar2"))
      val additionalInformation3 = AdditionalInformation(Some("foo3"), Some("bar3"))
      val additionalInformation4 = AdditionalInformation(Some("foo4"), Some("bar4"))
      val additionalInformation5 = AdditionalInformation(Some("foo5"), Some("bar5"))
      val additionalInformation6 = AdditionalInformation(Some("foo6"), Some("bar6"))
      val tooMuchInformation = Seq(additionalInformation1, additionalInformation2, additionalInformation3, additionalInformation4, additionalInformation5, additionalInformation6)
      val documentation = DocumentationType().copy(itemAdditionalInformation = tooMuchInformation)
      val declaration = Declaration().copy(documentationType = documentation)

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (governmentAgencyGoodsItem \ "AdditionalInformation" \ "StatementCode").toList.map(_.text) mustBe List("foo1", "foo2", "foo3", "foo4", "foo5", "foo6")
      (governmentAgencyGoodsItem \ "AdditionalInformation" \ "StatementDescription").toList.map(_.text) mustBe List("bar1", "bar2", "bar3", "bar4", "bar5", "bar6")
    }

    "omit statement code if empty" in {
      val additionalInformation1 = AdditionalInformation(Some("foo1"), Some("bar1"))
      val additionalInformation2 = AdditionalInformation(None, Some("bar2"))
      val additionalInformation3 = AdditionalInformation(Some("foo3"), Some("bar3"))
      val partialInformation = Seq(additionalInformation1, additionalInformation2, additionalInformation3)
      val documentation = DocumentationType().copy(itemAdditionalInformation = partialInformation)
      val declaration = Declaration().copy(documentationType = documentation)

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (governmentAgencyGoodsItem \ "AdditionalInformation" \ "StatementCode").toList.map(_.text) mustBe List("foo1", "foo3")
    }

    "omit statement description if empty" in {
      val additionalInformation1 = AdditionalInformation(Some("foo1"), Some("bar1"))
      val additionalInformation2 = AdditionalInformation(Some("foo2"), None)
      val additionalInformation3 = AdditionalInformation(Some("foo3"), Some("bar3"))
      val partialInformation = Seq(additionalInformation1, additionalInformation2, additionalInformation3)
      val documentation = DocumentationType().copy(itemAdditionalInformation = partialInformation)
      val declaration = Declaration().copy(documentationType = documentation)

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (governmentAgencyGoodsItem \ "AdditionalInformation" \ "StatementDescription").toList.map(_.text) mustBe List("bar1", "bar3")
    }

    "omit item level additional information if both fields are empty" in {
      val additionalInformation1 = AdditionalInformation(Some("foo1"), Some("bar1"))
      val additionalInformation2 = AdditionalInformation(None, None)
      val additionalInformation3 = AdditionalInformation(Some("foo3"), Some("bar3"))
      val partialInformation = Seq(additionalInformation1, additionalInformation2, additionalInformation3)
      val documentation = DocumentationType().copy(itemAdditionalInformation = partialInformation)
      val declaration = Declaration().copy(documentationType = documentation)

      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem")
      (governmentAgencyGoodsItem \ "AdditionalInformation").toList.length mustBe 2
    }
  }

  "Item level Previous documents data" should {
    "omit item level previous document nodes if previous document fields are empty" in {
      val documentationEmpty = DocumentationType().copy(itemPreviousDocument = Seq.empty)
      val declaration = Declaration().copy(documentationType = documentationEmpty)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument").toList.length mustBe 0
    }

    "omit item level previous document nodes if previous document fields are None or empty strings" in {
      val previousDocumentWithNonesAndEmptyStrings =
        PreviousDocument(categoryCode = None, id = Some(""), typeCode = Some(""), lineNumeric = None)

      val documentationEmpty = DocumentationType().copy(itemPreviousDocument =
        Seq(previousDocumentWithNonesAndEmptyStrings))
      val declaration = Declaration().copy(documentationType = documentationEmpty)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument").toList.length mustBe 0
    }

    "render a single item level previous document element with all child nodes present if all fields are non-empty" in {
      val previousDocument =
        PreviousDocument(categoryCode = Some("foo"), id = Some("bar"), typeCode = Some("foo1"), lineNumeric = Some("bar1"))
      val documentation = DocumentationType().copy(itemPreviousDocument = Seq(previousDocument))
      val declaration = Declaration().copy(documentationType = documentation)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
    }

    "render a previous document node without CategoryCode element if the field is empty" in {
      val previousDocument =
        PreviousDocument(categoryCode = None, id = Some("bar"), typeCode = Some("foo1"), lineNumeric = Some("bar1"))
      val documentation = DocumentationType().copy(itemPreviousDocument = Seq(previousDocument))
      val declaration = Declaration().copy(documentationType = documentation)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List.empty
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
    }

    "render a previous document node without ID element if the field is empty" in {
      val previousDocument =
        PreviousDocument(categoryCode = Some("foo"), id = None, typeCode = Some("foo1"), lineNumeric = Some("bar1"))
      val documentation = DocumentationType().copy(itemPreviousDocument = Seq(previousDocument))
      val declaration = Declaration().copy(documentationType = documentation)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List.empty
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
    }

    "render a previous document node without TypeCode element if the field is empty" in {
      val previousDocument =
        PreviousDocument(categoryCode = Some("foo"), id = Some("bar"), typeCode = None, lineNumeric = Some("bar1"))
      val documentation = DocumentationType().copy(itemPreviousDocument = Seq(previousDocument))
      val declaration = Declaration().copy(documentationType = documentation)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List.empty
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
    }

    "render a previous document node without LineNumeric element if the field is empty" in {
      val previousDocument =
        PreviousDocument(categoryCode = Some("foo"), id = Some("bar"), typeCode = Some("foo1"), lineNumeric = None)
      val documentation = DocumentationType().copy(itemPreviousDocument = Seq(previousDocument))
      val declaration = Declaration().copy(documentationType = documentation)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List.empty
    }

    "render 6 item level previous document elements" in {
      val previousDocuments = (1 to 6).map(i => PreviousDocument(
        Some(s"category_$i"),Some(s"id_$i"), Some(s"typeCode_$i"), Some(s"lineNumeric_$i"))).toSeq
      val documentation = DocumentationType().copy(itemPreviousDocument = previousDocuments)
      val declaration = Declaration().copy(documentationType = documentation)
      val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
      val governmentAgencyGoodsItem = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe
        List("category_1", "category_2", "category_3", "category_4", "category_5", "category_6")

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe
        List("id_1", "id_2", "id_3", "id_4", "id_5", "id_6")

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe
        List("typeCode_1", "typeCode_2", "typeCode_3", "typeCode_4", "typeCode_5", "typeCode_6")

      (governmentAgencyGoodsItem \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe
        List("lineNumeric_1", "lineNumeric_2", "lineNumeric_3", "lineNumeric_4", "lineNumeric_5", "lineNumeric_6")
    }

    "Header level Previous documents data" should {
      "not be populated in the XML when previous document is empty" in {

        val documentationEmpty = DocumentationType().copy(headerPreviousDocument = Seq.empty)
        val declaration = Declaration().copy(documentationType = documentationEmpty)

        val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument").toList.length mustBe 0
      }

      "omit header level previous document nodes if previous document fields are empty" in {
        val documentationEmpty = DocumentationType().copy(headerPreviousDocument = Seq.empty)
        val declaration = Declaration().copy(documentationType = documentationEmpty)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument").toList.length mustBe 0
      }

      "omit header level previous document nodes if previous document fields are None or empty strings" in {
        val previousDocumentWithNonesAndEmptyStrings =
          PreviousDocument(categoryCode = None, id = Some(""), typeCode = Some(""), lineNumeric = None)

        val documentationEmpty = DocumentationType().copy(headerPreviousDocument =
          Seq(previousDocumentWithNonesAndEmptyStrings))
        val declaration = Declaration().copy(documentationType = documentationEmpty)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument").toList.length mustBe 0
      }

      "render a single header level previous document element with all child nodes present if all fields are non-empty" in {
        val previousDocument =
          PreviousDocument(categoryCode = Some("foo"), id = Some("bar"), typeCode = Some("foo1"), lineNumeric = Some("bar1"))
        val documentation = DocumentationType().copy(headerPreviousDocument = Seq(previousDocument))
        val declaration = Declaration().copy(documentationType = documentation)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
        (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
        (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
        (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
      }

      "render a previous document header node without CategoryCode element if the field is empty" in {
        val previousDocument =
          PreviousDocument(categoryCode = None, id = Some("bar"), typeCode = Some("foo1"), lineNumeric = Some("bar1"))
        val documentation = DocumentationType().copy(headerPreviousDocument = Seq(previousDocument))
        val declaration = Declaration().copy(documentationType = documentation)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)

        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"
        (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List.empty
        (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
        (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
        (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
      }

      "render a previous document header node without ID element if the field is empty" in {
        val previousDocument =
          PreviousDocument(categoryCode = Some("foo"), id = None, typeCode = Some("foo1"), lineNumeric = Some("bar1"))
        val documentation = DocumentationType().copy(headerPreviousDocument = Seq(previousDocument))
        val declaration = Declaration().copy(documentationType = documentation)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
        (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List.empty
        (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
        (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
      }

      "render a previous document header node without TypeCode element if the field is empty" in {
        val previousDocument =
          PreviousDocument(categoryCode = Some("foo"), id = Some("bar"), typeCode = None, lineNumeric = Some("bar1"))
        val documentation = DocumentationType().copy(headerPreviousDocument = Seq(previousDocument))
        val declaration = Declaration().copy(documentationType = documentation)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"
        (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
        (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
        (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List.empty
        (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List("bar1")
      }

      "render a previous document header node without LineNumeric element if the field is empty" in {
        val previousDocument =
          PreviousDocument(categoryCode = Some("foo"), id = Some("bar"), typeCode = Some("foo1"), lineNumeric = None)
        val documentation = DocumentationType().copy(headerPreviousDocument = Seq(previousDocument))
        val declaration = Declaration().copy(documentationType = documentation)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe List("foo")
        (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe List("bar")
        (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe List("foo1")
        (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe List.empty
      }

      "render 4 header level previous document elements" in {
        val previousDocuments = (1 to 4).map(i => PreviousDocument(
          Some(s"category_$i"),Some(s"id_$i"), Some(s"typeCode_$i"), Some(s"lineNumeric_$i"))).toSeq
        val documentation = DocumentationType().copy(headerPreviousDocument = previousDocuments)
        val declaration = Declaration().copy(documentationType = documentation)
        val xmlElement: Elem = DeclarationXml().fromImportDeclaration(declaration)
        val goodsShipment = xmlElement \ "Declaration" \ "GoodsShipment"

        (goodsShipment \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) mustBe
          List("category_1", "category_2", "category_3", "category_4")
        (goodsShipment \ "PreviousDocument" \ "ID").toList.map(_.text) mustBe
          List("id_1", "id_2", "id_3", "id_4")
        (goodsShipment \ "PreviousDocument" \ "TypeCode").toList.map(_.text) mustBe
          List("typeCode_1", "typeCode_2", "typeCode_3", "typeCode_4")
        (goodsShipment \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) mustBe
          List("lineNumeric_1", "lineNumeric_2", "lineNumeric_3", "lineNumeric_4")
      }
    }
  }

}
