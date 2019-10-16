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

import org.scalatest.WordSpec
import play.api.test.Helpers.contentAsString
import play.api.test.{DefaultAwaitTimeout, FakeRequest, FutureAwaits, NoMaterializer}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import com.gu.scalatest.JsoupShouldMatchers
import play.api.mvc._
import play.api.test.Helpers._
import play.mvc.Http.HeaderNames
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsHeaderNames
import uk.gov.hmrc.cdsimportsddsfrontend.test.NotificationTestData._

import scala.xml.Elem

class NotificationsControllerSpec extends CdsImportsSpec with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers {

  val mrn: String = "MRN87878797"

  class Scenario(req:Request[AnyContent]) {
  //class Scenario() {
    val controller = new NotificationsController()(mcc)
    val response = controller.handleNotification(req)
    val contents = contentAsString(response)
  }

  "The controller" should {
    "Reject a non-xml body and no headers" in new Scenario(fakeRequest) {
      status(response) mustBe 500
      contents mustBe "No Authorization Header"
    }

    "Accept with an xml body and the required headers" in {
      val xmlReq = FakeRequest(POST,"/notification")
        .withHeaders(validHeaders:_*)
        .withXmlBody(exampleReceivedNotificationXML(mrn))
      new Scenario(xmlReq) {
        status(response) mustBe 202
        contents mustBe ""
      }
    }

    "Reject without an Athorization Header" in {
      val headers = validHeaders.filterNot{case (a,b) => a == HeaderNames.AUTHORIZATION}
      val xmlReq = FakeRequest(POST,"/notification")
        .withHeaders(headers:_*)
        .withXmlBody(exampleReceivedNotificationXML(mrn))
      new Scenario(xmlReq) {
        status(response) mustBe 500
        contents mustBe "No Authorization Header"
      }
    }

    "Reject without a X-Conversation-ID Header" in {
      val headers = validHeaders.filterNot{case (a,b) => a == CustomsHeaderNames.XConversationIdName}
      val xmlReq = FakeRequest(POST,"/notification")
        .withHeaders(headers:_*)
        .withXmlBody(exampleReceivedNotificationXML(mrn))
      new Scenario(xmlReq) {
        status(response) mustBe 500
        contents mustBe "No X-Conversation-ID Header"
      }
    }

    "Reject correct headers with a non-xml body" in {
      val xmlReq = FakeRequest(POST,"/notification")
        .withHeaders(validHeaders:_*)
        .withTextBody("")
      new Scenario(xmlReq) {
        status(response) mustBe 500
        contents mustBe "Body is not xml"
      }
    }


  }

}
