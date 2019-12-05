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

package uk.gov.hmrc.cdsimportsddsfrontend.services.xml

import java.util.UUID

import javax.inject.Singleton
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.cdsimportsddsfrontend.domain._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.AddressXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.BorderTransportMeansXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ClassificationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ConsignmentXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.DestinationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.DutyTaxFeeXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ExportCountryXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.GoodsMeasureXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.HeaderCustomsValuationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ItemCustomsValuationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ObligationGuaranteeXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.OriginXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.PackagingXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ValuationAdjustmentXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances._

import scala.xml.{Attribute, Elem, MetaData, NodeSeq, PrettyPrinter, Text}

@Singleton
class DeclarationXml {

  def fromImportDeclaration(dec: Declaration):Elem = {

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
        <FunctionalReferenceID>{dec.documentationAndReferences.localReferenceNumber.getOrElse("")}</FunctionalReferenceID>
        <TypeCode>{dec.declarationType.declarationType + dec.declarationType.additionalDeclarationType}</TypeCode>
        <GoodsItemQuantity>{dec.declarationType.totalNumberOfItems}</GoodsItemQuantity>
        {maybeElement("TotalGrossMassMeasure", dec.totalGrossMassMeasure)}
        <TotalPackageQuantity>1</TotalPackageQuantity>
        <AdditionalDocument>
        {maybeElement("CategoryCode", dec.documentationAndReferences.additionalPayment(0).additionalDocPaymentCategory)}
        {maybeElement("ID", dec.documentationAndReferences.additionalPayment(0).additionalDocPaymentID)}
        {maybeElement("TypeCode", dec.documentationAndReferences.additionalPayment(0).additionalDocPaymentType)}
        </AdditionalDocument>
        <AdditionalDocument>
          {maybeElement("CategoryCode", dec.documentationAndReferences.additionalPayment(1).additionalDocPaymentCategory)}
          {maybeElement("ID", dec.documentationAndReferences.additionalPayment(1).additionalDocPaymentID)}
          {maybeElement("TypeCode", dec.documentationAndReferences.additionalPayment(1).additionalDocPaymentType)}
        </AdditionalDocument>
        {additionalInformation(dec.documentationAndReferences.headerAdditionalInformation)}
        {dec.parties.authorisationHolders.map(_.toXml)}
        {dec.borderTransportMeans.toXml}
        {maybeCurrencyExchange(dec)}
        {maybeParty("Declarant", dec.parties.declarant)}
        {maybeParty("Exporter", dec.parties.exporter)}
        <GoodsShipment>
          {maybeParty("Buyer", dec.parties.buyer)}
          {dec.goodsShipment.consignment.toXml}
          {dec.headerCustomsValuation.toXml}
          {dec.goodsShipment.destination.toXml}
          {dec.goodsShipment.exportCountry.toXml}
          <GovernmentAgencyGoodsItem>
            <SequenceNumeric>{dec.goodsShipment.governmentAgencyGoodsItem.sequenceNumeric}</SequenceNumeric>
            {dec.goodsShipment.governmentAgencyGoodsItem.statisticalValue.toXml(statisticalValueAmountWriter)}
            {maybeElement("TransactionNatureCode", dec.goodsShipment.governmentAgencyGoodsItem.transactionNatureCode)}
            {dec.documentationAndReferences.additionalDocument.map(_.toXml)}
            {dec.documentationAndReferences.itemAdditionalInformation.map(additionalInformation)}
            <Commodity>
              {maybeElement("Description", dec.commodity.flatMap(_.description))}
              {dec.commodity.map(_.classification.map(_.toXml)).getOrElse(NodeSeq.Empty)}
              {dec.commodity.flatMap(_.dutyTaxFee).map(_.toXml).getOrElse(NodeSeq.Empty)}
              {dec.commodity.flatMap(c => c.goodsMeasure).flatMap(_.toXmlOption).getOrElse(NodeSeq.Empty)}
              {maybeInvoiceLine(dec)}
            </Commodity>
            {dec.itemCustomsValuation.toXml}
            {dec.parties.domesticDutyTaxParties.map(_.toXml)}
            <GovernmentProcedure>
              <CurrentCode>{dec.declarationType.requestedProcedureCode}</CurrentCode>
              <PreviousCode>{dec.declarationType.previousProcedureCode}</PreviousCode>
            </GovernmentProcedure>
            <GovernmentProcedure>
              <CurrentCode>{dec.declarationType.additionalProcedureCode}</CurrentCode>
            </GovernmentProcedure>
            {dec.goodsShipment.governmentAgencyGoodsItem.origin.map(_.toXml)}
            {dec.packaging.toXml}
            {dec.documentationAndReferences.itemPreviousDocuments.map(_.toXml)}
            {dec.goodsShipment.governmentAgencyGoodsItem.valuationAdjustment.toXml}
          </GovernmentAgencyGoodsItem>
          {maybeParty("Importer", dec.parties.importer)}
          {dec.documentationAndReferences.headerPreviousDocuments.map(_.toXml)}
          {maybeParty("Seller", dec.parties.seller)}
          {maybeTradeTerms(dec)}
          <UCR>
            <TraderAssignedReferenceID>1-12345</TraderAssignedReferenceID>
          </UCR>
        </GoodsShipment>
        {dec.obligationGuarantee.toXml}
      </Declaration>
    </md:MetaData>
  }

