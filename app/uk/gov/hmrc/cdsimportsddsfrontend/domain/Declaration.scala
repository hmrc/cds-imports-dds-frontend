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

case class Declaration(
                        header: Header,
                        goodsItemNumber: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/SequenceNumeric = "1" // only 1 item for now
                        totalNumberOfItems: String, // Declaration/GoodsItemQuantity = "1" // only 1 item for now
                        requestedProcedureCode: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/GovernmentProcedure/CurrentCode = "40"
                        previousProcedureCode: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/GovernmentProcedure/PreviousCode = "00"
                        additionalProcedureCode: String, // "000 or C07"
                        previousDocCategory: String,
                        previousDocType: String,
                        previousDocReference: String,
                        previousDocGoodsItemId: String,
                        additionalInfoCode: String,
                        additionalInfoDescription: String,
                        additionalDocCategoryCode: String,
                        additionalDocTypeCode: String,
                        additionalDocId: String,
                        additionalDocLPCO: String,
                        additionalDocName: String,
                        localReferenceNumber: String,
                        additionalDocPaymentID: String,
                        additionalDocPaymentCategory: String,
                        additionalDocPaymentType: String
                      )

object Declaration {
  def apply(): Declaration = {
    new Declaration(
      Header(),
      "1",
      "1",
      "40",
      "00",
      "000",
      "Y",
      "DCR",
      "9GB201909014000",
      "1",
      "00500",
      "IMPORTER",
      "N",
      "935",
      "12345/30.09.2019",
      "AC",
      "DocumentName",
      "Test1234",
      "1909241",
      "1",
      "DAN"
    )
  }
}
