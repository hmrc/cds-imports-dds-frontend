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
                                 declarationType: DeclarationTypeViewModel = DeclarationTypeViewModel(),
                                 documentationAndReferences: DocumentationAndReferencesViewModel = DocumentationAndReferencesViewModel(),
                                 parties: DeclarationPartiesViewModel = DeclarationPartiesViewModel(),
                                 valuationInformationAndTaxesViewModel: ValuationInformationAndTaxesViewModel = ValuationInformationAndTaxesViewModel(),
                                 whenAndWhereViewModel: WhenAndWhereViewModel = WhenAndWhereViewModel(),
                                 goodsIdentification: GoodsIdentificationViewModel = GoodsIdentificationViewModel(),
                                 transportInformationViewModel: TransportInformationViewModel = TransportInformationViewModel(),
                                 miscellaneousViewModel: MiscellaneousViewModel = MiscellaneousViewModel()
                      ) {

  lazy val dutyTaxFee = DutyTaxFee(valuationInformationAndTaxesViewModel.dutyRegimeCode,
    miscellaneousViewModel.quotaOrderNumber,
    valuationInformationAndTaxesViewModel.paymentMethodCode.map(Payment))

  lazy val commodity = Some(Commodity(
    goodsMeasure = Some(goodsIdentification.toGoodsMeasure),
    classification = goodsIdentification.toClassification(),
    description = goodsIdentification.description,
    dutyTaxFee = Some(dutyTaxFee),
    invoiceLine = Some(valuationInformationAndTaxesViewModel.toInvoiceLine)))

  lazy val consignment: Consignment = Consignment(
    transportInformationViewModel.container,
    Some(transportInformationViewModel.toArrivalTransportMeans),
    Some(GoodsLocation(
      name = whenAndWhereViewModel.goodsLocationName,
      typeCode = whenAndWhereViewModel.goodsLocationType,
      address = whenAndWhereViewModel.goodsLocationAddress.map(_.toAddress)
    )),
    whenAndWhereViewModel.placeOfLoading.map(LoadingLocation))

  lazy val governmentAgencyGoodsItem = GovernmentAgencyGoodsItem(
    additionalDocuments = combineAdditionalDocsAndWriteOffs(documentationAndReferences.additionalDocuments, miscellaneousViewModel.writeOffViewModels),
    origin = Seq(Origin(countryCode = whenAndWhereViewModel.originCountryCode,
      typeCode = whenAndWhereViewModel.originTypeCode),
      Origin(countryCode = whenAndWhereViewModel.preferentialOriginCountryCode,
        typeCode = whenAndWhereViewModel.preferentialOriginTypeCode)),
    sequenceNumeric = declarationType.goodsItemNumber,
    valuationAdjustment = valuationInformationAndTaxesViewModel.additionCode.map(ValuationAdjustment),
    transactionNatureCode = miscellaneousViewModel.natureOfTransaction,
    statisticalValue = miscellaneousViewModel.statisticalValue
  )

  lazy val goodsShipment = GoodsShipment(consignment = Some(consignment),
    destination = whenAndWhereViewModel.destination,
    exportCountry = whenAndWhereViewModel.exportCountry.map(ExportCountry),
    governmentAgencyGoodsItem = governmentAgencyGoodsItem,
    tradeTerms = Some(TradeTerms(
      valuationInformationAndTaxesViewModel.conditionCode,
      valuationInformationAndTaxesViewModel.locationID,
      valuationInformationAndTaxesViewModel.locationName
    ))
  )

  def toDeclaration: Declaration = {
    Declaration(declarationType = declarationType,
      documentationAndReferences = documentationAndReferences.toDocumentationAndReferences,
      parties = parties,
      currencyExchange = Some(valuationInformationAndTaxesViewModel.toCurrencyExchange),
      totalGrossMassMeasure = goodsIdentification.grossMass,
      commodity = commodity,
      packaging = Some(goodsIdentification.toPackaging),
      borderTransportMeans = Some(transportInformationViewModel.toBorderTransportMeans),
      headerCustomsValuation = Some(valuationInformationAndTaxesViewModel.toHeaderCustomsValuation),
      itemCustomsValuation = Some(valuationInformationAndTaxesViewModel.toItemCustomsValuation),
      goodsShipment = goodsShipment,
      obligationGuarantee = Some(miscellaneousViewModel.toObligationGuarantee)
    )
  }

  private def combineAdditionalDocsAndWriteOffs(
     additionalDocuments: Seq[AdditionalDocumentViewModel],
     writeOffs: Seq[WriteOffViewModel]): Seq[AdditionalDocument] = {
    additionalDocuments.zip(writeOffs).map{ docAndWriteOff =>
      val (additionalDoc, writeOff) = docAndWriteOff
      toAdditionalDocument(additionalDoc, writeOff)}
  }

  private def toAdditionalDocument(doc: AdditionalDocumentViewModel, writeOff: WriteOffViewModel): AdditionalDocument = AdditionalDocument(
    categoryCode = doc.documentCode,
    typeCode = doc.typeCode,
    id = doc.documentIdentifier,
    lpco = doc.documentStatus,
    name = doc.documentStatusReason,
    submitter = writeOff.issuingAuthority.map( issueAuth=> Submitter(name = Some(issueAuth))),
    effectiveDateTime = writeOff.dateOfValidity,
    writeOff = writeOff match {
      case WriteOffViewModel( _, _, None, None) => None
      case _ => Some(WriteOff(quantityQuantity = writeOff.quantity, unitCode = writeOff.measurementUnitAndQualifier))
    }
  )
}
