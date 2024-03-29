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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.DestinationFormMapping.destination
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.AddressFormMapping.addressViewModelMapping
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.WhenAndWhereViewModel

object WhenAndWhereFormMapping  {

  val whenAndWhere: (String, Mapping[WhenAndWhereViewModel]) = "whenAndWhere" -> mapping(
    "destination" -> optional(destination),
    "exportCountry" -> optional(text),
    "originCountryCode" -> optional(text),
    "originTypeCode" -> optional(text),
    "preferentialOriginCountryCode" -> optional(text),
    "preferentialOriginTypeCode" -> optional(text),
    "goodsLocationName" -> optional(text),
    "goodsLocationType" -> optional(text),
    "goodsLocationAddress" -> optional(addressViewModelMapping),
    "placeOfLoading" -> optional(text)
  )(WhenAndWhereViewModel.apply)(WhenAndWhereViewModel.unapply)
}
