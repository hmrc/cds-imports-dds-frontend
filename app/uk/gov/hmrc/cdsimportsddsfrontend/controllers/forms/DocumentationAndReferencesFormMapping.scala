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
  val additionalDocument: (String, Mapping[Seq[AdditionalDocument]]) = "additionalDocument" -> seq(mapping(
    "categoryCode" -> optional(text),
    "typeCode" -> optional(text),
    "id" -> optional(text),
    "lpco" -> optional(text),
    "name" -> optional(text)
  )(AdditionalDocument.apply)(AdditionalDocument.unapply))
}

object AdditionalPaymentTypeFormMapping {
  val paymentType: (String, Mapping[Seq[AdditionalPaymentType]]) = "additionalPayment" -> seq(mapping(
    "additionalDocPaymentID" -> optional(text),
    "additionalDocPaymentCategory" -> optional(text),
    "additionalDocPaymentType" -> optional(text)
  )(AdditionalPaymentType.apply)(AdditionalPaymentType.unapply))
}

object PreviousDocumentFormMapping {
  val previousDocument: Mapping[PreviousDocument] = mapping(
    "categoryCode" -> optional(text),
    "id" -> optional(text),
    "typeCode" -> optional(text),
    "lineNumeric" -> optional(text)
  )(PreviousDocument.apply)(PreviousDocument.unapply)
}

object AdditionalInformationFormMapping {
  val additionalInformationMapping: Mapping[AdditionalInformation] = mapping(
    "code" -> optional(text),
    "description" -> optional(text)
  )(AdditionalInformation.apply)(AdditionalInformation.unapply)
  val additionalInformation: (String, Mapping[AdditionalInformation]) = "additionalInformation" -> additionalInformationMapping
}

object DocumentationAndReferencesFormMapping {
  import AdditionalPaymentTypeFormMapping.paymentType
  import AdditionalDocumentFormMapping.additionalDocument
  import PreviousDocumentFormMapping.previousDocument
  import AdditionalInformationFormMapping.additionalInformationMapping
  val documentationAndReferences: (String, Mapping[DocumentationAndReferencesViewModel]) = "documentationAndReferences" -> mapping(
    "header.previousDocument" -> seq(previousDocument),
    "item.previousDocument" -> seq(previousDocument),
    "header.additionalInformation" -> additionalInformationMapping,
    "item.additionalInformation" -> seq(additionalInformationMapping),
    additionalDocument,
    "localReferenceNumber" -> optional(text),
    paymentType
  )(DocumentationAndReferencesViewModel.apply)(DocumentationAndReferencesViewModel.unapply)
}
