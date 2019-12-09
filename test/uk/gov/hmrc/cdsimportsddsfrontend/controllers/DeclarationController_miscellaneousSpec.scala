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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.{DeclarationViewModel, MiscellaneousViewModel}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CurrencyAmount
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future


class DeclarationController_miscellaneousSpec extends CdsImportsSpec
  with Scenarios with FutureAwaits with BeforeAndAfterEach {

  import DeclarationControllerSpec._

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  "A GET Request" should {
    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "miscellaneous.quotaOrderNumber")
        body should include element withName("input").withAttrValue("name", "miscellaneous.guaranteeType")
        body should include element withName("input").withAttrValue("name", "miscellaneous.grn")
        body should include element withName("input").withAttrValue("name", "miscellaneous.otherGRN")
        body should include element withName("input").withAttrValue("name", "miscellaneous.accessCode")
        body should include element withName("input").withAttrValue("name", "miscellaneous.importDutyAndOtherCharges.amount")
        body should include element withName("input").withAttrValue("name", "miscellaneous.importDutyAndOtherCharges.currency")
        body should include element withName("input").withAttrValue("name", "miscellaneous.customsOffice")
        body should include element withName("input").withAttrValue("name", "miscellaneous.natureOfTransaction")
        body should include element withName("input").withAttrValue("name", "miscellaneous.statisticalValue.amount")
        body should include element withName("input").withAttrValue("name", "miscellaneous.statisticalValue.currency")
      }
    }
  }

  "A POST Request" should {

    "post the expected data to the declaration service" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "miscellaneous.quotaOrderNumber" -> Seq("321"),
        "miscellaneous.guaranteeType" -> Seq("123"),
        "miscellaneous.grn" -> Seq("456"),
        "miscellaneous.otherGRN" -> Seq("789"),
        "miscellaneous.accessCode" -> Seq("2468"),
        "miscellaneous.importDutyAndOtherCharges.currency" -> Seq("GBP"),
        "miscellaneous.importDutyAndOtherCharges.amount" -> Seq("12345"),
        "miscellaneous.customsOffice" -> Seq("Shipley"),
        "miscellaneous.natureOfTransaction" -> Seq("4"),
        "miscellaneous.statisticalValue.amount" -> Seq("9876"),
        "miscellaneous.statisticalValue.currency" -> Seq("JPY")
      ) ++ declarationTypeFormData

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        private val actualDeclaration = captor.getValue
        actualDeclaration.miscellaneousViewModel mustBe
          MiscellaneousViewModel(Some("321"), Some("123"), Some("456"), Some("789"), Some("2468"),
            Some(CurrencyAmount("GBP", "12345")), Some("Shipley"), Some("4"), Some(CurrencyAmount("JPY", "9876")))
      }
    }

    "succeed when all optional fields are empty" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = declarationTypeFormData

      when(mockDeclarationService.submit(any(), any[DeclarationViewModel])(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))

      new PostScenario(formData) {
        status(response) mustBe Status.OK
      }
    }
  }
}
