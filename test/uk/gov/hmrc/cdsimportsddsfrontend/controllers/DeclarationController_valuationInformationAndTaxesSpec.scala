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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.{DeclarationViewModel, ValuationInformationAndTaxesViewModel}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ChargeDeduction, CurrencyAmount}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future

class DeclarationController_valuationInformationAndTaxesSpec extends CdsImportsSpec
  with Scenarios with FutureAwaits with BeforeAndAfterEach {
  import DeclarationControllerSpec._

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
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.amount")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.currency")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.itemChargeDeduction.typeCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.additionCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.itemChargeAmount.amount")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.itemChargeAmount.currency")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.rateNumeric")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.customsValuationMethodCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.dutyRegimeCode")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.amount")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.currency")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.headerChargeDeduction.typeCode")
      }
    }
  }

  "A POST Request" should {

    "post the expected ValuationInformationAndTaxes to the declaration service" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.conditionCode" -> Seq("conditionCode"),
        "valuationInformationAndTaxes.locationID" -> Seq("locationID"),
        "valuationInformationAndTaxes.locationName" -> Seq("locationName"),
        "valuationInformationAndTaxes.paymentMethodCode" -> Seq("paymentMethodCode"),
        "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.amount" -> Seq("itemChargeDeduction_currencyAmount_amount"),
        "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.currency" -> Seq("itemChargeDeduction_currencyAmount_currency"),
        "valuationInformationAndTaxes.itemChargeDeduction.typeCode" -> Seq("itemChargeDeduction_typeCode"),
        "valuationInformationAndTaxes.additionCode" -> Seq("additionCode"),
        "valuationInformationAndTaxes.itemChargeAmount.amount" -> Seq("itemChargeAmount"),
        "valuationInformationAndTaxes.itemChargeAmount.currency" -> Seq("currencyID"),
        "valuationInformationAndTaxes.rateNumeric" -> Seq("rateNumeric"),
        "valuationInformationAndTaxes.customsValuationMethodCode" -> Seq("customsValuationMethodCode"),
        "valuationInformationAndTaxes.dutyRegimeCode" -> Seq("dutyRegimeCode"),
        "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.amount" -> Seq("headerChargeDeduction_currencyAmount_amount"),
        "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.currency" -> Seq("headerChargeDeduction_currencyAmount_currency"),
        "valuationInformationAndTaxes.headerChargeDeduction.typeCode" -> Seq("headerChargeDeduction_typeCode")
      ) ++ mandatoryFormData

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        private val actualDeclaration: DeclarationViewModel = captor.getValue
        actualDeclaration.valuationInformationAndTaxesViewModel mustBe(
          ValuationInformationAndTaxesViewModel(
            Some("conditionCode"), Some("locationID"), Some("locationName"), Some("paymentMethodCode"),
            Some(ChargeDeduction("itemChargeDeduction_typeCode",
              CurrencyAmount("itemChargeDeduction_currencyAmount_currency", "itemChargeDeduction_currencyAmount_amount"))),
            Some("additionCode"), Some(CurrencyAmount("currencyID", "itemChargeAmount")), Some("rateNumeric"),
            Some("customsValuationMethodCode"), Some("dutyRegimeCode"),
            Some(ChargeDeduction("headerChargeDeduction_typeCode",
              CurrencyAmount("headerChargeDeduction_currencyAmount_currency", "headerChargeDeduction_currencyAmount_amount")))
          )
        )
      }
    }

    "succeed when all optional fields are empty" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = mandatoryFormData

      when(mockDeclarationService.submit(any(), any[DeclarationViewModel])(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))

      new PostScenario(formData) {
        status(response) mustBe Status.OK
      }
    }

    "fail when item chargeDeduction is missing the typeCode" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.amount" -> Seq("USD"),
        "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.currency" -> Seq("999")
      ) ++ mandatoryFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.itemChargeDeduction.typeCode-error")
      }
    }

    "fail when header chargeDeduction is missing the typeCode" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.amount" -> Seq("USD"),
        "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.currency" -> Seq("999")
      ) ++ mandatoryFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.headerChargeDeduction.typeCode-error")
      }
    }

    "fail when item chargeDeduction is missing the currency" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.itemChargeDeduction.typeCode" -> Seq("FOO"),
        "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.amount" -> Seq("123")
      ) ++ mandatoryFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.currency-error")
      }
    }

    "fail when header chargeDeduction is missing the currency" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.headerChargeDeduction.typeCode" -> Seq("FOO"),
        "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.amount" -> Seq("123")
      ) ++ mandatoryFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.currency-error")
      }
    }

    "fail when item chargeDeduction is missing the amount" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.itemChargeDeduction.typeCode" -> Seq("FOO"),
        "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.currency" -> Seq("USD")
      ) ++ mandatoryFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.itemChargeDeduction.currencyAmount.amount-error")
      }
    }

    "fail when header chargeDeduction is missing the amount" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.headerChargeDeduction.typeCode" -> Seq("FOO"),
        "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.currency" -> Seq("USD")
      ) ++ mandatoryFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.headerChargeDeduction.currencyAmount.amount-error")
      }
    }
  }

}

