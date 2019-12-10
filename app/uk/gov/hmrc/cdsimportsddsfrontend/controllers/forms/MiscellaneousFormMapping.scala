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

import play.api.data.Forms.{mapping, optional, seq, text}
import play.api.data.Mapping
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.CurrencyAmountFormMapping.currencyAmountMapping
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.{MiscellaneousViewModel, WriteOffViewModel}

object WriteOffFormMapping {
  val writeOffMapper: (String, Mapping[Seq[WriteOffViewModel]]) = "writeOff" -> seq(mapping(
    "issuingAuthority" -> optional(text),
    "dateOfValidity" -> optional(text),
    "quantity" -> optional(text),
    "measurementUnitAndQuantity" -> optional(text)
  )(WriteOffViewModel.apply)(WriteOffViewModel.unapply))
}

object MiscellaneousFormMapping {
  import WriteOffFormMapping.writeOffMapper
  val miscellaneous: (String, Mapping[MiscellaneousViewModel]) = "miscellaneous" -> mapping(
    "guaranteeType" -> optional(text),
    "grn" -> optional(text),
    "otherGRN" -> optional(text),
    "accessCode" -> optional(text),
    "importDutyAndOtherCharges" -> optional(currencyAmountMapping),
    "customsOffice" -> optional(text),
    "natureOfTransaction" -> optional(text),
    "statisticalValue" -> optional(currencyAmountMapping),
    writeOffMapper
  )(MiscellaneousViewModel.apply)(MiscellaneousViewModel.unapply)
}