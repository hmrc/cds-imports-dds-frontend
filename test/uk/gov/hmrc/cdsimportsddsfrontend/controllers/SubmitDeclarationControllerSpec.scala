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

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.test.Helpers._
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.submit_declaration

class SubmitDeclarationControllerSpec extends CdsImportsSpec with GuiceOneAppPerSuite with AuthenticationBehaviours {

  implicit val testMaterializer = ActorMaterializer()(ActorSystem())

  val submitDeclarationTemplate = new submit_declaration(mainTemplate)
  private val controller = new SubmitDeclarationController(submitDeclarationTemplate, mockAuthAction)

  "GET" should {
    "return 200" in {
      val result = controller.renderTemplate(csrfReq)
      status(result) mustBe Status.OK
    }

    "return HTML" in {
      val result = controller.renderTemplate(csrfReq)
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }

  }
}