  private[this] def maybeTradeTerms(declaration: Declaration): NodeSeq = {
    if (declaration.valuationInformationAndTaxes.conditionCode.exists(_.nonEmpty) ||
        declaration.valuationInformationAndTaxes.locationID.exists(_.nonEmpty) ||
        declaration.valuationInformationAndTaxes.locationName.exists(_.nonEmpty)) {
        <TradeTerms>
          {maybeElement("ConditionCode", declaration.valuationInformationAndTaxes.conditionCode)}
          {maybeElement("LocationID", declaration.valuationInformationAndTaxes.locationID)}
          {maybeElement("LocationName", declaration.valuationInformationAndTaxes.locationName)}
        </TradeTerms>
    } else {
      NodeSeq.Empty
    }
  }

  private[this] def maybeElement(elementName: String, maybeElementValue: Option[String], attribute: Option[Attribute] = None): NodeSeq = {
    maybeElementValue match {
      case Some(value) if value.nonEmpty =>
        val attributes: MetaData = attribute.getOrElse(scala.xml.Null)
        Elem.apply(null, elementName, attributes, scala.xml.TopScope, true, Text(value)) //scalastyle:ignore
      case _ => NodeSeq.Empty
    }
  }

  private[this] def maybeInvoiceLine(declaration: Declaration): NodeSeq = {
    if (declaration.valuationInformationAndTaxes.currencyID.exists(_.nonEmpty) ||
       declaration.valuationInformationAndTaxes.itemChargeAmount.exists(_.nonEmpty)) {
      val currencyId = declaration.valuationInformationAndTaxes.currencyID.getOrElse("GBP").toUpperCase()
      val itemChargeAmount = declaration.valuationInformationAndTaxes.itemChargeAmount.getOrElse("")
      <InvoiceLine>
        <ItemChargeAmount currencyID={currencyId}>{itemChargeAmount}</ItemChargeAmount>
      </InvoiceLine>
    } else {
      NodeSeq.Empty
    }
  }

  private[this] def maybeCurrencyExchange(declaration: Declaration): NodeSeq = {
    if (declaration.valuationInformationAndTaxes.rateNumeric.exists(_.nonEmpty)) {
      <CurrencyExchange>
        {maybeElement("RateNumeric", declaration.valuationInformationAndTaxes.rateNumeric)}
      </CurrencyExchange>
    } else {
      NodeSeq.Empty
    }
  }

