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

case class DeclarationViewModel(
                                 declarationType: DeclarationType = DeclarationType(),
                                 documentationType: DocumentationType = DocumentationType(),
                                 parties: DeclarationParties = DeclarationParties(),
                                 valuationInformationAndTaxesViewModel: ValuationInformationAndTaxesViewModel = ValuationInformationAndTaxesViewModel(),
                                 whenAndWhereViewModel: WhenAndWhereViewModel = WhenAndWhereViewModel(),
                                 goodsIdentification: GoodsIdentificationViewModel = GoodsIdentificationViewModel(),
                                 transportInformationViewModel: TransportInformationViewModel = TransportInformationViewModel()
                      ) {

  def toDeclaration: Declaration = {
    val consignment: Consignment = {
      val goodsLocation = whenAndWhereViewModel.goodsLocation
      Consignment(
          transportInformationViewModel.container,
          Some(transportInformationViewModel.toArrivalTransportMeans),
          goodsLocation,
          whenAndWhereViewModel.placeOfLoading.map(LoadingLocation))
    }

    Declaration(declarationType = declarationType,
      documentationType = documentationType,
      parties = parties,
      valuationInformationAndTaxes = valuationInformationAndTaxesViewModel.toValuationInformationAndTaxes,
      totalGrossMassMeasure = goodsIdentification.grossMass,
      commodity = Some(Commodity(
        goodsMeasure = Some(goodsIdentification.toGoodsMeasure),
        classification = goodsIdentification.toClassification(),
        description = goodsIdentification.description)),
      packaging = Some(goodsIdentification.toPackaging),
      borderTransportMeans = Some(transportInformationViewModel.toBorderTransportMeans),
      consignment = Some(consignment),
      headerCustomsValuation = Some(valuationInformationAndTaxesViewModel.toHeaderCustomsValuation),
      itemCustomsValuation = Some(valuationInformationAndTaxesViewModel.toItemCustomsValuation),
      goodsShipment = GoodsShipment(destination = whenAndWhereViewModel.destination,
                                    exportCountry = whenAndWhereViewModel.exportCountry,
                                    governmentAgencyGoodsItem = Some(GovernmentAgencyGoodsItem(origin = whenAndWhereViewModel.origin))
      )
    )
  }
}
