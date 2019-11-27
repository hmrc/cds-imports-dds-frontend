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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.{DeclarationViewModel, TransportInformationViewModel}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future

class DeclarationController_transportInformationSpec extends CdsImportsSpec
  with Scenarios with FutureAwaits with BeforeAndAfterEach {

  import DeclarationControllerSpec._

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  "A GET Request" should {
    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "transportInformation.container")
        body should include element withName("input").withAttrValue("name", "transportInformation.modeOfTransport")
        body should include element withName("input").withAttrValue("name", "transportInformation.transportIdentificationType")
        body should include element withName("input").withAttrValue("name", "transportInformation.transportId")
        body should include element withName("input").withAttrValue("name", "transportInformation.registrationNationalityCode")
      }
    }
  }

  "A POST Request" should {
    "post the expected data to the declaration service" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "transportInformation.container" -> Seq("container code"),
        "transportInformation.modeOfTransport" -> Seq("mode of transport"),
        "transportInformation.transportIdentificationType" -> Seq("transport identification type"),
        "transportInformation.transportId" -> Seq("transport id"),
        "transportInformation.registrationNationalityCode" -> Seq("registration nationality code")
      ) ++ declarationTypeFormData

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        private val actualDeclaration = captor.getValue
        actualDeclaration.transportInformationViewModel mustBe TransportInformationViewModel(
          Some("container code"),
          Some("mode of transport"),
          Some("transport identification type"),
          Some("transport id"),
          Some("registration nationality code")
        )
      }
    }

    "succeed when all optional fields are empty" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "transportInformation.container" -> Seq(""),
        "transportInformation.modeOfTransport" -> Seq(""),
        "transportInformation.transportIdentificationType" -> Seq(""),
        "transportInformation.transportId" -> Seq(""),
        "transportInformation.registrationNationalityCode" -> Seq("")
      ) ++ declarationTypeFormData

      when(mockDeclarationService.submit(any(), any[DeclarationViewModel])(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))

      new PostScenario(formData) {
        status(response) mustBe Status.OK
      }
    }
  }
}
