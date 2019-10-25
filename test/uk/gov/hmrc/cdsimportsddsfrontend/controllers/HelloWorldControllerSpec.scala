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
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.MessagesControllerComponents
import play.api.test.Helpers._
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.test.CdsImportsSpec
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.hello_world
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

class HelloWorldControllerSpec extends CdsImportsSpec with GuiceOneAppPerSuite with BeforeAndAfterEach with JsoupShouldMatchers {

  val helloWordTemplate = new hello_world(mainTemplate)
  implicit val foo: MessagesControllerComponents = stubMessagesControllerComponents()

  private val controller = new HelloWorldController(helloWordTemplate)

  override def beforeEach(): Unit = {
    featureSwitchRegistry.HelloWorld.enable()
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  "GET /" should {
    "return 200" in {
      val result = controller.helloWorld(fakeRequest)
      status(result) mustBe Status.OK
    }

    "return HTML" in {
      val result = controller.helloWorld(fakeRequest)
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }

    "return 404 when the HelloWorld feature is disabled" in {
      featureSwitchRegistry.HelloWorld.disable()
      val response = controller.helloWorld(fakeRequest)
      status(response) must be(Status.NOT_FOUND)
    }

    "return 503 when the HelloWorld feature is suspended" in {
      featureSwitchRegistry.HelloWorld.suspend()
      val response = controller.helloWorld(fakeRequest)
      status(response) must be(Status.SERVICE_UNAVAILABLE)
    }

    "show the link to the Single Page Declaration page when that feature is enabled" in {
      val response = controller.helloWorld(fakeRequest)
      val html = contentAsString(response).asBodyFragment
      html should include element withName("a").withAttrValue("href", "/customs/imports/single-page-declaration").withValue("Submit declaration FORM")
    }

    "omit the link to the Single Page Declaration page when that feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      val response = controller.helloWorld(fakeRequest)
      val html = contentAsString(response).asBodyFragment
      html should not include element(withName("a").withAttrValue("href", "/customs/imports/single-page-declaration").withValue("Submit declaration FORM"))
    }

    "omit the link to the Single Page Declaration page when that feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      val response = controller.helloWorld(fakeRequest)
      val html = contentAsString(response).asBodyFragment
      html should not include element(withName("a").withAttrValue("href", "/customs/imports/single-page-declaration").withValue("Submit declaration FORM"))
    }
  }

}
