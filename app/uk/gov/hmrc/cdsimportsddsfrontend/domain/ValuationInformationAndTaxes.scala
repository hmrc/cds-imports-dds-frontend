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

case class ValuationInformationAndTaxes(
                                         conditionCode: Option[String] = Some("CFR"),
                                         locationID: Option[String] = Some("GBDVR"),
                                         locationName: Option[String] = Some(""),
                                         paymentMethodCode: Option[String] = Some("E"),
                                         additionCode: Option[String] = Some("0000"),
                                         itemChargeAmount: Option[String] = Some("100"),
                                         currencyID: Option[String] = Some("GBP"),
                                         rateNumeric: Option[String] = Some("1.27"),
                                         dutyRegimeCode: Option[String] = Some("100")
                                       )
