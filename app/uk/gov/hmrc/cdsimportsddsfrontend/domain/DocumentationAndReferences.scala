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
import java.util.UUID

case class WriteOff(
  quantityQuantity: Option[String],
  unitCode: Option[String]
)

case class Submitter(
  name: Option[String]
)

case class AdditionalDocument(
  categoryCode: Option[String],
  typeCode: Option[String],
  id: Option[String],
  lpco: Option[String],
  name: Option[String],
  submitter: Option[Submitter],
  effectiveDateTime: Option[LocalDateTime],
  writeOff: Option[WriteOff]
)

/**
  * This helper is only required because the AdditionalDocument domain object is being used by the form mappings in the
  * front end but the form it is being mapped to does not contain all the required fields
  */
object AdditionalDocumentHelper {
  def apply(categoryCode: Option[String],
            typeCode: Option[String],
            id: Option[String],
            lpco: Option[String],
            name: Option[String]): AdditionalDocument = AdditionalDocument(categoryCode, typeCode, id, lpco, name, None, None, None)

  def unapply(arg: AdditionalDocument): Option[(Option[String], Option[String], Option[String], Option[String],
    Option[String])] = Some((arg.categoryCode, arg.typeCode, arg.id, arg.lpco, arg.name))
}

case class AdditionalPaymentType(
  additionalDocPaymentID: Option[String],
  additionalDocPaymentCategory: Option[String],
  additionalDocPaymentType: Option[String]
)

case class AdditionalInformation(
  code: Option[String],
  description: Option[String]
)

case class DocumentationAndReferences(
                                       headerPreviousDocuments: Seq[PreviousDocument],
                                       itemPreviousDocuments: Seq[PreviousDocument],
                                       headerAdditionalInformation: AdditionalInformation,
                                       itemAdditionalInformation: Seq[AdditionalInformation],
                                       additionalDocuments: Seq[AdditionalDocument],
                                       localReferenceNumber: Option[String],
                                       additionalPayments: Seq[AdditionalPaymentType]
)

object DocumentationAndReferences {
  def apply(): DocumentationAndReferences = DocumentationAndReferences(
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
      AdditionalDocument(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), Some("DocumentName1"), None, None, None),
      AdditionalDocument(Some("C"), Some("514"), Some("GBEIR201909014000"), None, None, None, None, None),
      AdditionalDocument(Some("C"), Some("506"), Some("GBDPO1909241"), None, None, None, None, None),
      AdditionalDocument(Some("N"), Some("935"), Some("12345/30.07.2019"), Some("AC"), None, None, None, None),
      AdditionalDocument(Some("N"), Some("935"), Some("12345/30.08.2019"), Some("AC"), None, None, None, None),
      AdditionalDocument(Some("N"), Some("935"), Some("12345/30.09.2019"), Some("AC"), None, None, None, None)
    ),
    Some(UUID.randomUUID().toString.replaceAll("-","").take(20)),
    Seq(
      AdditionalPaymentType(Some("1909241"), Some("1"), Some("DAN")),
      AdditionalPaymentType(Some("1909241"), Some("1"), Some("DAN"))
    )
  )
}
