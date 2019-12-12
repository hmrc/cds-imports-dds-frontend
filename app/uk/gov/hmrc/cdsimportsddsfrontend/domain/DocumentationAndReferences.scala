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

case class WriteOff(quantityQuantity: Option[String], unitCode: Option[String])

case class Submitter(name: Option[String])

case class AdditionalDocument(
  categoryCode: Option[String],
  typeCode: Option[String],
  id: Option[String],
  lpco: Option[String],
  name: Option[String],
  submitter: Option[Submitter],
  effectiveDateTime: Option[String],
  writeOff: Option[WriteOff])

case class AdditionalPaymentType(
  additionalDocPaymentID: Option[String],
  additionalDocPaymentCategory: Option[String],
  additionalDocPaymentType: Option[String])

case class AdditionalInformation(
  code: Option[String],
  description: Option[String]
)

case class DocumentationAndReferencesViewModel(
                                       headerPreviousDocuments: Seq[PreviousDocument],
                                       itemPreviousDocuments: Seq[PreviousDocument],
                                       headerAdditionalInformation: AdditionalInformation,
                                       itemAdditionalInformation: Seq[AdditionalInformation],                                  
                                       localReferenceNumber: Option[String],
                                       additionalPayments: Seq[AdditionalPaymentType]
)

object DocumentationAndReferencesViewModel {
  def apply(): DocumentationAndReferencesViewModel =
    new DocumentationAndReferencesViewModel(
      headerPreviousDocuments = Seq.empty,
      itemPreviousDocuments = Seq.empty,
      headerAdditionalInformation = AdditionalInformation(None, None),
      itemAdditionalInformation = Seq.empty,
      localReferenceNumber = None,
      additionalPayments = Seq.empty
    )
}
