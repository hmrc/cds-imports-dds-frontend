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
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.Helpers._
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationServiceResponse, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{declaration_result, submit_declaration}
import uk.gov.hmrc.govukfrontend.views.html.components.GovukButton

import scala.concurrent.Future
import scala.xml.Elem

class SubmitDeclarationControllerSpec extends CdsImportsSpec
  with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers {

  trait AllScenarios {
    val govukButton = new GovukButton()
    val submitTemplate = new submit_declaration(mainTemplate, govukButton)
    val resultTemplate = new declaration_result(mainTemplate)
    val mockDeclarationService = mock[CustomsDeclarationsService]
    val mockDeclarationStore = mock[DeclarationStore]
    val controller = new SubmitDeclarationController(submitTemplate, resultTemplate, mockAuthAction, mockDeclarationService, mockDeclarationStore)(appConfig, mcc)
  }

  class GetScenario extends AllScenarios {
    val response = controller.show().apply(fakeRequestWithCSRF)
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


  class PostScenario(formData: Map[String, Seq[String]],
                     declarationsServiceMockSetup: CustomsDeclarationsService => Unit,
                     declarationStoreMockSetup: DeclarationStore => Unit
                    ) extends AllScenarios {
    declarationsServiceMockSetup(mockDeclarationService)
    declarationStoreMockSetup(mockDeclarationStore)
    val formRequest = fakeRequestWithCSRF.withBody(AnyContentAsFormUrlEncoded(formData))
    val response = controller.submit.apply(formRequest)
    val body = contentAsString(response).asBodyFragment
  }

  "A POST Request" should {
    "when XML is valid" should {
      "Submit to the declaration service and return a success response" in signedInScenario { user =>
        val formData = Map("declaration-data" -> Seq("<declaration/>"))
        val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any[Elem] )(any())).thenReturn(Future.successful(DeclarationServiceResponse(<foo></foo>, 200, Some("Good"))))
        val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
        new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
          status(response) mustBe Status.OK
          body should include element withName("dd").withValue("Good")
        }
      }

      "Clear the declaration store" in signedInScenario { user =>
        val formData = Map("declaration-data" -> Seq("<declaration/>"))
        val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any[Elem] )(any())).thenReturn(Future.successful(DeclarationServiceResponse(<foo></foo>, 200, Some("Good"))))
        val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
        new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
          verify(mockDeclarationStore).deleteAllNotifications()
        }
      }
    }

    "when XML is invalid" should {
      "Not submit to the declaration service, and return an error response" in signedInScenario { user =>
        val formData = Map("declaration-data" -> Seq("<declaration>"))
        val customsDeclarationsServiceMockSetup: CustomsDeclarationsService => Unit = ds => when(ds.submit(any(), any[Elem] )(any())).thenReturn(Future.successful(DeclarationServiceResponse(<foo></foo>, 200, Some("Good"))))
        val declarationsStoreMockSetup: DeclarationStore => Unit = ds => when(ds.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
        new PostScenario(formData, customsDeclarationsServiceMockSetup, declarationsStoreMockSetup) {
          status(response) mustBe Status.BAD_REQUEST
          body should include element withName("body").withValue("This is not a valid xml document")
        }
      }
    }

  }
}
