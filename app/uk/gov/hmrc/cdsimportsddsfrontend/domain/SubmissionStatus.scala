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

import play.api.libs.json.{Format, Reads, Writes}

object SubmissionStatus extends Enumeration {
  type SubmissionStatus = Value

  implicit val format: Format[SubmissionStatus.Value] = EnumJson.format(SubmissionStatus)

  val PENDING, REQUESTED_CANCELLATION, ACCEPTED, RECEIVED, REJECTED, UNDERGOING_PHYSICAL_CHECK,
  ADDITIONAL_DOCUMENTS_REQUIRED, AMENDED, RELEASED, CLEARED, CANCELLED, CUSTOMS_POSITION_GRANTED,
  CUSTOMS_POSITION_DENIED, TAX_LIABILITY, INSUFFICIENT_BALANCE_IN_DAN, INSUFFICIENT_BALANCE_IN_DAN_REMINDER,
  GOODS_HAVE_EXITED_THE_COMMUNITY, DECLARATION_HANDLED_EXTERNALLY, AWAITING_EXIT_RESULTS, UNKNOWN = Value

  def retrieve(functionCode: String, nameCode: Option[String] = None): SubmissionStatus =
    codesMap.getOrElse(functionCode + nameCode.getOrElse(""), UNKNOWN)

  private val codesMap: Map[String, SubmissionStatus] = Map(
    "Pending" -> PENDING,
    "Cancellation Requested" -> REQUESTED_CANCELLATION,
    "01" -> ACCEPTED,
    "02" -> RECEIVED,
    "03" -> REJECTED,
    "05" -> UNDERGOING_PHYSICAL_CHECK,
    "06" -> ADDITIONAL_DOCUMENTS_REQUIRED,
    "07" -> AMENDED,
    "08" -> RELEASED,
    "09" -> CLEARED,
    "10" -> CANCELLED,
    "1139" -> CUSTOMS_POSITION_GRANTED,
    "1141" -> CUSTOMS_POSITION_DENIED,
    "13" -> TAX_LIABILITY,
    "14" -> INSUFFICIENT_BALANCE_IN_DAN,
    "15" -> INSUFFICIENT_BALANCE_IN_DAN_REMINDER,
    "17" -> DECLARATION_HANDLED_EXTERNALLY,
    "UnknownStatus" -> UNKNOWN
  )
}

object EnumJson {

  def format[E <: Enumeration](enum: E): Format[E#Value] =
    Format(Reads.enumNameReads(enum), Writes.enumNameWrites)

}
