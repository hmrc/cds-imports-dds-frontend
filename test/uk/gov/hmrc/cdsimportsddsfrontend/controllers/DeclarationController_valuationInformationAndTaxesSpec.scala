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

import akka.util.ByteString
import com.gu.scalatest.JsoupShouldMatchers
import org.jsoup.nodes.Element
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import play.api.libs.streams.Accumulator
import play.api.mvc.{AnyContentAsFormUrlEncoded, Result}
import play.api.test.Helpers.{contentAsString, status}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.DeclarationControllerSpec.declarationTypeFormData

import scala.concurrent.Future
import scala.xml.Elem

class DeclarationController_valuationInformationAndTaxesSpec extends CdsImportsSpec
  with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers with BeforeAndAfterEach {

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  class GetScenario extends BaseScenario {
    val response: Accumulator[ByteString, Result] = controller.show().apply(fakeRequestWithCSRF)
    val body: Element = contentAsString(response).asBodyFragment
  }

  "A GET Request" should {

    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.conditionCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.locationID")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.locationName")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.paymentMethodCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.additionCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.itemChargeAmount")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.currencyID")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.rateNumeric")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.customsValuationMethodCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.dutyRegimeCode")
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

    "succeed when all required fields are present" in signedInScenario { user =>
      val formData = Map(
        "valuationInformationAndTaxes.conditionCode" -> Seq("some data"),
        "valuationInformationAndTaxes.locationID" -> Seq("some data"),
        "valuationInformationAndTaxes.locationName" -> Seq("some data"),
        "valuationInformationAndTaxes.paymentMethodCode" -> Seq("some data"),
        "valuationInformationAndTaxes.additionCode" -> Seq("some data"),
        "valuationInformationAndTaxes.itemChargeAmount" -> Seq("some data"),
        "valuationInformationAndTaxes.currencyID" -> Seq("some data"),
        "valuationInformationAndTaxes.rateNumeric" -> Seq("some data"),
        "valuationInformationAndTaxes.customsValuationMethodCode" -> Seq("some data"),
        "valuationInformationAndTaxes.dutyRegimeCode" -> Seq("some data")
      ) ++ declarationTypeFormData
      val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
        status(response) mustBe Status.OK
        body should include element withName("dd").withValue("Good")
      }
    }

  }

  "The POST-ed xml" should {
    "contain the expected values" in signedInScenario { user =>
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
        "documentationType.additionalDocCategoryCode" -> Seq("N"),
        "documentationType.additionalDocTypeCode" -> Seq("935"),
        "documentationType.additionalDocId" -> Seq("12345/30.09.2019"),
        "documentationType.additionalDocLPCO" -> Seq("AC"),
        "documentationType.additionalDocName" -> Seq("DocumentName"),
        "documentationType.localReferenceNumber" -> Seq("localRef"),
        "documentationType.additionalDocPaymentID" -> Seq("123456"),
        "documentationType.additionalDocPaymentCategory" -> Seq("1"),
        "documentationType.additionalDocPaymentType" -> Seq("DAN")
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
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "GovernmentProcedure" \ "CurrentCode").map(_.text) mustBe List("105", "107")
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "GovernmentProcedure" \ "PreviousCode").head.text mustBe "106"
        (xml \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "PreviousDocument" \ "CategoryCode").toList.map(_.text) contains "Y"
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
        (xml \ "Declaration" \ "FunctionalReferenceID").head.text mustBe "localRef"
        (xml \ "Declaration" \ "AdditionalDocument" \ "ID").head.text mustBe "123456"
        (xml \ "Declaration" \ "AdditionalDocument" \ "CategoryCode").head.text mustBe "1"
        (xml \ "Declaration" \ "AdditionalDocument" \ "TypeCode").head.text mustBe "DAN"
        // TODO determine which fields are mandatory, and modify tests accordingly - should we omit non-populated tags or just leave empty?
      }
    }
  }
}

