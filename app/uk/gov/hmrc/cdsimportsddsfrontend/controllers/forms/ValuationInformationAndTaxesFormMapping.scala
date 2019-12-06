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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.ChargeDeductionFormMapping.chargeDeductionMapping
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.CurrencyAmountFormMapping.currencyAmountMapping
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.ValuationInformationAndTaxesViewModel

object ValuationInformationAndTaxesFormMapping  {

  val valuationInformationAndTaxes: (String, Mapping[ValuationInformationAndTaxesViewModel]) = "valuationInformationAndTaxes" -> mapping(
    "conditionCode" -> optional(text), // Declaration/GoodsShipment/TradeTerms/ConditionCode
    "locationID" -> optional(text), // Declaration/GoodsShipment/TradeTerms/LocationID
    "locationName" -> optional(text), // Declaration/GoodsShipment/TradeTerms/LocationName
    "paymentMethodCode" -> optional(text), // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/Commodity/DutyTaxFee/Payment/MethodCode
    "itemChargeDeduction" -> optional(chargeDeductionMapping),
    "additionCode" -> optional(text), // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/ValuationAdjustment/AdditionCode
    "itemChargeAmount" -> optional(currencyAmountMapping),
    "rateNumeric" -> optional(text), // Declaration/CurrencyExchange/RateNumeric
    "customsValuationMethodCode" -> optional(text), // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/CustomsValuation/MethodCode
    "dutyRegimeCode" -> optional(text), // Declaration/GoodsShipment/GovernmentAgencyGoodsItem/Commodity/DutyTaxFee/DutyRegimeCode,
    "headerChargeDeduction" -> optional(chargeDeductionMapping)
  )(ValuationInformationAndTaxesViewModel.apply)(ValuationInformationAndTaxesViewModel.unapply)

}
