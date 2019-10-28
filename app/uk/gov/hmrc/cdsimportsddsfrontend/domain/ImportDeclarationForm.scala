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

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.SubmitDeclarationModel
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.SubmitDeclarationModel.{FieldName, verifyXML}

case class ImportDeclarationForm(
                            declarationType: String, // Declaration/TypeCode = "IM"
                            additionalDeclarationType: String, // Declaration/TypeCode = "Z"
                            goodsItemNumber: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/SequenceNumeric = "1" // only 1 item for now
                            totalNumberOfItems: String, // Declaration/GoodsItemQuantity = "1" // only 1 item for now
                            requestedProcedureCode: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/GovernmentProcedure/CurrentCode = "40"
                            previousProcedureCode: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/GovernmentProcedure/PreviousCode = "00"
                            additionalProcedureCode: String, // "000 or C07"
                            previousDocCategory:String,
                            previousDocType:String,
                            previousDocReference:String,
                            previousDocGoodsItemId:String,
                            additionalInfoCode:String,
                            additionalInfoDescription:String,
                            additionalDocCategoryCode:String,
                            additionalDocTypeCode:String,
                            additionalDocId:String,
                            additionalDocLPCO:String,
                            additionalDocName:String
                            )

object ImportDeclarationForm {
  def apply():ImportDeclarationForm = {
    new ImportDeclarationForm("IM",
      "Z",
      "1",
      "1" ,
      "40",
      "00",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      ""
    )
  }

  val nonEmptyString = nonEmptyText.verifying("field.cannot.be.empty", _.nonEmpty)

  val form: Form[ImportDeclarationForm] = Form(
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
      "additionalDocName" ->nonEmptyString
    )(ImportDeclarationForm.apply)(ImportDeclarationForm.unapply)
  )

}
