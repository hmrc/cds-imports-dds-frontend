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

case class DeclarationTypeViewModel(
                            declarationType: String,
                            additionalDeclarationType: String,
                            goodsItemNumber: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/SequenceNumeric = "1" // only 1 item for now
                            totalNumberOfItems: String, // Declaration/GoodsItemQuantity = "1" // only 1 item for now
                            requestedProcedureCode: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/GovernmentProcedure/CurrentCode = "40"
                            previousProcedureCode: String, // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/GovernmentProcedure/PreviousCode = "00"
                            additionalProcedureCode: String // "000 or C07"

                          )

object DeclarationTypeViewModel {
  def apply(): DeclarationTypeViewModel = {
    new DeclarationTypeViewModel(
      "IM",
      "Z",
      "1",
      "1",
      "40",
      "00",
      "000"
    )
  }
}


