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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.{AddressViewModel, DeclarationViewModel, WhenAndWhereViewModel}
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
        body should include element withName("input").withAttrValue("name", "whenAndWhere.exportCountry")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.originCountryCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.originTypeCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.preferentialOriginCountryCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.preferentialOriginTypeCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationName")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationType")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationAddress.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationAddress.city")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationAddress.countryCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationAddress.postcode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.goodsLocationAddress.typeCode")
        body should include element withName("input").withAttrValue("name", "whenAndWhere.placeOfLoading")
      }
    }
  }

  "A POST Request" should {

    "post the expected data to the declaration service" in signedInScenario { user =>
      val formData: Map[String, Seq[String]] = Map(
        "whenAndWhere.destination.countryCode" -> Seq("destination countryCode"),
        "whenAndWhere.exportCountry" -> Seq("id"),
        "whenAndWhere.originCountryCode" -> Seq("origin countryCode"),
        "whenAndWhere.originTypeCode" -> Seq("origin typeCode"),
        "whenAndWhere.preferentialOriginCountryCode" -> Seq("preferential origin countryCode"),
        "whenAndWhere.preferentialOriginTypeCode" -> Seq("preferential origin typeCode"),
        "whenAndWhere.goodsLocationName" -> Seq("goods location name"),
        "whenAndWhere.goodsLocationType" -> Seq("goods location type"),
        "whenAndWhere.goodsLocationAddress.streetAndNumber" -> Seq("goods location street & number"),
        "whenAndWhere.goodsLocationAddress.city" -> Seq("goods location city"),
        "whenAndWhere.goodsLocationAddress.countryCode" -> Seq("goods location country code"),
        "whenAndWhere.goodsLocationAddress.postcode" -> Seq("goods location postcode"),
        "whenAndWhere.goodsLocationAddress.typeCode" -> Seq("goods location type code"),
        "whenAndWhere.placeOfLoading" -> Seq("an airport")
      ) ++ mandatoryFormData

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        private val actualDeclaration = captor.getValue
        actualDeclaration.whenAndWhereViewModel mustBe WhenAndWhereViewModel(
          Some(Destination(Some("destination countryCode"))),
          Some("id"),
          Some("origin countryCode"),
          Some("origin typeCode"),
          Some("preferential origin countryCode"),
          Some("preferential origin typeCode"),
          goodsLocationName = Some("goods location name"),
          goodsLocationType = Some("goods location type"),
          goodsLocationAddress = Some(AddressViewModel(
            Some("goods location street & number"),
            Some("goods location city"),
            Some("goods location country code"),
            Some("goods location postcode"),
            Some("goods location type code"))),
          Some("an airport")
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
  }
}

