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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import play.api.test.FutureAwaits
import play.api.test.Helpers.status
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future
import scala.xml.Elem

class DeclarationControllerSpec extends CdsImportsSpec
  with Scenarios
  with FutureAwaits
  with BeforeAndAfterEach {

  import DeclarationControllerSpec.declarationTypeFormData

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  "A GET Request" should {

    "return 404 when the SinglePageDeclaration feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      new GetScenario() {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      new GetScenario() {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType.declarationType")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalDeclarationType")
        body should include element withName("input").withAttrValue("name", "declarationType.goodsItemNumber")
        body should include element withName("input").withAttrValue("name", "declarationType.totalNumberOfItems")
        body should include element withName("input").withAttrValue("name", "declarationType.requestedProcedureCode")
        body should include element withName("input").withAttrValue("name", "declarationType.previousProcedureCode")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalProcedureCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocCategory")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocType")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocReference")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocGoodsItemId")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalInfoCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalInfoDescription")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[0].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[0].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[0].id")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[0].lpco")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[0].name")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[1].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[1].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[1].id")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[1].lpco")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[1].name")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[2].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[2].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[2].id")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[2].lpco")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[2].name")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[3].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[3].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[3].id")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[3].lpco")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalDocument[3].name")
        body should include element withName("input").withAttrValue("name", "documentationType.localReferenceNumber")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[0].additionalDocPaymentID")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[0].additionalDocPaymentCategory")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[0].additionalDocPaymentType")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[1].additionalDocPaymentID")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[1].additionalDocPaymentCategory")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[1].additionalDocPaymentType")
      }
    }

    "show the expected field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_declarationType").withValue("1.1 Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_additionalDeclarationType").withValue("1.2 Additional Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_goodsItemNumber").withValue("1.6 Goods Item Number")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_totalNumberOfItems").withValue("1.9 Total Number Of Items")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_requestedProcedureCode").withValue("1.10 Requested Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_previousProcedureCode").withValue("1.10 Previous Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_additionalProcedureCode").withValue("1.11 Additional Procedure Code (000 or C07)")
        // TODO add new fields
      }
    }

    "show the expected pre-populated field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType.declarationType").withAttrValue("value", "IM")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalDeclarationType").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "declarationType.goodsItemNumber").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "declarationType.totalNumberOfItems").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "declarationType.requestedProcedureCode").withAttrValue("value", "40")
        body should include element withName("input").withAttrValue("name", "declarationType.previousProcedureCode").withAttrValue("value", "00")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalProcedureCode").withAttrValue("value", "000")
        // TODO add new fields
        // TODO determine which fields should really be pre-populated, and modify tests accordingly
      }
    }
  }

  "in section 3" should {
    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "parties.exporter.name")
        body should include element withName("input").withAttrValue("name", "parties.exporter.identifier")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.city")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.postcode")
        //      | 3.15 Importer - Name                   | Foil Solutions          |
        //      | 3.15 Importer - Street and Number      | Aluminium Way           |
        //      | 3.15 Importer - City                   | Metalton                |
        //      | 3.15 Importer - CountryCode            | UK                      |
        //      | 3.15 Importer - Postcode               | ME7 4LL                 |
        //      | 3.16 Importer - EORI                   | GB87654321              |
        //      | 3.18 Declarant - EORI                  | GB15263748              |
        //      | 3.24 Seller - Name                     | Tinfoil Sans Frontieres |
        //      | 3.24 Seller - Street and Number        | 123 les Champs Insulees |
        //      | 3.24 Seller - City                     | Troyes                  |
        //      | 3.24 Seller - CountryCode              | FR                      |
        //      | 3.24 Seller - Postcode                 | 01414                   |
        //      | 3.24 Seller - Phone number             | 003344556677            |
        //      | 3.25 Seller - EORI                     | FR84736251              |
        //      | 3.26 Buyer - Name                      | Tinfoil R Us            |
        //      | 3.26 Buyer - Street and Number         | 12 Alcan Boulevard      |
        //      | 3.26 Buyer - City                      | Sheffield               |
        //      | 3.26 Buyer - CountryCode               | UK                      |
        //      | 3.26 Buyer - Postcode                  | S1 1VA                  |
        //      | 3.26 Buyer - Phone number              | 00441234567890          |
        //      | 3.27 Buyer - EORI                      | GB45362718              |
        //      | 3.39 Authorisation holder - identifier | GB62518473              |
        //      | 3.39 Authorisation holder - type code  | OK4U                    |
        //      | 3.40 VAT Number (or TSPVAT)            | 99887766                |
        //      | 3.40 Role Code                         | VAT                     |
      }
    }

    "show the expected field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "parties_exporter_name").withValue("3.1 Exporter - Name")
        body should include element withClass("govuk-label").withAttrValue("for", "parties_exporter_identifier").withValue("3.1 Exporter - EORI")
        body should include element withClass("govuk-label").withAttrValue("for", "parties_exporter_address_streetAndNumber").withValue("3.1 Exporter - Street and Number")
        body should include element withClass("govuk-label").withAttrValue("for", "parties_exporter_address_city").withValue("3.1 Exporter - City")
        body should include element withClass("govuk-label").withAttrValue("for", "parties_exporter_address_countryCode").withValue("3.1 Exporter - Country Code")
        body should include element withClass("govuk-label").withAttrValue("for", "parties_exporter_address_postcode").withValue("3.1 Exporter - Postcode")
      }
    }
  }

  "A POST Request" should {
    "return 404 when the SinglePageDeclaration feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      val formData = Map[String, Seq[String]]()
      when(mockDeclarationService.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      val formData = Map[String, Seq[String]]()
      when(mockDeclarationService.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "succeed when all required fields are present" in signedInScenario { user =>

      val documentationFormData = Map(
        "documentationType.previousDocCategory" -> Seq("previousDocCategory"),
        "documentationType.previousDocType" -> Seq("previousDocType"),
        "documentationType.previousDocReference" -> Seq("previousDocReference"),
        "documentationType.previousDocGoodsItemId" -> Seq("previousDocGoodsItemId"),
        "documentationType.additionalInfoCode" -> Seq("additionalInfoCode"),
        "documentationType.additionalInfoDescription" -> Seq("additionalInfoDescription"),

        "documentationType.additionalDocument[0].categoryCode" -> Seq("categoryCode"),
        "documentationType.additionalDocument[0].typeCode" -> Seq("typeCode"),
        "documentationType.additionalDocument[0].id" -> Seq("id"),
        "documentationType.additionalDocument[0].lpco" -> Seq("lpco"),
        "documentationType.additionalDocument[0].name" -> Seq("name"),

        "documentationType.additionalDocument[1].categoryCode" -> Seq("categoryCode"),
        "documentationType.additionalDocument[1].typeCode" -> Seq("typeCode"),
        "documentationType.additionalDocument[1].id" -> Seq("id"),
        "documentationType.additionalDocument[1].lpco" -> Seq("lpco"),
        "documentationType.additionalDocument[1].name" -> Seq("name"),

        "documentationType.additionalDocument[2].categoryCode" -> Seq("categoryCode"),
        "documentationType.additionalDocument[2].typeCode" -> Seq("typeCode"),
        "documentationType.additionalDocument[2].id" -> Seq("id"),
        "documentationType.additionalDocument[2].lpco" -> Seq("lpco"),
        "documentationType.additionalDocument[2].name" -> Seq("name"),

        "documentationType.additionalDocument[3].categoryCode" -> Seq("categoryCode"),
        "documentationType.additionalDocument[3].typeCode" -> Seq("typeCode"),
        "documentationType.additionalDocument[3].id" -> Seq("id"),
        "documentationType.additionalDocument[3].lpco" -> Seq("lpco"),
        "documentationType.additionalDocument[3].name" -> Seq("name"),
        "documentationType.localReferenceNumber" -> Seq("localRef"),

        "documentationType.additionalPayment[0].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[0].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[0].additionalDocPaymentType" -> Seq("DAN"),

        "documentationType.additionalPayment[1].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[1].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[1].additionalDocPaymentType" -> Seq("DAN"),

        "documentationType.additionalPayment[2].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[2].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[2].additionalDocPaymentType" -> Seq("DAN"),

        "documentationType.additionalPayment[3].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[3].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[3].additionalDocPaymentType" -> Seq("DAN")
      )

      val formData = declarationTypeFormData ++ documentationFormData
      when(mockDeclarationService.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK
        body should include element withName("dd").withValue("Good")
      }
    }

    "fail when some mandatory fields are missing" in signedInScenario { user =>
      val formData = Map("declarationType.declarationType" -> Seq("declarationType"))
      when(mockDeclarationService.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withAttrValue("id", "declarationType.additionalDeclarationType-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.goodsItemNumber-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.totalNumberOfItems-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.requestedProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.previousProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.additionalProcedureCode-error").withValue("This field is required")

      }
    }
  }

  "The POST-ed xml" should {
    "work" in signedInScenario { user =>
      val formData = Map(
        "declarationType.declarationType" -> Seq("101"),
        "declarationType.additionalDeclarationType" -> Seq("102"),
        "declarationType.goodsItemNumber" -> Seq("103"),
        "declarationType.totalNumberOfItems" -> Seq("104"),
        "declarationType.requestedProcedureCode" -> Seq("105"),
        "declarationType.previousProcedureCode" -> Seq("106"),
        "declarationType.additionalProcedureCode" -> Seq("107"),
        "documentationType.previousDocCategory" -> Seq("Y"),
        "documentationType.previousDocType" -> Seq("DCR"),
        "documentationType.previousDocReference" -> Seq("9GB201909014000"),
        "documentationType.previousDocGoodsItemId" -> Seq("1"),
        "documentationType.additionalInfoCode" -> Seq("00500"),
        "documentationType.additionalInfoDescription" -> Seq("IMPORTER"),
        "documentationType.additionalDocument[0].categoryCode" -> Seq("N"),
        "documentationType.additionalDocument[0].typeCode" -> Seq("935"),
        "documentationType.additionalDocument[0].id" -> Seq("12345/30.09.2019"),
        "documentationType.additionalDocument[0].lpco" -> Seq("AC"),
        "documentationType.additionalDocument[0].name" -> Seq("DocumentName"),
        "documentationType.additionalDocument[1].categoryCode" -> Seq("N"),
        "documentationType.additionalDocument[1].typeCode" -> Seq("935"),
        "documentationType.additionalDocument[1].id" -> Seq("12345/30.09.2019"),
        "documentationType.additionalDocument[1].lpco" -> Seq("AC"),
        "documentationType.additionalDocument[1].name" -> Seq("DocumentName"),
        "documentationType.additionalDocument[2].categoryCode" -> Seq("N"),
        "documentationType.additionalDocument[2].typeCode" -> Seq("935"),
        "documentationType.additionalDocument[2].id" -> Seq("12345/30.09.2019"),
        "documentationType.additionalDocument[2].lpco" -> Seq("AC"),
        "documentationType.additionalDocument[2].name" -> Seq("DocumentName"),
        "documentationType.additionalDocument[3].categoryCode" -> Seq("N"),
        "documentationType.additionalDocument[3].typeCode" -> Seq("935"),
        "documentationType.additionalDocument[3].id" -> Seq("12345/30.09.2019"),
        "documentationType.additionalDocument[3].lpco" -> Seq("AC"),
        "documentationType.additionalDocument[3].name" -> Seq("DocumentName"),
        "documentationType.localReferenceNumber" -> Seq("localRef"),
        "documentationType.additionalPayment[0].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[0].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[0].additionalDocPaymentType" -> Seq("DAN"),
        "documentationType.additionalPayment[1].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[1].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[1].additionalDocPaymentType" -> Seq("DAN"),
        "documentationType.additionalPayment[2].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[2].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[2].additionalDocPaymentType" -> Seq("DAN"),
        "documentationType.additionalPayment[3].additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalPayment[3].additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalPayment[3].additionalDocPaymentType" -> Seq("DAN"),
        "parties.exporter.name" -> Seq("exporter dude"),
        "parties.exporter.identifier" -> Seq("GB1020304050"),
        "parties.exporter.address.streetAndNumber" -> Seq("123 Shipping Lane"),
        "parties.exporter.address.city" -> Seq("Metalville"),
        "parties.exporter.address.countryCode" -> Seq("Tattooine"),
        "parties.exporter.address.postcode" -> Seq("S7 4RS")
      )
      val captor: ArgumentCaptor[Elem] = ArgumentCaptor.forClass(classOf[Elem])
      when(mockDeclarationService.submit(any(), captor.capture())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK
        val xml: Elem = captor.getValue
        // TODO these tests should be around DeclarationXML.fromImportDeclaration
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) contains  "Y"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "PreviousDocument" \ "TypeCode").toList.map(_.text) contains "DCR"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "PreviousDocument" \ "ID").toList.map(_.text) contains "9GB201909014000"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "PreviousDocument" \ "LineNumeric").toList.map(_.text) contains "1"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalInformation" \ "StatementCode").text mustBe "00500"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalInformation" \ "StatementDescription").text mustBe "IMPORTER"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalDocument" \ "CategoryCode").toList.map(_.text) contains "N"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalDocument" \ "TypeCode").toList.map(_.text) contains "935"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalDocument" \ "ID").toList.map(_.text) contains "12345/30.09.2019"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalDocument" \ "LPCOExemptionCode").toList.map(_.text) contains "AC"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalDocument" \ "Name").toList.map(_.text) must contain("DocumentName")
        (xml \ "Declaration" \ "FunctionalReferenceID").head.text mustBe "localRef"
        (xml \ "Declaration" \ "AdditionalDocument" \ "ID").head.text mustBe "123456"
        (xml \ "Declaration" \ "AdditionalDocument" \ "CategoryCode").head.text mustBe "1"
        (xml \ "Declaration" \ "AdditionalDocument" \ "TypeCode").head.text mustBe "DAN"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Consignor" \ "Name").text mustBe "exporter dude"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Consignor" \ "ID").text mustBe "GB1020304050"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Consignor" \ "Address" \ "Line").text mustBe "123 Shipping Lane"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Consignor" \ "Address" \ "CityName").text mustBe "Metalville"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Consignor" \ "Address" \ "CountryCode").text mustBe "Tattooine"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "Consignor" \ "Address" \ "PostcodeID").text mustBe "S7 4RS"
      }
    }
  }
}

object DeclarationControllerSpec {
  val declarationTypeFormData: Map[String, Seq[String]] = Map(
    "declarationType.declarationType" -> Seq("declarationType"),
    "declarationType.additionalDeclarationType" -> Seq("additionalDeclarationType"),
    "declarationType.goodsItemNumber" -> Seq("goodsItemNumber"),
    "declarationType.totalNumberOfItems" -> Seq("totalNumberOfItems"),
    "declarationType.requestedProcedureCode" -> Seq("requestedProcedureCode"),
    "declarationType.previousProcedureCode" -> Seq("previousProcedureCode"),
    "declarationType.additionalProcedureCode" -> Seq("additionalProcedureCode")
  )

}