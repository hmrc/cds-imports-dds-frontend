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

case class DocumentationType(
  previousDocCategory:  Option[String],
  previousDocType: Option[String],
  previousDocReference: Option[String],
  previousDocGoodsItemId: Option[String],
  additionalInfoCode: Option[String],
  additionalInfoDescription: Option[String],
  additionalDocCategoryCode: Option[String],
  additionalDocTypeCode: Option[String],
  additionalDocId: Option[String],
  additionalDocLPCO: Option[String],
  additionalDocName: Option[String],
  localReferenceNumber: Option[String],
  additionalDocPaymentID: Option[String],
  additionalDocPaymentCategory: Option[String],
  additionalDocPaymentType: Option[String]
)

object DocumentationType {
  def apply(): DocumentationType =
    DocumentationType(
      Some("Y"),
      Some("DCR"),
      Some("9GB201909014000"),
      Some("1"),
      Some("00500"),
      Some("IMPORTER"),
      Some("N"),
      Some("935"),
      Some("12345/30.09.2019"),
      Some("AC"),
      Some("DocumentName"),
      Some("Test1234"),
      Some("1909241"),
      Some("1"),
      Some("DAN")
    )
}
