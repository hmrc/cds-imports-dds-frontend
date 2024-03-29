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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.model
import java.util.UUID

import uk.gov.hmrc.cdsimportsddsfrontend.domain._

case class DocumentationAndReferencesViewModel(
   headerPreviousDocuments: Seq[PreviousDocument],
   itemPreviousDocuments: Seq[PreviousDocument],
   headerAdditionalInformation: AdditionalInformation,
   itemAdditionalInformation: Seq[AdditionalInformation],
   additionalDocuments: Seq[AdditionalDocumentViewModel],
   localReferenceNumber: String,
   additionalPayments: Seq[AdditionalPaymentType]
) {

  def toDocumentationAndReferences: DocumentationAndReferences = {
    DocumentationAndReferences(
      headerPreviousDocuments = headerPreviousDocuments,
      itemPreviousDocuments = itemPreviousDocuments,
      headerAdditionalInformation = headerAdditionalInformation,
      itemAdditionalInformation = itemAdditionalInformation,
      localReferenceNumber = localReferenceNumber,
      additionalPayments = additionalPayments
    )
  }
}

object DocumentationAndReferencesViewModel {
  def apply(): DocumentationAndReferencesViewModel = DocumentationAndReferencesViewModel(
    Seq(
      PreviousDocument(Some("Y"),Some("20191101"), Some("CLE"),Some("1")),
      PreviousDocument(Some("Y"),Some("9GB201909014000"), Some("DCR"),Some("1")),
      PreviousDocument(Some("Y"),Some("20191101"), Some("CLE"),Some("1")),
      PreviousDocument(Some("Y"),Some("9GB201909014000"), Some("DCR"),Some("1"))
    ),
    Seq(
      PreviousDocument(Some("Y"),Some("20191101"), Some("CLE"),Some("1")),
      PreviousDocument(Some("Y"),Some("9GB201909014000"), Some("DCR"),Some("1")),
      PreviousDocument(Some("Z"),Some("20191103"), Some("ZZZ"),Some("1")),
      PreviousDocument(Some("Z"),Some("9GB201909014002"), Some("235"),Some("1")),
      PreviousDocument(Some("Z"),Some("9GB201909014003"), Some("ZZZ"),Some("1")),
      PreviousDocument(Some("Z"),Some("9GB201909014004"), Some("270"),Some("1"))
    ),
    AdditionalInformation(Some("TSP01"), Some("TSP")),
    Nil,
    Seq(
      AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), Some("DocumentName1")),
      AdditionalDocumentViewModel(Some("C"), Some("514"), Some("GBEIR201909014000"), None, None),
      AdditionalDocumentViewModel(Some("C"), Some("506"), Some("GBDPO1909241"), None, None),
      AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.07.2019"), Some("AC"), None),
      AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.08.2019"), Some("AC"), None),
      AdditionalDocumentViewModel(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), None)
    ),
    UUID.randomUUID().toString.replaceAll("-","").take(20),
    Seq(
      AdditionalPaymentType(Some("1909241"), Some("1"), Some("DAN")),
      AdditionalPaymentType(Some("1909241"), Some("1"), Some("DAN"))
    )
  )
}