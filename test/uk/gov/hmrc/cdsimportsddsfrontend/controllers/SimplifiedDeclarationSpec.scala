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
import org.mockito.Mockito.when
import org.scalatest.WordSpec
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.Helpers.{contentAsString, status}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{declaration_result, simplified_declaration}
import play.api.test.Helpers._
import org.mockito.ArgumentMatchers.any

import scala.concurrent.Future

class SimplifiedDeclarationSpec extends CdsImportsSpec
  with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers {

  trait BaseScenario {
    val formTemplate = new simplified_declaration(mainTemplate)
    val resultTemplate = new declaration_result(mainTemplate)
    val mockDeclarationService = mock[CustomsDeclarationsService]
    val mockDeclarationStore = mock[DeclarationStore]
    val controller = new SimplifiedDeclaration(formTemplate, resultTemplate, mockDeclarationService, mockDeclarationStore, mockAuthAction)(appConfig, mcc)
  }

  class GetScenario extends BaseScenario {
    val response = controller.show().apply(fakeRequestWithCSRF)
    val body = contentAsString(response).asBodyFragment
  }

  "A GET Request" should {
    "render the page page correctly" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType")
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
}