  private[this] def maybeParty(tagName: String, party: Option[Party]): NodeSeq = {
    party match {
      case Some(party) =>
        val childNodes =
          maybeElement("Name", party.name) ++
          maybeElement("ID", party.identifier) ++
          party.address.flatMap(_.toXmlOption).getOrElse(NodeSeq.Empty) ++
          maybePhoneNumber(party)
        Elem.apply(null, tagName, scala.xml.Null, scala.xml.TopScope, true, childNodes :_*) // scalastyle:ignore
      case None => NodeSeq.Empty
    }
  }

  private[this] def maybePhoneNumber(party: Party): NodeSeq = {
    party.phoneNumber match {
      case Some(phoneNumber) =>
        <Communication>
          <ID>{phoneNumber}</ID>
        </Communication>
      case None => NodeSeq.Empty
    }
  }

  private[this] def additionalInformation(additionalInformation: AdditionalInformation): NodeSeq = {
    additionalInformation match {
      case AdditionalInformation(None, None) => NodeSeq.Empty
      case _ =>
        <AdditionalInformation>
          {maybeElement("StatementCode", additionalInformation.code)}
          {maybeElement("StatementDescription", additionalInformation.description)}
        </AdditionalInformation>
    }
  }

}

object DeclarationXml {

  def apply(): DeclarationXml = new DeclarationXml()

  // Turn a scala xml document into a fully escaped html string
  def prettyPrintToHtml(xml:Elem):String = {
    val prettyPrinter = new PrettyPrinter(250,4)
    val xmlString = prettyPrinter.format(xml)
    HtmlFormat.escape(xmlString).toString()
      .replaceAll("[\r\n]", "<br/>")
      .replaceAll(" {2}", "&nbsp;&nbsp;")
  }

  def goodDeclaration(): Elem = {
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
        <TotalPackageQuantity>1</TotalPackageQuantity>
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
            <AdditionalDocument>
              <CategoryCode>N</CategoryCode>
              <ID>12345/30.07.2019</ID>
              <TypeCode>935</TypeCode>
              <LPCOExemptionCode>AC</LPCOExemptionCode>
            </AdditionalDocument>
            <AdditionalDocument>
              <CategoryCode>N</CategoryCode>
              <ID>12345/30.09.2019</ID>
              <TypeCode>935</TypeCode>
              <LPCOExemptionCode>AC</LPCOExemptionCode>
            </AdditionalDocument>
            <AdditionalInformation>
              <StatementCode>00500</StatementCode>
              <StatementDescription>IMPORTER</StatementDescription>
            </AdditionalInformation>
            <AdditionalInformation>
              <StatementCode>00500</StatementCode>
              <StatementDescription>IMPORTER</StatementDescription>
            </AdditionalInformation>
            <AdditionalInformation>
              <StatementCode>00500</StatementCode>
              <StatementDescription>IMPORTER</StatementDescription>
            </AdditionalInformation>
            <AdditionalInformation>
              <StatementCode>00500</StatementCode>
              <StatementDescription>IMPORTER</StatementDescription>
            </AdditionalInformation>
            <AdditionalInformation>
              <StatementCode>00500</StatementCode>
              <StatementDescription>IMPORTER</StatementDescription>
            </AdditionalInformation>
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
            <PreviousDocument>
              <CategoryCode>Z</CategoryCode>
              <ID>20191103</ID>
              <TypeCode>ZZZ</TypeCode>
              <LineNumeric>1</LineNumeric>
            </PreviousDocument>
            <PreviousDocument>
              <CategoryCode>Z</CategoryCode>
              <ID>9GB201909014002</ID>
              <TypeCode>235</TypeCode>
              <LineNumeric>1</LineNumeric>
            </PreviousDocument>
            <PreviousDocument>
              <CategoryCode>Z</CategoryCode>
              <ID>9GB201909014003</ID>
              <TypeCode>ZZZ</TypeCode>
              <LineNumeric>1</LineNumeric>
            </PreviousDocument>
            <PreviousDocument>
              <CategoryCode>Z</CategoryCode>
              <ID>9GB201909014004</ID>
              <TypeCode>270</TypeCode>
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
