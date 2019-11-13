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

import java.time.LocalDateTime

import com.gu.scalatest.JsoupShouldMatchers
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers._
import play.api.test.{DefaultAwaitTimeout, FakeRequest, FutureAwaits}
import play.mvc.Http.HeaderNames
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Notification.issueDateTimeFormatter
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Notification, SubmissionStatus}
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsHeaderNames, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.test.CdsImportsSpec
import uk.gov.hmrc.cdsimportsddsfrontend.test.NotificationTestData._
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.view_notifications

import scala.collection.JavaConversions._
import scala.concurrent.Future


class NotificationsControllerSpec extends CdsImportsSpec with FutureAwaits with DefaultAwaitTimeout with JsoupShouldMatchers with MockitoSugar {

  val movementReferenceNumber: String = "MRN87878797"

  class Scenario() {
    val mockDeclarationStore = mock[DeclarationStore]
    val captor: ArgumentCaptor[Notification] = ArgumentCaptor.forClass(classOf[Notification])
    when(mockDeclarationStore.putNotification(captor.capture())(any())).thenReturn(Future.successful(true))
    val viewNotificationsTemplate = new view_notifications(mainTemplate)
    val controller = new NotificationsController(mockDeclarationStore, viewNotificationsTemplate)(mcc, appConfig)
  }

  "The handleNotification" should {
    "Reject a non-xml body and no headers" in new Scenario() {
      val response = controller.handleNotification(fakeRequest)
      val contents = contentAsString(response)
      status(response) mustBe 400
      contents mustBe "No Authorization Header"
    }

    "Accept with an xml body and the required headers" in {
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(validHeaders: _*)
        .withXmlBody(exampleReceivedNotificationXML(movementReferenceNumber))
      new Scenario() {
        val response = controller.handleNotification(xmlReq)
        val contents = contentAsString(response)
        status(response) mustBe 202
        contents mustBe ""
      }
    }

    "Put a single notification into the declaration store" in {
      val someDateTimeAsString = "20100102012000Z"
      val xml = exampleReceivedNotificationXML(movementReferenceNumber, someDateTimeAsString)
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(validHeaders: _*)
        .withXmlBody(xml)
      new Scenario() {
        controller.handleNotification(xmlReq)

        private val storedNotification = captor.getValue
        storedNotification.actionId mustBe "XConv1"
        storedNotification.mrn mustBe movementReferenceNumber
        storedNotification.dateTimeIssued mustBe LocalDateTime.parse(someDateTimeAsString, issueDateTimeFormatter)
        storedNotification.status mustBe SubmissionStatus.RECEIVED
        storedNotification.errors mustBe Seq.empty
        storedNotification.payload mustBe xml.toString
      }
    }

    "Return 500 if the notification can't be persisted" in {
      val xml = exampleReceivedNotificationXML(movementReferenceNumber)
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(validHeaders: _*)
        .withXmlBody(xml)
      new Scenario() {
        when(mockDeclarationStore.putNotification(any())(any())).thenReturn(Future.successful(false))
        val response = controller.handleNotification(xmlReq)
        status(response) mustBe 500
      }
    }

    "Put multiple notifications into the declaration store" in {
      val xml = exampleNotificationWithMultipleResponsesXML(movementReferenceNumber)
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(validHeaders: _*)
        .withXmlBody(xml)
      new Scenario() {
        controller.handleNotification(xmlReq)
        private val storedNotifications = captor.getAllValues
        storedNotifications.map(_.status) mustBe List(SubmissionStatus.RECEIVED, SubmissionStatus.ACCEPTED)
      }
    }

    "Return 500 if any notification can't be persisted" in {
      val xml = exampleNotificationWithMultipleResponsesXML(movementReferenceNumber)
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(validHeaders: _*)
        .withXmlBody(xml)
      new Scenario() {
        when(mockDeclarationStore.putNotification(any())(any()))
          .thenReturn(Future.successful(true))
          .thenReturn(Future.successful(false))
        val response = controller.handleNotification(xmlReq)
        status(response) mustBe 500
      }
    }

    "Reject without an Authorization Header" in {
      val headers = validHeaders.filterNot { case (a, b) => a == HeaderNames.AUTHORIZATION }
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(headers: _*)
        .withXmlBody(exampleReceivedNotificationXML(movementReferenceNumber))
      new Scenario() {
        val response = controller.handleNotification(xmlReq)
        val contents = contentAsString(response)
        status(response) mustBe 400
        contents mustBe "No Authorization Header"
      }
    }

    "Reject without a X-Conversation-ID Header" in {
      val headers = validHeaders.filterNot { case (a, b) => a == CustomsHeaderNames.XConversationIdName }
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(headers: _*)
        .withXmlBody(exampleReceivedNotificationXML(movementReferenceNumber))
      new Scenario() {
        val response = controller.handleNotification(xmlReq)
        val contents = contentAsString(response)
        status(response) mustBe 400
        contents mustBe "No X-Conversation-ID Header"
      }
    }

    "Reject correct headers with a non-xml body" in {
      val xmlReq = FakeRequest(POST, "/notification")
        .withHeaders(validHeaders: _*)
        .withTextBody("")
      new Scenario() {
        val response = controller.handleNotification(xmlReq)
        val contents = contentAsString(response)
        status(response) mustBe 400
        contents mustBe "Body is not xml"
      }
    }

  }


  "The show notifications" should {
    "Show a single notification" in new Scenario() {
      val notification = Notification("a1", "a2", LocalDateTime.now(), SubmissionStatus.ACCEPTED, Seq.empty, "a3")
      when(mockDeclarationStore.getNotifications()(any())).thenReturn(Future.successful(Seq(notification)))

      val response = controller.show(fakeRequest)
      val html = contentAsString(response).asBodyFragment
      status(response) mustBe 200
      html should include element withName("dd").withValue("a1")
      html should include element withName("dd").withValue("a2")
      html should include element withName("dd").withValue("a3")
      html should include element withName("dd").withValue("ACCEPTED")
    }

    "Show multiple notifications" in new Scenario() {
      val notification1 = Notification("a1", "a2", LocalDateTime.now(), SubmissionStatus.ACCEPTED, Seq.empty, "a3")
      val notification2 = Notification("a4", "a5", LocalDateTime.now(), SubmissionStatus.ACCEPTED, Seq.empty, "a6")
      when(mockDeclarationStore.getNotifications()(any())).thenReturn(Future.successful(Seq(notification1, notification2)))

      val response = controller.show(fakeRequest)
      status(response) mustBe 200
      val html = contentAsString(response).asBodyFragment
      html should include element withName("dd").withValue("a1")
      html should include element withName("dd").withValue("a2")
      html should include element withName("dd").withValue("a3")
      html should include element withName("dd").withValue("ACCEPTED")
      html should include element withName("dd").withValue("a4")
      html should include element withName("dd").withValue("a5")
      html should include element withName("dd").withValue("a6")
    }


  }


}
