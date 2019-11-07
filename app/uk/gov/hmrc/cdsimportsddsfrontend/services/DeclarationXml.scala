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

package uk.gov.hmrc.cdsimportsddsfrontend.services

import java.util.UUID

import play.twirl.api.{Html, HtmlFormat}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Declaration, Eori}

import scala.xml.{Elem, PrettyPrinter, Text}

object DeclarationXml {

  // This should later build an xml that can be submitted to the declaration API.
  def fromImportDeclaration(eori:Eori, dec: Declaration):Elem = {
    val referenceId = UUID.randomUUID().toString.replaceAll("-","").take(10)

    <md:MetaData xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:md="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2" xmlns:clm63055="urn:un:unece:uncefact:codelist:standard:UNECE:AgencyIdentificationCode:D12B" xmlns:ds="urn:wco:datamodel:WCO:MetaData_DS-DMS:2" xsi:schemaLocation="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2 ../DocumentMetaData_2_DMS.xsd " xmlns="urn:wco:datamodel:WCO:DEC-DMS:2">
      <md:WCODataModelVersionCode>3.6</md:WCODataModelVersionCode>
      <md:WCOTypeName>DEC</md:WCOTypeName>
      <md:ResponsibleCountryCode>GB</md:ResponsibleCountryCode>
      <md:ResponsibleAgencyName>HMRC</md:ResponsibleAgencyName>
      <md:AgencyAssignedCustomizationVersionCode>v2.1</md:AgencyAssignedCustomizationVersionCode>
      <Declaration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:p1="urn:wco:datamodel:WCO:Declaration_DS:DMS:2" xmlns:clm63055="urn:un:unece:uncefact:codelist:standard:UNECE:AgencyIdentificationCode:D12B" xmlns:clm5ISO42173A="urn:un:unece:uncefact:codelist:standard:ISO:ISO3AlphaCurrencyCode:2012-08-31" xsi:schemaLocation="urn:wco:datamodel:WCO:DEC-DMS:2 ../WCO_DEC_2_DMS.xsd " xmlns="urn:wco:datamodel:WCO:DEC-DMS:2">
        <AcceptanceDateTime>
          <p1:DateTimeString formatCode="304">20191101010000+01</p1:DateTimeString>
        </AcceptanceDateTime>
        <FunctionCode>9</FunctionCode>
        <FunctionalReferenceID>{dec.documentationType.localReferenceNumber.getOrElse("")}</FunctionalReferenceID>
        <TypeCode>{dec.declarationType.declarationType+dec.declarationType.additionalDeclarationType}</TypeCode>
        <GoodsItemQuantity>{dec.declarationType.totalNumberOfItems}</GoodsItemQuantity>
        <TotalPackageQuantity>55</TotalPackageQuantity>
        <AdditionalDocument>
          {maybeElement("CategoryCode", dec.documentationType.additionalPayment(0).additionalDocPaymentCategory)}
          {maybeElement("ID", dec.documentationType.additionalPayment(0).additionalDocPaymentID)}
          {maybeElement("TypeCode", dec.documentationType.additionalPayment(0).additionalDocPaymentType)}
        </AdditionalDocument>
        <AdditionalDocument>
          {maybeElement("CategoryCode", dec.documentationType.additionalPayment(1).additionalDocPaymentCategory)}
          {maybeElement("ID", dec.documentationType.additionalPayment(1).additionalDocPaymentID)}
          {maybeElement("TypeCode", dec.documentationType.additionalPayment(1).additionalDocPaymentType)}
        </AdditionalDocument>
        <AdditionalDocument>
          {maybeElement("CategoryCode", dec.documentationType.additionalPayment(2).additionalDocPaymentCategory)}
          {maybeElement("ID", dec.documentationType.additionalPayment(2).additionalDocPaymentID)}
          {maybeElement("TypeCode", dec.documentationType.additionalPayment(2).additionalDocPaymentType)}
        </AdditionalDocument>
        <AdditionalDocument>
          {maybeElement("CategoryCode", dec.documentationType.additionalPayment(3).additionalDocPaymentCategory)}
          {maybeElement("ID", dec.documentationType.additionalPayment(3).additionalDocPaymentID)}
          {maybeElement("TypeCode", dec.documentationType.additionalPayment(3).additionalDocPaymentType)}
        </AdditionalDocument>
        <AdditionalInformation>
          <StatementCode>TSP01</StatementCode>
          <StatementDescription>TSP</StatementDescription>
        </AdditionalInformation>
        <AuthorisationHolder>
          <ID>GB201909014000</ID>
          <CategoryCode>EIR</CategoryCode>
        </AuthorisationHolder>
        <AuthorisationHolder>
          <ID>GB201909014000</ID>
          <CategoryCode>DPO</CategoryCode>
        </AuthorisationHolder>
        <BorderTransportMeans>
          <RegistrationNationalityCode>FR</RegistrationNationalityCode>
          <ModeCode>1</ModeCode>
        </BorderTransportMeans>
        <Declarant>
          <ID>GB201909014000</ID>
        </Declarant>
        <Exporter>
          <Name>French Foil Ltd.</Name>
          <Address>
            <CityName>Paris</CityName>
            <CountryCode>FR</CountryCode>
            <Line>10 Rue Paris</Line>
            <PostcodeID>92311</PostcodeID>
          </Address>
        </Exporter>
        <GoodsShipment>
          <TransactionNatureCode>1</TransactionNatureCode>
          <Consignment>
            <ContainerCode>1</ContainerCode>
            <ArrivalTransportMeans>
              <ID>12345</ID>
              <IdentificationTypeCode>10</IdentificationTypeCode>
            </ArrivalTransportMeans>
            <GoodsLocation>
              <Name>DVRDOVDVR</Name>
              <TypeCode>A</TypeCode>
              <Address>
                <TypeCode>U</TypeCode>
                <CountryCode>GB</CountryCode>
              </Address>
            </GoodsLocation>
            <TransportEquipment>
              <SequenceNumeric>1</SequenceNumeric>
              <ID>DM1234</ID>
            </TransportEquipment>
          </Consignment>
          <Destination>
            <CountryCode>GB</CountryCode>
          </Destination>
          <DomesticDutyTaxParty>
            <ID>GB201909014</ID>
            <RoleCode>FR1</RoleCode>
          </DomesticDutyTaxParty>
          <ExportCountry>
            <ID>FR</ID>
          </ExportCountry>
          <GovernmentAgencyGoodsItem>
            <SequenceNumeric>{dec.declarationType.goodsItemNumber}</SequenceNumeric>
            <AdditionalDocument>
              {maybeElement("CategoryCode", dec.documentationType.additionalDocument(0).categoryCode)}
              {maybeElement("ID", dec.documentationType.additionalDocument(0).id)}
              {maybeElement("Name",dec.documentationType.additionalDocument(0).name)}
              {maybeElement("TypeCode", dec.documentationType.additionalDocument(0).typeCode)}
              {maybeElement("LPCOExemptionCode",dec.documentationType.additionalDocument(0).lpco)}
            </AdditionalDocument>
            <AdditionalDocument>
              {maybeElement("CategoryCode", dec.documentationType.additionalDocument(1).categoryCode)}
              {maybeElement("ID", dec.documentationType.additionalDocument(1).id)}
              {maybeElement("Name",dec.documentationType.additionalDocument(1).name)}
              {maybeElement("TypeCode", dec.documentationType.additionalDocument(1).typeCode)}
              {maybeElement("LPCOExemptionCode",dec.documentationType.additionalDocument(1).lpco)}
            </AdditionalDocument>
            <AdditionalDocument>
              {maybeElement("CategoryCode", dec.documentationType.additionalDocument(2).categoryCode)}
              {maybeElement("ID", dec.documentationType.additionalDocument(2).id)}
              {maybeElement("Name",dec.documentationType.additionalDocument(2).name)}
              {maybeElement("TypeCode", dec.documentationType.additionalDocument(2).typeCode)}
              {maybeElement("LPCOExemptionCode",dec.documentationType.additionalDocument(2).lpco)}
            </AdditionalDocument>
            <AdditionalDocument>
              {maybeElement("CategoryCode", dec.documentationType.additionalDocument(3).categoryCode)}
              {maybeElement("ID", dec.documentationType.additionalDocument(3).id)}
              {maybeElement("Name",dec.documentationType.additionalDocument(3).name)}
              {maybeElement("TypeCode", dec.documentationType.additionalDocument(3).typeCode)}
              {maybeElement("LPCOExemptionCode",dec.documentationType.additionalDocument(3).lpco)}
              <WriteOff>
                <QuantityQuantity unitCode="KGM#G">10</QuantityQuantity>
              </WriteOff>
            </AdditionalDocument>
            <AdditionalInformation>
              <StatementCode>{dec.documentationType.additionalInfoCode.getOrElse("")}</StatementCode>
              <StatementDescription>{dec.documentationType.additionalInfoDescription.getOrElse("")}</StatementDescription>
            </AdditionalInformation>
            <Commodity>
              <Description>Aluminium Foil not exceeding 0,2 mm</Description>
              <Classification>
                <ID>76071111</ID>
                <IdentificationTypeCode>TSP</IdentificationTypeCode>
              </Classification>
              <Classification>
                <ID>10</ID>
                <IdentificationTypeCode>TRC</IdentificationTypeCode>
              </Classification>
              <DutyTaxFee>
                <DutyRegimeCode>{dec.valuationInformationAndTaxes.dutyRegimeCode.getOrElse("")}</DutyRegimeCode>
                <Payment>
                  <MethodCode>{dec.valuationInformationAndTaxes.paymentMethodCode.getOrElse("")}</MethodCode>
                </Payment>
              </DutyTaxFee>
              <GoodsMeasure>
                <GrossMassMeasure unitCode="KGM">90000100</GrossMassMeasure>
                <NetNetWeightMeasure unitCode="KGM">90000000</NetNetWeightMeasure>
                <TariffQuantity>90000000</TariffQuantity>
              </GoodsMeasure>
              <InvoiceLine>
                <ItemChargeAmount currencyID={dec.valuationInformationAndTaxes.currencyID.getOrElse("GBP").toUpperCase()}>{dec.valuationInformationAndTaxes.itemChargeAmount.getOrElse("")}</ItemChargeAmount>
              </InvoiceLine>
            </Commodity>
            <CustomsValuation>
              <MethodCode>{dec.valuationInformationAndTaxes.customsValuationMethodCode.getOrElse("")}</MethodCode>
            </CustomsValuation>
            <GovernmentProcedure>
              <CurrentCode>{dec.declarationType.requestedProcedureCode}</CurrentCode>
              <PreviousCode>{dec.declarationType.previousProcedureCode}</PreviousCode>
            </GovernmentProcedure>
            <GovernmentProcedure>
              <CurrentCode>{dec.declarationType.additionalProcedureCode}</CurrentCode>
            </GovernmentProcedure>
            <Origin>
              <CountryCode>FR</CountryCode>
              <TypeCode>1</TypeCode>
            </Origin>
            <Packaging>
              <SequenceNumeric>1</SequenceNumeric>
              <MarksNumbersID>PK/12344</MarksNumbersID>
              <QuantityQuantity>55</QuantityQuantity>
              <TypeCode>PK</TypeCode>
            </Packaging>
            <PreviousDocument>
              <CategoryCode>Y</CategoryCode>
              <ID>20191101</ID>
              <TypeCode>CLE</TypeCode>
              <LineNumeric>1</LineNumeric>
            </PreviousDocument>
            <PreviousDocument>
              <CategoryCode>{dec.documentationType.previousDocCategory.getOrElse("")}</CategoryCode>
              <ID>{dec.documentationType.previousDocReference.getOrElse("")}</ID>
              <TypeCode>{dec.documentationType.previousDocType.getOrElse("")}</TypeCode>
              <LineNumeric>{dec.documentationType.previousDocGoodsItemId.getOrElse("")}</LineNumeric>
            </PreviousDocument>
            <ValuationAdjustment>
              <AdditionCode>{dec.valuationInformationAndTaxes.additionCode.getOrElse("")}</AdditionCode>
            </ValuationAdjustment>
          </GovernmentAgencyGoodsItem>
          <Importer>
            <ID>GB201909014000</ID>
          </Importer>
          <PreviousDocument>
            <CategoryCode>Y</CategoryCode>
            <ID>20191101</ID>
            <TypeCode>CLE</TypeCode>
            <LineNumeric>1</LineNumeric>
          </PreviousDocument>
          <PreviousDocument>
            <CategoryCode>Y</CategoryCode>
            <ID>9GB201909014000</ID>
            <TypeCode>DCR</TypeCode>
            <LineNumeric>1</LineNumeric>
          </PreviousDocument>
          <TradeTerms>
            <ConditionCode>{dec.valuationInformationAndTaxes.conditionCode.getOrElse("")}</ConditionCode>
            <LocationID>{dec.valuationInformationAndTaxes.locationID.getOrElse("")}</LocationID>
            <LocationName>{dec.valuationInformationAndTaxes.locationName.getOrElse("")}</LocationName>
          </TradeTerms>
          <UCR>
            <TraderAssignedReferenceID>{dec.documentationType.previousDocReference.getOrElse("")}-12345</TraderAssignedReferenceID>
          </UCR>
        </GoodsShipment>
        <CurrencyExchange>
          <RateNumeric>{dec.valuationInformationAndTaxes.rateNumeric.getOrElse("")}</RateNumeric>
        </CurrencyExchange>
      </Declaration>
    </md:MetaData>

  }

