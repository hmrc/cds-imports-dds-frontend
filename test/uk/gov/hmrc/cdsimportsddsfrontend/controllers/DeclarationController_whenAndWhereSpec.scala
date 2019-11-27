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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.DeclarationViewModel
import uk.gov.hmrc.cdsimportsddsfrontend.domain._
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future

class DeclarationController_whenAndWhereSpec extends CdsImportsSpec
  with Scenarios with FutureAwaits with BeforeAndAfterEach {

  import DeclarationControllerSpec._

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  "A GET Request" should {
    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "whenAndWhere.destination.countryCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.exportCountry.id")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.origin.countryCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.origin.typeCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocation.name")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocation.typeCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocation.address.countryCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocation.address.typeCode")
      }
    }
  }

  "A POST Request" should {

    "post the expected data to the declaration service" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "whenAndWhere.destination.countryCode" -> Seq("destination countryCode"),
        "whenAndWhere.exportCountry.id" -> Seq("id"),
        "whenAndWhere.origin.countryCode" -> Seq("origin countryCode"),
        "whenAndWhere.origin.typeCode" -> Seq("typeCode"),
        "whenAndWhere.goodsLocation.name" -> Seq("goods location name"),
        "whenAndWhere.goodsLocation.typeCode" -> Seq("goods location type"),
        "whenAndWhere.goodsLocation.address.countryCode" -> Seq("goods location country code"),
        "whenAndWhere.goodsLocation.address.typeCode" -> Seq("goods location type code")
      ) ++ declarationTypeFormData

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        private val actualDeclaration = captor.getValue
        actualDeclaration.whenAndWhere mustBe WhenAndWhere(
          Some(Destination(Some("destination countryCode"))),
          Some(ExportCountry(id = Some("id"))),
          Some(Origin(countryCode = Some("origin countryCode"), typeCode = Some("typeCode")))
        )
      }
    }

    "succeed when all optional fields are empty" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "whenAndWhere.destination.countryCode" -> Seq(""),
        "whenAndWhere.exportCountry.id" -> Seq(""),
        "whenAndWhere.origin.countryCode" -> Seq(""),
        "whenAndWhere.origin.typeCode" -> Seq("")
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

