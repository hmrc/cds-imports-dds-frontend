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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.CurrencyAmountFormMapping.currencyAmountMapping
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.MiscellaneousViewModel

object MiscellaneousFormMapping {
  val miscellaneous: (String, Mapping[MiscellaneousViewModel]) = "miscellaneous" -> mapping(
    "quotaOrderNumber" -> optional(text),
    "guaranteeType" -> optional(text),
    "grn" -> optional(text),
    "otherGRN" -> optional(text),
    "accessCode" -> optional(text),
    "importDutyAndOtherCharges" -> optional(currencyAmountMapping),
    "customsOffice" -> optional(text)
  )(MiscellaneousViewModel.apply)(MiscellaneousViewModel.unapply)
}
