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

import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Eori, ImportDeclaration}

import scala.xml.Elem

object DeclarationXml {

  def fromImportDeclaration(eori:Eori, importDeclaration: ImportDeclaration):Elem = {
    build(eori)
  }

   // This should later build an xml that can be submitted to the declaration API.
  def build(importersEori:Eori):Elem = {
    <MetaData xmlns:ns3="urn:wco:datamodel:WCO:DEC-DMS:2" xmlns:ns2="urn:wco:datamodel:WCO:Declaration_DS:DMS:2" xmlns="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2">
      <WCODataModelVersionCode>3.6</WCODataModelVersionCode>
      <WCOTypeName>DEC</WCOTypeName>
      <ResponsibleCountryCode>GB</ResponsibleCountryCode>
      <ResponsibleAgencyName>HMRC</ResponsibleAgencyName>
      <AgencyAssignedCustomizationCode>v2.1</AgencyAssignedCustomizationCode>
      <ns3:Declaration>
        <ns3:FunctionCode>9</ns3:FunctionCode>
        <ns3:FunctionalReferenceID>Mf1kMq9AKW5tQyRog8V</ns3:FunctionalReferenceID>
        <ns3:TypeCode>EXA</ns3:TypeCode>
        <ns3:GoodsItemQuantity>1</ns3:GoodsItemQuantity>
        <ns3:TotalPackageQuantity>88980359</ns3:TotalPackageQuantity>
        <ns3:SpecificCircumstancesCodeCode>A20</ns3:SpecificCircumstancesCodeCode>
        <ns3:BorderTransportMeans>
          <ns3:ID>x7MRRGhXNv</ns3:ID>
          <ns3:IdentificationTypeCode>81</ns3:IdentificationTypeCode>
          <ns3:ModeCode>4</ns3:ModeCode>
        </ns3:BorderTransportMeans>
        <ns3:Consignment>
          <ns3:Carrier>
            <ns3:ID>IE7778647</ns3:ID>
          </ns3:Carrier>
          <ns3:Itinerary>
            <ns3:SequenceNumeric>0</ns3:SequenceNumeric>
            <ns3:RoutingCountryCode>ME</ns3:RoutingCountryCode>
          </ns3:Itinerary>
        </ns3:Consignment>
        <ns3:Declarant>
          <ns3:ID>{importersEori}</ns3:ID>
        </ns3:Declarant>
        <ns3:ExitOffice>
          <ns3:ID>GB000060</ns3:ID>
        </ns3:ExitOffice>
        <ns3:Exporter>
          <ns3:ID>DK127640</ns3:ID>
        </ns3:Exporter>
        <ns3:GoodsShipment>
          <ns3:TransactionNatureCode>2</ns3:TransactionNatureCode>
          <ns3:Consignee>
            <ns3:ID>FR94986003290</ns3:ID>
          </ns3:Consignee>
          <ns3:Consignment>
            <ns3:ContainerCode>0</ns3:ContainerCode>
            <ns3:DepartureTransportMeans>
              <ns3:ID>EbJjjAsFRsahZRz</ns3:ID>
              <ns3:IdentificationTypeCode>40</ns3:IdentificationTypeCode>
            </ns3:DepartureTransportMeans>
            <ns3:GoodsLocation>
              <ns3:TypeCode>I</ns3:TypeCode>
              <ns3:Address>
                <ns3:TypeCode>H</ns3:TypeCode>
                <ns3:CountryCode>CZ</ns3:CountryCode>
              </ns3:Address>
            </ns3:GoodsLocation>
            <ns3:TransportEquipment>
              <ns3:SequenceNumeric>0</ns3:SequenceNumeric>
              <ns3:Seal>
                <ns3:SequenceNumeric>0</ns3:SequenceNumeric>
              </ns3:Seal>
            </ns3:TransportEquipment>
          </ns3:Consignment>
          <ns3:Destination>
            <ns3:CountryCode>IE</ns3:CountryCode>
          </ns3:Destination>
          <ns3:ExportCountry>
            <ns3:ID>PT</ns3:ID>
          </ns3:ExportCountry>
          <ns3:GovernmentAgencyGoodsItem>
            <ns3:SequenceNumeric>1</ns3:SequenceNumeric>
            <ns3:StatisticalValueAmount currencyID="GBP">656</ns3:StatisticalValueAmount>
            <ns3:AdditionalInformation>
              <ns3:StatementCode>25670</ns3:StatementCode>
              <ns3:StatementDescription>00XuYS4vw29jTsVuY18M7HMaoDMwHszYRjwvzaqUoHb436KGAixIlZb7gXk</ns3:StatementDescription>
            </ns3:AdditionalInformation>
            <ns3:Commodity>
              <ns3:Description>Vanity case with outer surface made of plastic sheet</ns3:Description>
              <ns3:Classification>
                <ns3:ID>0Q</ns3:ID>
                <ns3:IdentificationTypeCode>TSP</ns3:IdentificationTypeCode>
              </ns3:Classification>
              <ns3:GoodsMeasure>
                <ns3:GrossMassMeasure unitCode="KGM">834.41</ns3:GrossMassMeasure>
                <ns3:NetNetWeightMeasure unitCode="KGM">885.12</ns3:NetNetWeightMeasure>
              </ns3:GoodsMeasure>
            </ns3:Commodity>
            <ns3:GovernmentProcedure>
              <ns3:CurrentCode>Ax</ns3:CurrentCode>
              <ns3:PreviousCode>ob</ns3:PreviousCode>
            </ns3:GovernmentProcedure>
            <ns3:GovernmentProcedure>
              <ns3:CurrentCode>376</ns3:CurrentCode>
            </ns3:GovernmentProcedure>
            <ns3:Packaging>
              <ns3:SequenceNumeric>0</ns3:SequenceNumeric>
              <ns3:MarksNumbersID>c96rKLwOrLDwCBTwgWO2Pq8USv4V</ns3:MarksNumbersID>
              <ns3:QuantityQuantity>228908</ns3:QuantityQuantity>
              <ns3:TypeCode>LZ</ns3:TypeCode>
            </ns3:Packaging>
          </ns3:GovernmentAgencyGoodsItem>
          <ns3:PreviousDocument>
            <ns3:CategoryCode>Z</ns3:CategoryCode>
            <ns3:ID>6GB357357720458-8995800581QWERTYUIO</ns3:ID>
            <ns3:TypeCode>DCR</ns3:TypeCode>
            <ns3:LineNumeric>1</ns3:LineNumeric>
          </ns3:PreviousDocument>
        </ns3:GoodsShipment>
        <ns3:PresentationOffice>
          <ns3:ID>GBWKG001</ns3:ID>
        </ns3:PresentationOffice>
      </ns3:Declaration>
    </MetaData>
  }
}
