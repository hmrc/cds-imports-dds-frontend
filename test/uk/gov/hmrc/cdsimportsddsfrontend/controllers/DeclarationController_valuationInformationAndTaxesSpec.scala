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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ChargeDeduction, CurrencyAmount, Declaration, ValuationInformationAndTaxes}
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
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.chargeDeduction.currencyAmount.amount")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.chargeDeduction.currencyAmount.currency")
        body should include element withName("input").withAttrValue("name", "valuationInformationAndTaxes.chargeDeduction.typeCode")
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

    "post the expected ValuationInformationAndTaxes to the declaration service" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.conditionCode" -> Seq("conditionCode"),
        "valuationInformationAndTaxes.locationID" -> Seq("locationID"),
        "valuationInformationAndTaxes.locationName" -> Seq("locationName"),
        "valuationInformationAndTaxes.paymentMethodCode" -> Seq("paymentMethodCode"),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.amount" -> Seq("chargeDeduction_currencyAmount_amount"),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.currency" -> Seq("chargeDeduction_currencyAmount_currency"),
        "valuationInformationAndTaxes.chargeDeduction.typeCode" -> Seq("chargeDeduction_typeCode"),
        "valuationInformationAndTaxes.additionCode" -> Seq("additionCode"),
        "valuationInformationAndTaxes.itemChargeAmount" -> Seq("itemChargeAmount"),
        "valuationInformationAndTaxes.currencyID" -> Seq("currencyID"),
        "valuationInformationAndTaxes.rateNumeric" -> Seq("rateNumeric"),
        "valuationInformationAndTaxes.customsValuationMethodCode" -> Seq("customsValuationMethodCode"),
        "valuationInformationAndTaxes.dutyRegimeCode" -> Seq("dutyRegimeCode")
      ) ++ declarationTypeFormData

      val captor: ArgumentCaptor[Declaration] = ArgumentCaptor.forClass(classOf[Declaration])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        private val actualDeclaration = captor.getValue
        actualDeclaration.valuationInformationAndTaxes mustBe(
          ValuationInformationAndTaxes(
            Some("conditionCode"), Some("locationID"), Some("locationName"), Some("paymentMethodCode"),
            Some(ChargeDeduction("chargeDeduction_typeCode",
              CurrencyAmount("chargeDeduction_currencyAmount_currency", "chargeDeduction_currencyAmount_amount"))),
            Some("additionCode"), Some("itemChargeAmount"), Some("currencyID"), Some("rateNumeric"),
            Some("customsValuationMethodCode"), Some("dutyRegimeCode")
          )
        )
      }
    }

    "succeed when all optional fields are empty" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.conditionCode" -> Seq(""),
        "valuationInformationAndTaxes.locationID" -> Seq(""),
        "valuationInformationAndTaxes.locationName" -> Seq(""),
        "valuationInformationAndTaxes.paymentMethodCode" -> Seq(""),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.amount" -> Seq(""),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.currency" -> Seq(""),
        "valuationInformationAndTaxes.chargeDeduction.typeCode" -> Seq(""),
        "valuationInformationAndTaxes.additionCode" -> Seq(""),
        "valuationInformationAndTaxes.itemChargeAmount" -> Seq(""),
        "valuationInformationAndTaxes.currencyID" -> Seq(""),
        "valuationInformationAndTaxes.rateNumeric" -> Seq(""),
        "valuationInformationAndTaxes.customsValuationMethodCode" -> Seq(""),
        "valuationInformationAndTaxes.dutyRegimeCode" -> Seq("")
      ) ++ declarationTypeFormData

      when(mockDeclarationService.submit(any(), any[Declaration])(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))

      new PostScenario(formData) {
        status(response) mustBe Status.OK
      }
    }

    "fail when chargeDeduction is missing the typeCode" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.amount" -> Seq("USD"),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.currency" -> Seq("999")
      ).withDefaultValue(Seq("")) ++ declarationTypeFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.chargeDeduction.typeCode-error")
      }
    }

    "fail when chargeDeduction is missing the currency" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.chargeDeduction.typeCode" -> Seq("FOO"),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.amount" -> Seq("123")
      ).withDefaultValue(Seq("")) ++ declarationTypeFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.chargeDeduction.currencyAmount.currency-error")
      }
    }

    "fail when chargeDeduction is missing the amount" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "valuationInformationAndTaxes.chargeDeduction.typeCode" -> Seq("FOO"),
        "valuationInformationAndTaxes.chargeDeduction.currencyAmount.currency" -> Seq("USD")
      ).withDefaultValue(Seq("")) ++ declarationTypeFormData

      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withValue("This field is required")
          .withAttrValue("id", "valuationInformationAndTaxes.chargeDeduction.currencyAmount.amount-error")
      }
    }
  }

}

