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

package uk.gov.hmrc.cdsimportsddsfrontend.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import play.api.libs.json.{Format, Json, OFormat}

import scala.util.Try
import cats.implicits._
import scala.xml.{Node, NodeSeq}

case class ConversationId(value: String) extends AnyVal
case class AuthToken(value: String) extends AnyVal
case class NotificationApiRequestHeaders(authToken: AuthToken, conversationId: ConversationId)


case class ErrorPointer(documentSectionCode: String, tagId: Option[String] = None)

object ErrorPointer {
  implicit val errorPointerFormat = Json.format[ErrorPointer]
}

case class NotificationError(validationCode: String, pointers: Seq[ErrorPointer])

object NotificationError {
  implicit val notificationErrorFormat = Json.format[NotificationError]
}


case class Notification(
                         actionId: String,
                         mrn: String,
                         dateTimeIssued: LocalDateTime,
                         status: SubmissionStatus.SubmissionStatus,
                         errors: Seq[NotificationError],
                         payload: String
                       )

object Notification {
  implicit val notificationFormat: OFormat[Notification] = Json.format[Notification]

  def buildNotificationsFromRequest(
                                             notificationApiRequestHeaders: NotificationApiRequestHeaders,
                                             notificationXml: NodeSeq
                                           ): Either[String, Seq[Notification]] =
    Either.catchNonFatal {
      val responsesXml = notificationXml \ "Response"

      responsesXml.map { singleResponseXml =>
        val mrn = (singleResponseXml \ "Declaration" \ "ID").text
        val formatter304 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssX")
        val dateTimeIssued =
          LocalDateTime.parse((singleResponseXml \ "IssueDateTime" \ "DateTimeString").text, formatter304)
        val functionCode = (singleResponseXml \ "FunctionCode").text

        val nameCode =
          if ((singleResponseXml \ "Status").nonEmpty)
            Some((singleResponseXml \ "Status" \ "NameCode").text)
          else None

        val errors = buildErrorsFromRequest(singleResponseXml)
        Notification(
          actionId = notificationApiRequestHeaders.conversationId.value,
          mrn = mrn,
          dateTimeIssued = dateTimeIssued,
          status = SubmissionStatus.retrieve(functionCode, nameCode),
          errors = errors,
          payload = notificationXml.toString
        )
      }

    }.leftMap(_.getMessage)

  private def buildErrorsFromRequest(singleResponseXml: Node): Seq[NotificationError] =
    if ((singleResponseXml \ "Error").nonEmpty) {
      val errorsXml = singleResponseXml \ "Error"
      errorsXml.map { singleErrorXml =>
        val validationCode = (singleErrorXml \ "ValidationCode").text
        val pointers = buildErrorPointers(singleErrorXml)
        NotificationError(validationCode = validationCode, pointers = pointers)
      }
    } else Seq.empty

  private def buildErrorPointers(singleErrorXml: Node): Seq[ErrorPointer] =
    if ((singleErrorXml \ "Pointer").nonEmpty) {
      val pointersXml = singleErrorXml \ "Pointer"
      pointersXml.map { singlePointerXml =>
        val documentSectionCode = (singlePointerXml \ "DocumentSectionCode").text
        val tagId = if ((singlePointerXml \ "TagID").nonEmpty) Some((singlePointerXml \ "TagID").text) else None
        ErrorPointer(documentSectionCode = documentSectionCode, tagId = tagId)
      }
    } else Seq.empty

}

