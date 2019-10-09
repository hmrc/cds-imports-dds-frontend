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
import org.scalatest.WordSpec
import play.api.mvc.AnyContentAsFormUrlEncoded
import play.api.test.Helpers._
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.submit_declaration

class SubmitDeclarationControllerSpec extends WordSpec with CdsImportsSpec with AuthenticationBehaviours with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers{


  class GetScenario() {
    val submitTemplate = new submit_declaration(mainTemplate)
    val controller = new SubmitDeclarationController(submitTemplate,mockAuthAction)(appConfig,mcc)
    val response = controller.renderTemplate().apply(csrfReq)
    val body = contentAsString(response).asBodyFragment
  }

  "A GET Request" should {
    "render the page page correctly" in new GetScenario() {
      status(response) mustBe Status.OK
      body should include element withName("textarea").withAttrValue("id", "declaration-data")
    }
  }


  class PostScenario(formData:Map[String,Seq[String]]) {
    val submitTemplate = new submit_declaration(mainTemplate)
    val controller = new SubmitDeclarationController(submitTemplate,mockAuthAction)(appConfig,mcc)
    val formRequest = csrfReq.withBody(AnyContentAsFormUrlEncoded(formData))
    val response = controller.submit.apply(formRequest)
    val body = contentAsString(response).asBodyFragment
  }

  "A POST Request" should {
    "Submit the data" in  {
      val formData = Map("declaration-data" -> Seq("<declaration/>"))
      new PostScenario(formData) {
        status(response) mustBe Status.OK
        body should include element withName("body").withValue("SUCCESS! Customs Declaration submitted")
      }
    }
  }
}
