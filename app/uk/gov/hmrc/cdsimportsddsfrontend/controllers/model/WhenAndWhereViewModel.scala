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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.model

import uk.gov.hmrc.cdsimportsddsfrontend.domain._

case class WhenAndWhereViewModel(
                        destination: Option[Destination] = Some(Destination()),
                        exportCountry: Option[ExportCountry] = Some(ExportCountry()),
                        origin: Option[Origin] = Some(Origin()),
                        goodsLocation: Option[GoodsLocation] = Some(GoodsLocation(
                          Some("FXTFXTFXT"), Some("A"), Some(Address(
                            streetAndNumber = None, city = None, countryCode = Some("GB"), postcode = None, typeCode = Some("U"))))),
                        placeOfLoading: Option[String] = Some("JFK"))
