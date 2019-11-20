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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Address, Declaration, Party}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
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

    "show the expected declaration form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType.declarationType")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalDeclarationType")
        body should include element withName("input").withAttrValue("name", "declarationType.goodsItemNumber")
        body should include element withName("input").withAttrValue("name", "declarationType.totalNumberOfItems")
        body should include element withName("input").withAttrValue("name", "declarationType.requestedProcedureCode")
        body should include element withName("input").withAttrValue("name", "declarationType.previousProcedureCode")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalProcedureCode")
      }
    }

    "show the expected previous document form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].id")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].id")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].id")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].id")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].id")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].id")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationType.header.additionalInformation.code")
        body should include element withName("input").withAttrValue("name", "documentationType.header.additionalInformation.description")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[0].code")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[0].description")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[1].code")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[1].description")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[2].code")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[2].description")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[3].code")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[3].description")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[4].code")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[4].description")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[5].code")
        body should include element withName("input").withAttrValue("name", "documentationType.item.additionalInformation[5].description")
      }
    }

    "show the expected additional document form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
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
      }
    }

    "show the expected additional payment form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[0].additionalDocPaymentID")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[0].additionalDocPaymentCategory")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[0].additionalDocPaymentType")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[1].additionalDocPaymentID")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[1].additionalDocPaymentCategory")
        body should include element withName("input").withAttrValue("name", "documentationType.additionalPayment[1].additionalDocPaymentType")
      }
    }

    "show the expected declaration field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_declarationType").withValue("1.1 Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_additionalDeclarationType").withValue("1.2 Additional Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_goodsItemNumber").withValue("1.6 Goods Item Number")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_totalNumberOfItems").withValue("1.9 Total Number Of Items")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_requestedProcedureCode").withValue("1.10 Requested Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_previousProcedureCode").withValue("1.10 Previous Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_additionalProcedureCode").withValue("1.11 Additional Procedure Code (000 or C07)")
      }
    }

    "show the previous document expected field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_0_categoryCode").withValue("2.1 Previous Document Category 1")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_0_id").withValue("2.1 Previous Document Goods Item Identifier 1")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_0_typeCode").withValue("2.1 Previous Document Type 1")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_0_lineNumeric").withValue("2.1 Previous Document Reference 1")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_1_categoryCode").withValue("2.1 Previous Document Category 2")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_1_id").withValue("2.1 Previous Document Goods Item Identifier 2")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_1_typeCode").withValue("2.1 Previous Document Type 2")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_1_lineNumeric").withValue("2.1 Previous Document Reference 2")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_2_categoryCode").withValue("2.1 Previous Document Category 3")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_2_id").withValue("2.1 Previous Document Goods Item Identifier 3")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_2_typeCode").withValue("2.1 Previous Document Type 3")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_2_lineNumeric").withValue("2.1 Previous Document Reference 3")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_3_categoryCode").withValue("2.1 Previous Document Category 4")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_3_id").withValue("2.1 Previous Document Goods Item Identifier 4")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_3_typeCode").withValue("2.1 Previous Document Type 4")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_3_lineNumeric").withValue("2.1 Previous Document Reference 4")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_4_categoryCode").withValue("2.1 Previous Document Category 5")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_4_id").withValue("2.1 Previous Document Goods Item Identifier 5")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_4_typeCode").withValue("2.1 Previous Document Type 5")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_4_lineNumeric").withValue("2.1 Previous Document Reference 5")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_5_categoryCode").withValue("2.1 Previous Document Category 6")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_5_id").withValue("2.1 Previous Document Goods Item Identifier 6")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_5_typeCode").withValue("2.1 Previous Document Type 6")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationType_previousDocument_5_lineNumeric").withValue("2.1 Previous Document Reference 6")
      }
    }

    "show the expected pre-populated declaration field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType.declarationType").withAttrValue("value", "IM")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalDeclarationType").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "declarationType.goodsItemNumber").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "declarationType.totalNumberOfItems").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "declarationType.requestedProcedureCode").withAttrValue("value", "40")
        body should include element withName("input").withAttrValue("name", "declarationType.previousProcedureCode").withAttrValue("value", "00")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalProcedureCode").withAttrValue("value", "000")
      }
    }

    "show the expected pre-populated previous document field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].id").withAttrValue("value", "20191101")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].typeCode").withAttrValue("value", "CLE")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[0].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].id").withAttrValue("value", "9GB201909014000")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].typeCode").withAttrValue("value", "DCR")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[1].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].id").withAttrValue("value", "20191103")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].typeCode").withAttrValue("value", "ZZZ")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[2].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].id").withAttrValue("value", "9GB201909014002")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].typeCode").withAttrValue("value", "235")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[3].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].id").withAttrValue("value", "9GB201909014003")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].typeCode").withAttrValue("value", "ZZZ")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[4].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].id").withAttrValue("value", "9GB201909014004")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].typeCode").withAttrValue("value", "270")
        body should include element withName("input").withAttrValue("name", "documentationType.previousDocument[5].lineNumeric").withAttrValue("value", "1")
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
        body should include element withName("input").withAttrValue("name", "parties.buyer.name")
        body should include element withName("input").withAttrValue("name", "parties.buyer.identifier")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.city")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.buyer.phoneNumber")
        body should include element withName("input").withAttrValue("name", "parties.seller.name")
        body should include element withName("input").withAttrValue("name", "parties.seller.identifier")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.city")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.seller.phoneNumber")
        body should include element withName("input").withAttrValue("name", "parties.declarant.identifier")
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

    "post to the declaration service when all required fields are present" in signedInScenario { user =>
      val formData = declarationTypeFormData
      when(mockDeclarationService.submit(any(), any[Declaration])(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK
      }
    }

    "post the expected additional information data to the declaration service" in signedInScenario { user =>
      val formData = declarationTypeFormData ++ documentationFormData

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.documentationType.headerAdditionalInformation.code mustBe (Some("additionalInfoCode"))
        actualDeclaration.documentationType.headerAdditionalInformation.description mustBe (Some("additionalInfoDescription"))
        actualDeclaration.documentationType.itemAdditionalInformation(0).code mustBe (Some("itemAdditionalInfoCode 1"))
        actualDeclaration.documentationType.itemAdditionalInformation(0).description mustBe (Some("itemAdditionalInfoDescription 1"))
        actualDeclaration.documentationType.itemAdditionalInformation(1).code mustBe (Some("itemAdditionalInfoCode 2"))
        actualDeclaration.documentationType.itemAdditionalInformation(1).description mustBe (Some("itemAdditionalInfoDescription 2"))
        actualDeclaration.documentationType.itemAdditionalInformation(2).code mustBe (Some("itemAdditionalInfoCode 3"))
        actualDeclaration.documentationType.itemAdditionalInformation(2).description mustBe (Some("itemAdditionalInfoDescription 3"))
        actualDeclaration.documentationType.itemAdditionalInformation(3).code mustBe (Some("itemAdditionalInfoCode 4"))
        actualDeclaration.documentationType.itemAdditionalInformation(3).description mustBe (Some("itemAdditionalInfoDescription 4"))
        actualDeclaration.documentationType.itemAdditionalInformation(4).code mustBe (Some("itemAdditionalInfoCode 5"))
        actualDeclaration.documentationType.itemAdditionalInformation(4).description mustBe (Some("itemAdditionalInfoDescription 5"))
        actualDeclaration.documentationType.itemAdditionalInformation(5).code mustBe (Some("itemAdditionalInfoCode 6"))
        actualDeclaration.documentationType.itemAdditionalInformation(5).description mustBe (Some("itemAdditionalInfoDescription 6"))
      }
    }

    "post the expected Buyer to the declaration service" in signedInScenario { user =>
      val formData = declarationTypeFormData ++ Map(
        "parties.buyer.name" -> Seq("Foo Ltd"),
        "parties.buyer.address.streetAndNumber" -> Seq("123 Wembley Way"),
        "parties.buyer.address.city" -> Seq("London"),
        "parties.buyer.address.countryCode" -> Seq("GB"),
        "parties.buyer.address.postcode" -> Seq("W1A 1AA"),
        "parties.buyer.phoneNumber" -> Seq("0115 582 9384"),
        "parties.buyer.identifier" -> Seq("GB1966")
      )

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.buyer mustBe(Some(Party(
          name = Some("Foo Ltd"),
          identifier = Some("GB1966"),
          address = Some(Address("123 Wembley Way", "London", "GB", "W1A 1AA")),
          phoneNumber = Some("0115 582 9384"))))
      }
    }

    "post the expected Seller to the declaration service" in signedInScenario { user =>
      val formData = declarationTypeFormData ++ Map(
        "parties.seller.name" -> Seq("Bar Ltd"),
        "parties.seller.address.streetAndNumber" -> Seq("321 Arcade Av"),
        "parties.seller.address.city" -> Seq("Leeds"),
        "parties.seller.address.countryCode" -> Seq("GB"),
        "parties.seller.address.postcode" -> Seq("LS1 7DP"),
        "parties.seller.phoneNumber" -> Seq("0113 876 4567"),
        "parties.seller.identifier" -> Seq("GB2001")
      )

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.seller mustBe(Some(Party(
          name = Some("Bar Ltd"),
          identifier = Some("GB2001"),
          address = Some(Address("321 Arcade Av", "Leeds", "GB", "LS1 7DP")),
          phoneNumber = Some("0113 876 4567"))))
      }
    }

    "trims whitespace from buyer before binding the form" in signedInScenario { user =>
      val formData = declarationTypeFormData ++ Map(
        "parties.buyer.name" -> Seq("   Foo Ltd     "),
        "parties.buyer.address.streetAndNumber" -> Seq("123 Wembley Way     "),
        "parties.buyer.address.city" -> Seq("London     "),
        "parties.buyer.address.countryCode" -> Seq("GB     "),
        "parties.buyer.address.postcode" -> Seq("W1A 1AA     "),
        "parties.buyer.phoneNumber" -> Seq("     "),
        "parties.buyer.identifier" -> Seq("     ")
      )

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.buyer mustBe(Some(Party(
          name = Some("Foo Ltd"),
          identifier = None,
          address = Some(Address("123 Wembley Way", "London", "GB", "W1A 1AA")),
          phoneNumber = None)))
      }
    }

    "trims whitespace from seller before binding the form" in signedInScenario { user =>
      val formData = declarationTypeFormData ++ Map(
        "parties.seller.name" -> Seq("   Foo Ltd     "),
        "parties.seller.address.streetAndNumber" -> Seq("123 Wembley Way     "),
        "parties.seller.address.city" -> Seq("London     "),
        "parties.seller.address.countryCode" -> Seq("GB     "),
        "parties.seller.address.postcode" -> Seq("W1A 1AA     "),
        "parties.seller.phoneNumber" -> Seq("     "),
        "parties.seller.identifier" -> Seq("     ")
      )

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.seller mustBe(Some(Party(
          name = Some("Foo Ltd"),
          identifier = None,
          address = Some(Address("123 Wembley Way", "London", "GB", "W1A 1AA")),
          phoneNumber = None)))
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

  val previousDocumentFormData = Map(
    "documentationType.previousDocument[0].categoryCode" -> Seq("categoryCode"),
    "documentationType.previousDocument[0].typeCode" -> Seq("typeCode"),
    "documentationType.previousDocument[0].id" -> Seq("id"),
    "documentationType.previousDocument[0].lineNumeric" -> Seq("lineNumeric"),

    "documentationType.previousDocument[1].categoryCode" -> Seq("categoryCode2"),
    "documentationType.previousDocument[1].typeCode" -> Seq("typeCode2"),
    "documentationType.previousDocument[1].id" -> Seq("id2"),
    "documentationType.previousDocument[1].lineNumeric" -> Seq("lineNumeric2"),

    "documentationType.previousDocument[2].categoryCode" -> Seq("categoryCode"),
    "documentationType.previousDocument[2].typeCode" -> Seq("typeCode"),
    "documentationType.previousDocument[2].id" -> Seq("id"),
    "documentationType.previousDocument[2].lineNumeric" -> Seq("lineNumeric"),

    "documentationType.previousDocument[3].categoryCode" -> Seq("categoryCode"),
    "documentationType.previousDocument[3].typeCode" -> Seq("typeCode"),
    "documentationType.previousDocument[3].id" -> Seq("id"),
    "documentationType.previousDocument[3].lineNumeric" -> Seq("lineNumeric"),

    "documentationType.previousDocument[4].categoryCode" -> Seq("categoryCode"),
    "documentationType.previousDocument[4].typeCode" -> Seq("typeCode"),
    "documentationType.previousDocument[4].id" -> Seq("id"),
    "documentationType.previousDocument[4].lineNumeric" -> Seq("lineNumeric"),

    "documentationType.previousDocument[5].categoryCode" -> Seq("categoryCode"),
    "documentationType.previousDocument[5].typeCode" -> Seq("typeCode"),
    "documentationType.previousDocument[5].id" -> Seq("id"),
    "documentationType.previousDocument[5].lineNumeric" -> Seq("lineNumeric")
  )

  val additionalDocumentFormData = Map(
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

    "documentationType.additionalDocument[4].categoryCode" -> Seq("categoryCode"),
    "documentationType.additionalDocument[4].typeCode" -> Seq("typeCode"),
    "documentationType.additionalDocument[4].id" -> Seq("id"),
    "documentationType.additionalDocument[4].lpco" -> Seq("lpco"),
    "documentationType.additionalDocument[4].name" -> Seq("name"),

    "documentationType.additionalDocument[5].categoryCode" -> Seq("categoryCode"),
    "documentationType.additionalDocument[5].typeCode" -> Seq("typeCode"),
    "documentationType.additionalDocument[5].id" -> Seq("id"),
    "documentationType.additionalDocument[5].lpco" -> Seq("lpco"),
    "documentationType.additionalDocument[5].name" -> Seq("name")
  )

  val documentationFormData = Map(
    "documentationType.header.additionalInformation.code" -> Seq("additionalInfoCode"),
    "documentationType.header.additionalInformation.description" -> Seq("additionalInfoDescription"),

    "documentationType.item.additionalInformation[0].code" -> Seq("itemAdditionalInfoCode 1"),
    "documentationType.item.additionalInformation[0].description" -> Seq("itemAdditionalInfoDescription 1"),
    "documentationType.item.additionalInformation[1].code" -> Seq("itemAdditionalInfoCode 2"),
    "documentationType.item.additionalInformation[1].description" -> Seq("itemAdditionalInfoDescription 2"),
    "documentationType.item.additionalInformation[2].code" -> Seq("itemAdditionalInfoCode 3"),
    "documentationType.item.additionalInformation[2].description" -> Seq("itemAdditionalInfoDescription 3"),
    "documentationType.item.additionalInformation[3].code" -> Seq("itemAdditionalInfoCode 4"),
    "documentationType.item.additionalInformation[3].description" -> Seq("itemAdditionalInfoDescription 4"),
    "documentationType.item.additionalInformation[4].code" -> Seq("itemAdditionalInfoCode 5"),
    "documentationType.item.additionalInformation[4].description" -> Seq("itemAdditionalInfoDescription 5"),
    "documentationType.item.additionalInformation[5].code" -> Seq("itemAdditionalInfoCode 6"),
    "documentationType.item.additionalInformation[5].description" -> Seq("itemAdditionalInfoDescription 6"),

    "documentationType.localReferenceNumber" -> Seq("localRef"),

    "documentationType.additionalPayment[0].additionalDocPaymentID" -> Seq("123456"),
    "documentationType.additionalPayment[0].additionalDocPaymentCategory" -> Seq("1"),
    "documentationType.additionalPayment[0].additionalDocPaymentType" -> Seq("DAN"),

    "documentationType.additionalPayment[1].additionalDocPaymentID" -> Seq("123456"),
    "documentationType.additionalPayment[1].additionalDocPaymentCategory" -> Seq("1"),
    "documentationType.additionalPayment[1].additionalDocPaymentType" -> Seq("DAN")
  )

}