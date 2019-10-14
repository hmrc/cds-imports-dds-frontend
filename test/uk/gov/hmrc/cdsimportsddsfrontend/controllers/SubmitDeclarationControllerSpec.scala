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
import org.mockito.ArgumentMatchers.any
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.Helpers._
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse
import uk.gov.hmrc.cdsimportsddsfrontend.services.CustomsDeclarationsService
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{submit_declaration, declaration_result}

import scala.concurrent.Future

class SubmitDeclarationControllerSpec extends CdsImportsSpec
  with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers {

  trait AllScenarios {
    val submitTemplate = new submit_declaration(mainTemplate)
    val resultTemplate = new declaration_result(mainTemplate)
    val mockDeclarationService = mock[CustomsDeclarationsService]
    val controller = new SubmitDeclarationController(submitTemplate, resultTemplate, mockAuthAction, mockDeclarationService)(appConfig, mcc)
  }

  class GetScenario extends AllScenarios {
    val response = controller.renderTemplate().apply(fakeRequestWithCSRF)
    val body = contentAsString(response).asBodyFragment
  }

  "A GET Request" should {
    "render the page page correctly" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("textarea").withAttrValue("id", "declaration-data")
      }
    }
  }


  class PostScenario(formData: Map[String, Seq[String]], mockSetup: CustomsDeclarationsService => Unit) extends AllScenarios {
    mockSetup(mockDeclarationService)
    val formRequest = fakeRequestWithCSRF.withBody(AnyContentAsFormUrlEncoded(formData))
    val response = controller.submit.apply(formRequest)
    val body = contentAsString(response).asBodyFragment
  }

  "A POST Request" should {
    "Succeed when submitting valid xml data" in signedInScenario { user =>
      val formData = Map("declaration-data" -> Seq("<declaration/>"))
      val mockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      new PostScenario(formData, mockSetup) {
        status(response) mustBe Status.OK
        body should include element withName("td").withValue("Good")
      }
    }

    "Fail when submitting not xml data " in signedInScenario { user =>
      val formData = Map("declaration-data" -> Seq("<declaration>"))
      val mockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any())(any())).thenReturn(Future.successful(CustomsDeclarationsResponse(200, Some("Good"))))
      new PostScenario(formData, mockSetup) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("body").withValue("This is not a valid xml document")
      }
    }


  }
}
