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

class DeclarationController_valuationInformationAndTaxesSpec extends CdsImportsSpec
  with Scenarios with FutureAwaits with BeforeAndAfterEach {
  import DeclarationControllerSpec.declarationTypeFormData

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
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

  "A POST Request" should {
    val documentationFormData = Map(
      "documentationType.additionalPayment[0].additionalDocPaymentID" -> Seq(""),
      "documentationType.additionalPayment[0].additionalDocPaymentCategory" -> Seq(""),
      "documentationType.additionalPayment[0].additionalDocPaymentType" -> Seq(""),
      "documentationType.additionalPayment[1].additionalDocPaymentID" -> Seq(""),
      "documentationType.additionalPayment[1].additionalDocPaymentCategory" -> Seq(""),
      "documentationType.additionalPayment[1].additionalDocPaymentType" -> Seq(""),
      "documentationType.additionalDocument[0].categoryCode" -> Seq(""),
      "documentationType.additionalDocument[0].typeCode" -> Seq(""),
      "documentationType.additionalDocument[0].id" -> Seq(""),
      "documentationType.additionalDocument[0].lpco" -> Seq(""),
      "documentationType.additionalDocument[0].name" -> Seq(""),
      "documentationType.additionalDocument[1].categoryCode" -> Seq(""),
      "documentationType.additionalDocument[1].typeCode" -> Seq(""),
      "documentationType.additionalDocument[1].id" -> Seq(""),
      "documentationType.additionalDocument[1].lpco" -> Seq(""),
      "documentationType.additionalDocument[1].name" -> Seq(""),
      "documentationType.additionalDocument[2].categoryCode" -> Seq(""),
      "documentationType.additionalDocument[2].typeCode" -> Seq(""),
      "documentationType.additionalDocument[2].id" -> Seq(""),
      "documentationType.additionalDocument[2].lpco" -> Seq(""),
      "documentationType.additionalDocument[2].name" -> Seq(""),
      "documentationType.additionalDocument[3].categoryCode" -> Seq(""),
      "documentationType.additionalDocument[3].typeCode" -> Seq(""),
      "documentationType.additionalDocument[3].id" -> Seq(""),
      "documentationType.additionalDocument[3].lpco" -> Seq(""),
      "documentationType.additionalDocument[3].name" -> Seq("")
    )

    "succeed when all fields contain data" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.conditionCode" -> Seq("conditionCode"),
        "valuationInformationAndTaxes.locationID" -> Seq("locationID"),
        "valuationInformationAndTaxes.locationName" -> Seq("locationName"),
        "valuationInformationAndTaxes.paymentMethodCode" -> Seq("paymentMethodCode"),
        "valuationInformationAndTaxes.additionCode" -> Seq("additionCode"),
        "valuationInformationAndTaxes.itemChargeAmount" -> Seq("itemChargeAmount"),
        "valuationInformationAndTaxes.currencyID" -> Seq("currencyID"),
        "valuationInformationAndTaxes.rateNumeric" -> Seq("rateNumeric"),
        "valuationInformationAndTaxes.customsValuationMethodCode" -> Seq("customsValuationMethodCode"),
        "valuationInformationAndTaxes.dutyRegimeCode" -> Seq("dutyRegimeCode")
      ) ++ declarationTypeFormData ++ documentationFormData

      when(mockDeclarationService.submit(any(), any[Declaration])(any())).thenReturn(Future.successful(DeclarationServiceResponse(<foo></foo>, 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))

      new PostScenario(formData) {
        status(response) mustBe Status.OK
        body should include element withName("dd").withValue("Good")
      }
    }

    "succeed when all optional fields are empty" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.conditionCode" -> Seq(""),
        "valuationInformationAndTaxes.locationID" -> Seq(""),
        "valuationInformationAndTaxes.locationName" -> Seq(""),
        "valuationInformationAndTaxes.paymentMethodCode" -> Seq(""),
        "valuationInformationAndTaxes.additionCode" -> Seq(""),
        "valuationInformationAndTaxes.itemChargeAmount" -> Seq(""),
        "valuationInformationAndTaxes.currencyID" -> Seq(""),
        "valuationInformationAndTaxes.rateNumeric" -> Seq(""),
        "valuationInformationAndTaxes.customsValuationMethodCode" -> Seq(""),
        "valuationInformationAndTaxes.dutyRegimeCode" -> Seq("")
      ) ++ declarationTypeFormData ++ documentationFormData

      when(mockDeclarationService.submit(any(), any[Declaration])(any())).thenReturn(Future.successful(DeclarationServiceResponse(<foo></foo>, 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))

      new PostScenario(formData) {
        status(response) mustBe Status.OK
        body should include element withName("dd").withValue("Good")
      }
    }
  }

}

