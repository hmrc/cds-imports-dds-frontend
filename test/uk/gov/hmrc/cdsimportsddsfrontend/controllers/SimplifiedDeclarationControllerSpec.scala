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

import scala.concurrent.Future
import scala.xml.Elem

class SimplifiedDeclarationControllerSpec extends CdsImportsSpec
  with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  trait BaseScenario {
    val formTemplate = new simplified_declaration(mainTemplate)
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
      }
    }

    "show the expected field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType").withValue("Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "additionalDeclarationType").withValue("Additional Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "goodsItemNumber").withValue("Goods Item Number")
        body should include element withClass("govuk-label").withAttrValue("for", "totalNumberOfItems").withValue("Total Number Of Items")
        body should include element withClass("govuk-label").withAttrValue("for", "requestedProcedureCode").withValue("Requested Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "previousProcedureCode").withValue("Previous Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "additionalProcedureCode").withValue("Additional Procedure Code (000 or C07)")
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
        body should include element withName("input").withAttrValue("name", "additionalProcedureCode").withAttrValueMatching("value", "^$")
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
      val formData = Map("declarationType" -> Seq("declarationType"),
        "additionalDeclarationType" -> Seq("additionalDeclarationType"),
        "goodsItemNumber" -> Seq("goodsItemNumber"),
        "totalNumberOfItems" -> Seq("totalNumberOfItems"),
        "requestedProcedureCode" -> Seq("requestedProcedureCode"),
        "previousProcedureCode" -> Seq("previousProcedureCode"),
        "additionalProcedureCode" -> Seq("additionalProcedureCode")
      )
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      val formData = Map("declarationType" -> Seq("declarationType"),
        "additionalDeclarationType" -> Seq("additionalDeclarationType"),
        "goodsItemNumber" -> Seq("goodsItemNumber"),
        "totalNumberOfItems" -> Seq("totalNumberOfItems"),
        "requestedProcedureCode" -> Seq("requestedProcedureCode"),
        "previousProcedureCode" -> Seq("previousProcedureCode"),
        "additionalProcedureCode" -> Seq("additionalProcedureCode")
      )
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "succeed when all the fields are present" in signedInScenario { user =>
      val formData = Map("declarationType" -> Seq("declarationType"),
        "additionalDeclarationType" -> Seq("additionalDeclarationType"),
        "goodsItemNumber" -> Seq("goodsItemNumber"),
        "totalNumberOfItems" -> Seq("totalNumberOfItems"),
        "requestedProcedureCode" -> Seq("requestedProcedureCode"),
        "previousProcedureCode" -> Seq("previousProcedureCode"),
        "additionalProcedureCode" -> Seq("additionalProcedureCode")
      )
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) mustBe Status.OK
        body should include element withName("td").withValue("Good")
      }
    }

    "fail when some fields are missing" in signedInScenario { user =>
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
        "additionalProcedureCode" -> Seq("107")
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
      }
    }
  }
}

