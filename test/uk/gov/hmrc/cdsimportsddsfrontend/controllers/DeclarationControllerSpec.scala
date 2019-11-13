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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Declaration
import uk.gov.hmrc.cdsimportsddsfrontend.services.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future

class DeclarationControllerSpec extends CdsImportsSpec
  with Scenarios
  with FutureAwaits
  with BeforeAndAfterEach {

  import DeclarationControllerSpec._

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
        body should include element withName("input").withAttrValue("name", "documentationType.header.additionalInformation.code")
        body should include element withName("input").withAttrValue("name", "documentationType.header.additionalInformation.description")
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
        body should include element withName("input").withAttrValue("name", "parties.importer.name")
        body should include element withName("input").withAttrValue("name", "parties.importer.identifier")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.city")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.declarant.identifier")
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
  }


  "A POST Request" should {
    "return 404 when the SinglePageDeclaration feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      val formData = Map[String, Seq[String]]()
      new PostScenario(formData) {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      val formData = Map[String, Seq[String]]()
      new PostScenario(formData) {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "succeed when all required fields are present" in signedInScenario { user =>
      val formData = declarationTypeFormData ++ documentationFormData

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any())).thenReturn(Future.successful(DeclarationServiceResponse(<foo></foo>, 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK
        body should include element withName("dd").withValue("Good")

        val actualDeclaration = captor.getValue
        actualDeclaration.documentationType.additionalInformationCode mustBe("additionalInfoCode")
        actualDeclaration.documentationType.additionalInformationDescription mustBe("additionalInfoDescription")
      }
    }

    "fail when mandatory fields are missing" in signedInScenario { user =>
      val formData = Map[String, Seq[String]]()
      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withAttrValue("id", "declarationType.declarationType-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.additionalDeclarationType-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.goodsItemNumber-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.totalNumberOfItems-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.requestedProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.previousProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.additionalProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "documentationType.header.additionalInformation.code-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "documentationType.header.additionalInformation.description-error").withValue("This field is required")

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

  val documentationFormData = Map(
    "documentationType.previousDocCategory" -> Seq("previousDocCategory"),
    "documentationType.previousDocType" -> Seq("previousDocType"),
    "documentationType.previousDocReference" -> Seq("previousDocReference"),
    "documentationType.previousDocGoodsItemId" -> Seq("previousDocGoodsItemId"),
    "documentationType.header.additionalInformation.code" -> Seq("additionalInfoCode"),
    "documentationType.header.additionalInformation.description" -> Seq("additionalInfoDescription"),

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

}