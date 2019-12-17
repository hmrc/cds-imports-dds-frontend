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

import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers.{charset, contentAsString, contentType, status}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.your_import_declarations
import uk.gov.hmrc.govukfrontend.views.html.components.GovukButton

class YourImportDeclarationsControllerSpec extends CdsImportsSpec with AuthenticationBehaviours
  with GuiceOneAppPerSuite {

  val pageTemplate = new your_import_declarations(mainTemplate, new GovukButton())

  implicit val myMcc = mcc
  implicit val defaultLang = langs.availables.head

  private val controller = new YourImportDeclarationsController(pageTemplate)(mockAuthAction,mcc)

  "GET /" should {
    "return 200" in signedInScenario { _ =>
      val result = controller.yourDeclarations(fakeRequest)
      status(result) mustBe Status.OK
    }

    "return HTML" in signedInScenario { _ =>
      val result = controller.yourDeclarations(fakeRequest)
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }

    "show the button to the Single Page Declaration page" in signedInScenario { _ =>
      val response = controller.yourDeclarations(fakeRequest)
      val html = contentAsString(response).asBodyFragment
      val singlePageDeclarationUrl = routes.DeclarationController.show().url

      html should include element withName("a").withAttrValue("href", singlePageDeclarationUrl)
        .withValue(messagesApi("yourDeclarations.newDeclarationButton"))
    }

    "display message 'no declarations' if no declarations available" in signedInScenario { _ =>
      val response = controller.yourDeclarations(fakeRequest)
      val html = contentAsString(response).asBodyFragment
      html should include element(withName("p")
        .withValue(messagesApi("yourDeclarations.noDeclarations")))
    }
  }
}
