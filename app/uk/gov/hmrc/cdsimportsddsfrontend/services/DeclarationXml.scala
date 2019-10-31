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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Eori, ImportDeclarationForm}

import scala.xml.{Elem, PrettyPrinter}

object DeclarationXml {

  // This should later build an xml that can be submitted to the declaration API.
  def fromImportDeclaration(eori:Eori, dec: ImportDeclarationForm):Elem = {
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
        <FunctionalReferenceID>{dec.localReferenceNumber}</FunctionalReferenceID>
        <TypeCode>{dec.declarationType+dec.additionalDeclarationType}</TypeCode>
        <GoodsItemQuantity>{dec.totalNumberOfItems}</GoodsItemQuantity>
        <TotalPackageQuantity>55</TotalPackageQuantity>
        <AdditionalDocument>
          <CategoryCode>{dec.additionalDocPaymentCategory}</CategoryCode>
          <ID>{dec.additionalDocPaymentID}</ID>
          <TypeCode>{dec.additionalDocPaymentType}</TypeCode>
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
            <SequenceNumeric>{dec.goodsItemNumber}</SequenceNumeric>
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
              <CategoryCode>{dec.additionalDocCategoryCode}</CategoryCode>
              <ID>{dec.additionalDocId}</ID>
              <Name>{dec.additionalDocName}</Name>
              <TypeCode>{dec.additionalDocTypeCode}</TypeCode>
              <LPCOExemptionCode>{dec.additionalDocLPCO}</LPCOExemptionCode>
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
              <StatementCode>{dec.additionalInfoCode}</StatementCode>
              <StatementDescription>{dec.additionalInfoDescription}</StatementDescription>
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
              <CurrentCode>{dec.requestedProcedureCode}</CurrentCode>
              <PreviousCode>{dec.previousProcedureCode}</PreviousCode>
            </GovernmentProcedure>
            <GovernmentProcedure>
              <CurrentCode>{dec.additionalProcedureCode}</CurrentCode>
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
              <CategoryCode>{dec.previousDocCategory}</CategoryCode>
              <ID>{dec.previousDocReference}</ID>
              <TypeCode>{dec.previousDocType}</TypeCode>
              <LineNumeric>{dec.previousDocGoodsItemId}</LineNumeric>
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
            <TraderAssignedReferenceID>{dec.previousDocReference}-12345</TraderAssignedReferenceID>
          </UCR>
        </GoodsShipment>
      </Declaration>
    </md:MetaData>

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
