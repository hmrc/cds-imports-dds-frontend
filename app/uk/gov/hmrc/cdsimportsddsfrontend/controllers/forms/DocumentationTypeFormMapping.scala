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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms

import play.api.data.Forms.{mapping, optional, text, seq}
import play.api.data.Mapping
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

object AdditionalDocumentFormMapping {
  val additionalDocumentType: (String, Mapping[Seq[AdditionalDocumentType]]) = "additionalDocument" -> seq(mapping(
    "categoryCode" -> optional(text),
    "typeCode" -> optional(text),
    "id" -> optional(text),
    "lpco" -> optional(text),
    "name" -> optional(text)
  )(AdditionalDocumentType.apply)(AdditionalDocumentType.unapply))
}

object AdditionalPaymentTypeFormMapping {
  val paymentType: (String, Mapping[Seq[AdditionalPaymentType]]) = "additionalPayment" -> seq(mapping(
    "additionalDocPaymentID" -> optional(text),
    "additionalDocPaymentCategory" -> optional(text),
    "additionalDocPaymentType" -> optional(text)
  )(AdditionalPaymentType.apply)(AdditionalPaymentType.unapply))
}

object AdditionalInformationFormMapping extends FormValidators {
  val additionalInformationMapping: Mapping[AdditionalInformation] = mapping(
    "code" -> nonEmptyString,
    "description" -> nonEmptyString
  )(AdditionalInformation.apply)(AdditionalInformation.unapply)
  val additionalInformation: (String, Mapping[AdditionalInformation]) = "additionalInformation" -> additionalInformationMapping
}

object DocumentationTypeFormMapping extends FormValidators {
  import AdditionalPaymentTypeFormMapping.paymentType
  import AdditionalDocumentFormMapping.additionalDocumentType
  import AdditionalInformationFormMapping.additionalInformationMapping
  val documentationType: (String, Mapping[DocumentationType]) = "documentationType" -> mapping(
    "previousDocCategory" -> optional(text),
    "previousDocType" -> optional(text),
    "previousDocReference" -> optional(text),
    "previousDocGoodsItemId" -> optional(text),
    "header.additionalInformation" -> additionalInformationMapping,
    additionalDocumentType,
    "localReferenceNumber" -> optional(text),
    paymentType
  )(DocumentationType.apply)(DocumentationType.unapply)
}