  private def maybeElement(name: String, maybeValue: Option[String]) = {
    if (maybeValue.exists(_.trim.nonEmpty))
      Elem.apply(null, name, scala.xml.Null, scala.xml.TopScope, true, Text(maybeValue.getOrElse("").trim))
  }

  //Turn a scala xml document into a fully escaped html string
  def prettyPrintToHtml(xml:Elem):String = {
    val prettyPrinter = new PrettyPrinter(250,4)
    val xmlString = prettyPrinter.format(xml)
    HtmlFormat.escape(xmlString).toString()
      .replaceAll("[\r\n]", "<br/>")
      .replaceAll("  ", "&nbsp;&nbsp;")
  }



  def goodDeclaration():Elem = {
    val referenceId = UUID.randomUUID().toString.replaceAll("-","").take(10)

    <md:MetaData xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:md="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2" xmlns:clm63055="urn:un:unece:uncefact:codelist:standard:UNECE:AgencyIdentificationCode:D12B" xmlns:ds="urn:wco:datamodel:WCO:MetaData_DS-DMS:2" xsi:schemaLocation="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2 ../DocumentMetaData_2_DMS.xsd " xmlns="urn:wco:datamodel:WCO:DEC-DMS:2">
      <md:WCODataModelVersionCode>3.6</md:WCODataModelVersionCode>
      <md:WCOTypeName>DEC</md:WCOTypeName>
      <md:ResponsibleCountryCode>GB</md:ResponsibleCountryCode>
      <md:ResponsibleAgencyName>HMRC</md:ResponsibleAgencyName>
      <md:AgencyAssignedCustomizationVersionCode>v2.1</md:AgencyAssignedCustomizationVersionCode>
      <Declaration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:6" xmlns:p1="urn:wco:datamodel:WCO:Declaration_DS:DMS:2" xmlns:clm63055="urn:un:unece:uncefact:codelist:standard:UNECE:AgencyIdentificationCode:D12B" xmlns:clm5ISO42173A="urn:un:unece:uncefact:codelist:standard:ISO:ISO3AlphaCurrencyCode:2012-08-31" xsi:schemaLocation="urn:wco:datamodel:WCO:DEC-DMS:2 ../WCO_DEC_2_DMS.xsd " xmlns="urn:wco:datamodel:WCO:DEC-DMS:2">
        <AcceptanceDateTime>
          <p1:DateTimeString formatCode="304">20191101010000+01</p1:DateTimeString>
        </AcceptanceDateTime>
        <FunctionCode>9</FunctionCode>
        <FunctionalReferenceID>R251_TC14_129</FunctionalReferenceID>
        <TypeCode>IMZ</TypeCode>
        <GoodsItemQuantity>1</GoodsItemQuantity>
        <TotalPackageQuantity>55</TotalPackageQuantity>
        <AdditionalDocument>
          <CategoryCode>1</CategoryCode>
          <ID>1909241</ID>
          <TypeCode>DAN</TypeCode>
        </AdditionalDocument>
        <AdditionalInformation>
          <StatementCode>TSP01</StatementCode>
          <StatementDescription>TSP</StatementDescription>
        </AdditionalInformation>
        <AuthorisationHolder>
          <ID>GB201909014000</ID>
          <CategoryCode>EIR</CategoryCode>
        </AuthorisationHolder>
        <AuthorisationHolder>
          <ID>GB201909014000</ID>
          <CategoryCode>DPO</CategoryCode>
        </AuthorisationHolder>
        <BorderTransportMeans>
          <RegistrationNationalityCode>FR</RegistrationNationalityCode>
          <ModeCode>1</ModeCode>
        </BorderTransportMeans>
        <Declarant>
          <ID>GB201909014000</ID>
        </Declarant>
        <Exporter>
          <Name>French Foil Ltd.</Name>
          <Address>
            <CityName>Paris</CityName>
            <CountryCode>FR</CountryCode>
            <Line>10 Rue Paris</Line>
            <PostcodeID>92311</PostcodeID>
          </Address>
        </Exporter>
        <GoodsShipment>
          <TransactionNatureCode>1</TransactionNatureCode>
          <Consignment>
            <ContainerCode>1</ContainerCode>
            <ArrivalTransportMeans>
              <ID>12345</ID>
              <IdentificationTypeCode>10</IdentificationTypeCode>
            </ArrivalTransportMeans>
            <GoodsLocation>
              <Name>DVRDOVDVR</Name>
              <TypeCode>A</TypeCode>
              <Address>
                <TypeCode>U</TypeCode>
                <CountryCode>GB</CountryCode>
              </Address>
            </GoodsLocation>
            <TransportEquipment>
              <SequenceNumeric>1</SequenceNumeric>
              <ID>DM1234</ID>
            </TransportEquipment>
          </Consignment>
          <Destination>
            <CountryCode>GB</CountryCode>
          </Destination>
          <DomesticDutyTaxParty>
            <ID>GB201909014</ID>
            <RoleCode>FR1</RoleCode>
          </DomesticDutyTaxParty>
          <ExportCountry>
            <ID>FR</ID>
          </ExportCountry>
          <GovernmentAgencyGoodsItem>
            <SequenceNumeric>1</SequenceNumeric>
            <AdditionalDocument>
              <CategoryCode>C</CategoryCode>
              <ID>GBEIR201909014000</ID>
              <TypeCode>514</TypeCode>
            </AdditionalDocument>
            <AdditionalDocument>
              <CategoryCode>C</CategoryCode>
              <ID>GBDPO1909241</ID>
              <TypeCode>506</TypeCode>
            </AdditionalDocument>
            <AdditionalDocument>
              <CategoryCode>N</CategoryCode>
              <ID>12345/30.09.2019</ID>
              <TypeCode>935</TypeCode>
              <LPCOExemptionCode>AC</LPCOExemptionCode>
            </AdditionalDocument>
            <AdditionalDocument>
              <CategoryCode>I</CategoryCode>
              <ID>GBCPI000001-0001</ID>
              <TypeCode>004</TypeCode>
              <LPCOExemptionCode>AE</LPCOExemptionCode>
              <WriteOff>
                <QuantityQuantity unitCode="KGM#G">10</QuantityQuantity>
              </WriteOff>
            </AdditionalDocument>
            <AdditionalInformation>
              <StatementCode>00500</StatementCode>
              <StatementDescription>IMPORTER</StatementDescription>
            </AdditionalInformation>
            <Commodity>
              <Description>Aluminium Foil not exceeding 0,2 mm</Description>
              <Classification>
                <ID>76071111</ID>
                <IdentificationTypeCode>TSP</IdentificationTypeCode>
              </Classification>
              <Classification>
                <ID>10</ID>
                <IdentificationTypeCode>TRC</IdentificationTypeCode>
              </Classification>
              <DutyTaxFee>
                <DutyRegimeCode>100</DutyRegimeCode>
                <Payment>
                  <MethodCode>E</MethodCode>
                </Payment>
              </DutyTaxFee>
              <GoodsMeasure>
                <GrossMassMeasure unitCode="KGM">90000100</GrossMassMeasure>
                <NetNetWeightMeasure unitCode="KGM">90000000</NetNetWeightMeasure>
                <TariffQuantity>90000000</TariffQuantity>
              </GoodsMeasure>
              <InvoiceLine>
                <ItemChargeAmount currencyID="GBP">90500000</ItemChargeAmount>
              </InvoiceLine>
            </Commodity>
            <CustomsValuation>
              <MethodCode>1</MethodCode>
            </CustomsValuation>
            <GovernmentProcedure>
              <CurrentCode>40</CurrentCode>
              <PreviousCode>00</PreviousCode>
            </GovernmentProcedure>
            <GovernmentProcedure>
              <CurrentCode>000</CurrentCode>
            </GovernmentProcedure>
            <Origin>
              <CountryCode>FR</CountryCode>
              <TypeCode>1</TypeCode>
            </Origin>
            <Packaging>
              <SequenceNumeric>1</SequenceNumeric>
              <MarksNumbersID>PK/12344</MarksNumbersID>
              <QuantityQuantity>55</QuantityQuantity>
              <TypeCode>PK</TypeCode>
            </Packaging>
            <PreviousDocument>
              <CategoryCode>Y</CategoryCode>
              <ID>20191101</ID>
              <TypeCode>CLE</TypeCode>
              <LineNumeric>1</LineNumeric>
            </PreviousDocument>
            <PreviousDocument>
              <CategoryCode>Y</CategoryCode>
              <ID>9GB201909014000</ID>
              <TypeCode>DCR</TypeCode>
              <LineNumeric>1</LineNumeric>
            </PreviousDocument>
            <ValuationAdjustment>
              <AdditionCode>0000</AdditionCode>
            </ValuationAdjustment>
          </GovernmentAgencyGoodsItem>
          <Importer>
            <ID>GB201909014000</ID>
          </Importer>
          <PreviousDocument>
            <CategoryCode>Y</CategoryCode>
            <ID>20191101</ID>
            <TypeCode>CLE</TypeCode>
            <LineNumeric>1</LineNumeric>
          </PreviousDocument>
          <PreviousDocument>
            <CategoryCode>Y</CategoryCode>
            <ID>9GB201909014000</ID>
            <TypeCode>DCR</TypeCode>
            <LineNumeric>1</LineNumeric>
          </PreviousDocument>
          <TradeTerms>
            <ConditionCode>CFR</ConditionCode>
            <LocationID>GBDVR</LocationID>
          </TradeTerms>
          <UCR>
            <TraderAssignedReferenceID>9GB201909014000-12345</TraderAssignedReferenceID>
          </UCR>
        </GoodsShipment>
      </Declaration>
    </md:MetaData>
  }

}
