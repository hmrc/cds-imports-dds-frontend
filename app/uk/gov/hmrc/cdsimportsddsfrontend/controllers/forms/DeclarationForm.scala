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

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Declaration

object DeclarationForm {

  val nonEmptyString = nonEmptyText.verifying("field.cannot.be.empty", _.nonEmpty) //TODO fix error message

  lazy val form: Form[Declaration] = Form(
    mapping(
      "declarationType" -> nonEmptyString,
      "additionalDeclarationType"-> nonEmptyString,
      "goodsItemNumber"-> nonEmptyString,
      "totalNumberOfItems"-> nonEmptyString,
      "requestedProcedureCode"-> nonEmptyString,
      "previousProcedureCode"-> nonEmptyString,
      "additionalProcedureCode"-> nonEmptyString,

      "previousDocCategory" ->nonEmptyString,
      "previousDocType" ->nonEmptyString,
      "previousDocReference" ->nonEmptyString,
      "previousDocGoodsItemId" ->nonEmptyString,
      "additionalInfoCode" ->nonEmptyString,
      "additionalInfoDescription" ->nonEmptyString,
      "additionalDocCategoryCode" ->nonEmptyString,
      "additionalDocTypeCode" ->nonEmptyString,
      "additionalDocId" ->nonEmptyString,
      "additionalDocLPCO" ->nonEmptyString,
      "additionalDocName" ->nonEmptyString,
      "localReferenceNumber" ->nonEmptyString,
      "additionalDocPaymentID" ->nonEmptyString,
      "additionalDocPaymentCategory" ->nonEmptyString,
      "additionalDocPaymentType" ->nonEmptyString
    )(Declaration.apply)(Declaration.unapply)
  )

}