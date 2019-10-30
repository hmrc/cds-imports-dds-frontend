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

import com.gu.scalatest.JsoupShouldMatchers
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.Helpers.{contentAsString, status}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{declaration_result, simplified_declaration}
import uk.gov.hmrc.govukfrontend.views.html.components._

import scala.concurrent.Future
import scala.xml.Elem

class SimplifiedDeclarationControllerSpec extends CdsImportsSpec
  with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  trait BaseScenario {
    val govukButton = new GovukButton()
    val formTemplate = new simplified_declaration(mainTemplate, govukButton)
    val resultTemplate = new declaration_result(mainTemplate)
    val mockDeclarationService = mock[CustomsDeclarationsService]
    val mockDeclarationStore = mock[DeclarationStore]
    val controller = new SimplifiedDeclarationController(formTemplate, resultTemplate, mockDeclarationService, mockDeclarationStore, mockAuthAction)
  }

  class GetScenario extends BaseScenario {
    val response = controller.show().apply(fakeRequestWithCSRF)
    val body = contentAsString(response).asBodyFragment
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
        body should include element withName("input").withAttrValue("name", "declarationType")
        body should include element withName("input").withAttrValue("name", "additionalDeclarationType")
        body should include element withName("input").withAttrValue("name", "goodsItemNumber")
        body should include element withName("input").withAttrValue("name", "totalNumberOfItems")
        body should include element withName("input").withAttrValue("name", "requestedProcedureCode")
        body should include element withName("input").withAttrValue("name", "previousProcedureCode")
        body should include element withName("input").withAttrValue("name", "additionalProcedureCode")
        // TODO add new fields
      }
    }

    "show the expected field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType").withValue("1.1 Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "additionalDeclarationType").withValue("1.2 Additional Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "goodsItemNumber").withValue("1.6 Goods Item Number")
        body should include element withClass("govuk-label").withAttrValue("for", "totalNumberOfItems").withValue("1.9 Total Number Of Items")
        body should include element withClass("govuk-label").withAttrValue("for", "requestedProcedureCode").withValue("1.10 Requested Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "previousProcedureCode").withValue("1.10 Previous Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "additionalProcedureCode").withValue("1.11 Additional Procedure Code (000 or C07)")
        // TODO add new fields
      }
    }

    "show the expected pre-populated field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType").withAttrValue("value", "IM")
        body should include element withName("input").withAttrValue("name", "additionalDeclarationType").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "goodsItemNumber").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "totalNumberOfItems").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "requestedProcedureCode").withAttrValue("value", "40")
        body should include element withName("input").withAttrValue("name", "previousProcedureCode").withAttrValue("value", "00")
        body should include element withName("input").withAttrValue("name", "additionalProcedureCode").withAttrValueMatching("value", "000")
        // TODO add new fields
        // TODO determine which fields should really be pre-populated, and modify tests accordingly
      }
    }
  }

  class PostScenario(formData: Map[String, Seq[String]],
                     declarationsServiceMockSetup: CustomsDeclarationsService => Unit,
                     declarationStoreMockSetup: DeclarationStore => Unit
                    ) extends BaseScenario {
    declarationsServiceMockSetup(mockDeclarationService)
    declarationStoreMockSetup(mockDeclarationStore)
    val formRequest = fakeRequestWithCSRF.withBody(AnyContentAsFormUrlEncoded(formData))
    val response = controller.submit.apply(formRequest)
    val body = contentAsString(response).asBodyFragment
  }


  "A POST Request" should {
    "return 404 when the SinglePageDeclaration feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      val formData = Map[String, Seq[String]]()
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      val formData = Map[String, Seq[String]]()
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "succeed when all required fields are present" in signedInScenario { user =>
    // TODO determine which fields are mandatory, and modify tests accordingly
    val formData = Map("declarationType" -> Seq("declarationType"),
        "additionalDeclarationType" -> Seq("additionalDeclarationType"),
        "goodsItemNumber" -> Seq("goodsItemNumber"),
        "totalNumberOfItems" -> Seq("totalNumberOfItems"),
        "requestedProcedureCode" -> Seq("requestedProcedureCode"),
        "previousProcedureCode" -> Seq("previousProcedureCode"),
        "additionalProcedureCode" -> Seq("additionalProcedureCode"),
        "previousDocCategory" -> Seq("previousDocCategory"),
        "previousDocType" -> Seq("previousDocType"),
        "previousDocReference" -> Seq("previousDocReference"),
        "previousDocGoodsItemId" -> Seq("previousDocGoodsItemId"),
        "additionalInfoCode" -> Seq("additionalInfoCode"),
        "additionalInfoDescription" -> Seq("additionalInfoDescription"),
        "additionalDocCategoryCode" -> Seq("additionalDocCategoryCode"),
        "additionalDocTypeCode" -> Seq("additionalDocTypeCode"),
        "additionalDocId" -> Seq("additionalDocId"),
        "additionalDocLPCO" -> Seq("additionalDocLPCO"),
        "additionalDocName" -> Seq("additionalDocName")
      )
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) mustBe Status.OK
        body should include element withName("dd").withValue("Good")
      }
    }

    "fail when some mandatory fields are missing" in signedInScenario { user =>
      val formData = Map("declarationType" -> Seq("declarationType"))
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withAttrValue("id", "additionalDeclarationType-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "goodsItemNumber-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "totalNumberOfItems-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "requestedProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "previousProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "additionalProcedureCode-error").withValue("This field is required")
        // TODO determine which fields are mandatory, and modify tests accordingly
      }
    }
  }

  "The POST-ed xml" should {
    "work" in signedInScenario { user =>
      val formData = Map("declarationType" -> Seq("101"),
        "additionalDeclarationType" -> Seq("102"),
        "goodsItemNumber" -> Seq("103"),
        "totalNumberOfItems" -> Seq("104"),
        "requestedProcedureCode" -> Seq("105"),
        "previousProcedureCode" -> Seq("106"),
        "additionalProcedureCode" -> Seq("107"),
        "previousDocCategory" -> Seq("Y"),
        "previousDocType" -> Seq("DCR"),
        "previousDocReference" -> Seq("9GB201909014000"),
        "previousDocGoodsItemId" -> Seq("1"),
        "additionalInfoCode" -> Seq("00500"),
        "additionalInfoDescription" -> Seq("IMPORTER"),
        "additionalDocCategoryCode" -> Seq("N"),
        "additionalDocTypeCode" -> Seq("935"),
        "additionalDocId" -> Seq("12345/30.09.2019"),
        "additionalDocLPCO" -> Seq("AC"),
        "additionalDocName" -> Seq("DocumentName")
      )
      val captor: ArgumentCaptor[Elem] = ArgumentCaptor.forClass(classOf[Elem])
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), captor.capture())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) mustBe Status.OK
        val xml = captor.getValue
        // TODO these tests should be around DeclarationXML.fromImportDeclaration
        (xml \ "Declaration" \ "TypeCode").head.text mustBe "101102"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "SequenceNumeric").head.text mustBe "103"
        (xml \ "Declaration" \ "GoodsItemQuantity").head.text mustBe "104"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "GovernmentProcedure" \ "CurrentCode").map(_.text) mustBe List("105","107")
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "GovernmentProcedure" \ "PreviousCode").head.text mustBe "106"
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
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "AdditionalDocument" \ "Name").toList.map(_.text) contains "DocumentName"
        // TODO determine which fields are mandatory, and modify tests accordingly - should we omit non-populated tags or just leave empty?
      }
    }
  }
}

