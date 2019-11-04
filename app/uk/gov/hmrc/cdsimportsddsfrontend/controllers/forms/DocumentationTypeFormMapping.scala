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

import play.api.data.Forms.{mapping, optional, text}
import play.api.data.Mapping
import uk.gov.hmrc.cdsimportsddsfrontend.domain.DocumentationType

object DocumentationTypeFormMapping {
  val documentationType: (String, Mapping[DocumentationType]) = "documentationType" -> mapping(
    "previousDocCategory" -> optional(text),
    "previousDocType" -> optional(text),
    "previousDocReference" -> optional(text),
    "previousDocGoodsItemId" -> optional(text),
    "additionalInfoCode" -> optional(text),
    "additionalInfoDescription" -> optional(text),
    "additionalDocCategoryCode" -> optional(text),
    "additionalDocTypeCode" -> optional(text),
    "additionalDocId" -> optional(text),
    "additionalDocLPCO" -> optional(text),
    "additionalDocName" -> optional(text),
    "localReferenceNumber" -> optional(text),
    "additionalDocPaymentID" -> optional(text),
    "additionalDocPaymentCategory" -> optional(text),
    "additionalDocPaymentType" -> optional(text)
  )(DocumentationType.apply)(DocumentationType.unapply)
}