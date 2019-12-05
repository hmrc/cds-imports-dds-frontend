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
                        declarationType: DeclarationType = DeclarationType(),
                        documentationAndReferences: DocumentationAndReferences = DocumentationAndReferences(),
                        parties: DeclarationParties = DeclarationParties(),
                        valuationInformationAndTaxes: ValuationInformationAndTaxes = ValuationInformationAndTaxes(),
                        commodity: Option[Commodity] = None,
                        totalGrossMassMeasure: Option[String] = None,
                        packaging: Option[Packaging] = None,
                        borderTransportMeans: Option[BorderTransportMeans] = None,
                        headerCustomsValuation: Option[HeaderCustomsValuation] = None,
                        itemCustomsValuation: Option[ItemCustomsValuation] = None,
                        goodsShipment: GoodsShipment = GoodsShipment(consignment = None,
                                                                     destination = None,
                                                                     exportCountry = None,
                                                                     governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(
                                                                       origin = Seq(),
                                                                       sequenceNumeric = "1",
                                                                       valuationAdjustment = None
                                                                     )),
                        obligationGuarantee: Option[ObligationGuarantee] = None
                      )
