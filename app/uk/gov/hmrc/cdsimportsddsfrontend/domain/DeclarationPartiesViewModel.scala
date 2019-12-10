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

case class DeclarationPartiesViewModel(
                               declarant: Option[Party] = Some(Party(identifier = Some("GB201909014000"))),
                               importer: Option[Party] = Some(Party(name = Some("Foil R Us"), address = Some(Address(
                                 Some("82 Boulevard de Clichy"), Some("Paris"), Some("FR"), Some("75018"), Some("U")
                               )))),
                               exporter: Option[Party] = Some(Party(identifier = Some("GB025115155321"))),
                               buyer: Option[Party] = None,
                               seller: Option[Party] = None,
                               authorisationHolders: Seq[AuthorisationHolder] = Seq(AuthorisationHolder(Some("GB201909014000"), Some("EIR")),
                                                                                    AuthorisationHolder(Some("GB201909014000"), Some("DPO"))),
                               domesticDutyTaxParties: Seq[DomesticDutyTaxParty] = Seq()
                             )
