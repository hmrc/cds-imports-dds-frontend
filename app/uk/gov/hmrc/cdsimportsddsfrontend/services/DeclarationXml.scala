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

    <MetaData xmlns="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2" xmlns:ns2="urn:wco:datamodel:WCO:Declaration_DS:DMS:2" xmlns:ns3="urn:wco:datamodel:WCO:DEC-DMS:2">
      <WCODataModelVersionCode>3.6</WCODataModelVersionCode>
      <WCOTypeName>DEC</WCOTypeName>
      <ResponsibleCountryCode>GB</ResponsibleCountryCode>
      <ResponsibleAgencyName>HMRC</ResponsibleAgencyName>
      <AgencyAssignedCustomizationCode>v2.1</AgencyAssignedCustomizationCode>
      <ns3:Declaration>
        <ns3:FunctionCode>9</ns3:FunctionCode>
        <ns3:FunctionalReferenceID>{referenceId}</ns3:FunctionalReferenceID>
        <ns3:TypeCode>{dec.declarationType+dec.additionalDeclarationType}</ns3:TypeCode>
        <ns3:GoodsItemQuantity>{dec.totalNumberOfItems}</ns3:GoodsItemQuantity>
        <ns3:InvoiceAmount currencyID="GBP">864.95</ns3:InvoiceAmount>
        <ns3:TotalPackageQuantity>2</ns3:TotalPackageQuantity>
        <ns3:AdditionalDocument>
          <ns3:CategoryCode>1</ns3:CategoryCode>
          <ns3:ID>8107751</ns3:ID>
          <ns3:TypeCode>DAN</ns3:TypeCode>
        </ns3:AdditionalDocument>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>AEOC</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>CGU</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>DPO</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>EIR</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>CWP</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:BorderTransportMeans>
          <ns3:RegistrationNationalityCode>US</ns3:RegistrationNationalityCode>
          <ns3:ModeCode>1</ns3:ModeCode>
        </ns3:BorderTransportMeans>
        <ns3:Declarant>
          <ns3:ID>GB164538549000</ns3:ID>
        </ns3:Declarant>
        <ns3:Exporter>
          <ns3:Name>SUPPLIER US</ns3:Name>
          <ns3:Address>
            <ns3:CityName>SUPPLIER CITY</ns3:CityName>
            <ns3:CountryCode>US</ns3:CountryCode>
            <ns3:Line>SUPPLIER STREET</ns3:Line>
            <ns3:PostcodeID>NA</ns3:PostcodeID>
          </ns3:Address>
        </ns3:Exporter>
        <ns3:GoodsShipment>
          <ns3:TransactionNatureCode>1</ns3:TransactionNatureCode>
          <ns3:Consignment>
            <ns3:ContainerCode>0</ns3:ContainerCode>
            <ns3:ArrivalTransportMeans>
              <ns3:ID>AGG01REMTIME39437</ns3:ID>
              <ns3:IdentificationTypeCode>11</ns3:IdentificationTypeCode>
            </ns3:ArrivalTransportMeans>
            <ns3:GoodsLocation>
              <ns3:Name>CWU1234567999GB</ns3:Name>
              <ns3:TypeCode>B</ns3:TypeCode>
              <ns3:Address>
                <ns3:TypeCode>Y</ns3:TypeCode>
                <ns3:CountryCode>GB</ns3:CountryCode>
              </ns3:Address>
            </ns3:GoodsLocation>
          </ns3:Consignment>
          <ns3:CustomsValuation>
            <ns3:ChargeDeduction>
              <ns3:ChargesTypeCode>AV</ns3:ChargesTypeCode>
              <ns3:OtherChargeDeductionAmount currencyID="GBP">74.52</ns3:OtherChargeDeductionAmount>
            </ns3:ChargeDeduction>
          </ns3:CustomsValuation>
          <ns3:Destination>
            <ns3:CountryCode>GB</ns3:CountryCode>
          </ns3:Destination>
          <ns3:ExportCountry>
            <ns3:ID>US</ns3:ID>
          </ns3:ExportCountry>
          <ns3:GovernmentAgencyGoodsItem>
            <ns3:SequenceNumeric>{dec.goodsItemNumber}</ns3:SequenceNumeric>
            <ns3:StatisticalValueAmount currencyID="GBP">864.95</ns3:StatisticalValueAmount>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBAEOCGB164538549000</ns3:ID>
              <ns3:TypeCode>501</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBCGUguaranteenotrequired-CCC</ns3:ID>
              <ns3:TypeCode>505</ns3:TypeCode>
              <ns3:LPCOExemptionCode>CC</ns3:LPCOExemptionCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBDPO8107751</ns3:ID>
              <ns3:TypeCode>506</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBEIRIGB164538549000</ns3:ID>
              <ns3:TypeCode>514</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBCWPGB164538549000</ns3:ID>
              <ns3:TypeCode>517</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>N</ns3:CategoryCode>
              <ns3:ID>AGG01REMTIME39437</ns3:ID>
              <ns3:TypeCode>935</ns3:TypeCode>
              <ns3:LPCOExemptionCode>AC</ns3:LPCOExemptionCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>Y</ns3:CategoryCode>
              <ns3:ID>GBAEOC03000/15</ns3:ID>
              <ns3:TypeCode>023</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>Y</ns3:CategoryCode>
              <ns3:ID>GBAEOC03000/15</ns3:ID>
              <ns3:TypeCode>024</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalInformation>
              <ns3:StatementCode>00500</ns3:StatementCode>
              <ns3:StatementDescription>IMPORTER</ns3:StatementDescription>
            </ns3:AdditionalInformation>
            <ns3:Commodity>
              <ns3:Description>ALUMINIUM AND ARTICLES THEREOF. Aluminium plates, sheets and strip, of a thickness exceeding 0,2 mm. Other. Of aluminium, not alloyed. Other. - S</ns3:Description>
              <ns3:Classification>
                <ns3:ID>76069100</ns3:ID>
                <ns3:IdentificationTypeCode>TSP</ns3:IdentificationTypeCode>
              </ns3:Classification>
              <ns3:Classification>
                <ns3:ID>90</ns3:ID>
                <ns3:IdentificationTypeCode>TRC</ns3:IdentificationTypeCode>
              </ns3:Classification>
              <ns3:DutyTaxFee>
                <ns3:DutyRegimeCode>100</ns3:DutyRegimeCode>
                <ns3:TypeCode>A00</ns3:TypeCode>
                <ns3:Payment>
                  <ns3:MethodCode>E</ns3:MethodCode>
                </ns3:Payment>
              </ns3:DutyTaxFee>
              <ns3:DutyTaxFee>
                <ns3:DutyRegimeCode>100</ns3:DutyRegimeCode>
                <ns3:TypeCode>B00</ns3:TypeCode>
                <ns3:Payment>
                  <ns3:MethodCode>E</ns3:MethodCode>
                </ns3:Payment>
              </ns3:DutyTaxFee>
              <ns3:GoodsMeasure>
                <ns3:GrossMassMeasure unitCode="KGM">105.000</ns3:GrossMassMeasure>
                <ns3:NetNetWeightMeasure unitCode="KGM">100.000</ns3:NetNetWeightMeasure>
              </ns3:GoodsMeasure>
              <ns3:InvoiceLine>
                <ns3:ItemChargeAmount currencyID="GBP">864.95</ns3:ItemChargeAmount>
              </ns3:InvoiceLine>
            </ns3:Commodity>
            <ns3:CustomsValuation>
              <ns3:MethodCode>1</ns3:MethodCode>
            </ns3:CustomsValuation>
            <ns3:GovernmentProcedure>
              <ns3:CurrentCode>{dec.requestedProcedureCode}</ns3:CurrentCode>
              <ns3:PreviousCode>{dec.previousProcedureCode}</ns3:PreviousCode>
            </ns3:GovernmentProcedure>
            <ns3:GovernmentProcedure>
              <ns3:CurrentCode>{dec.additionalProcedureCode}</ns3:CurrentCode>
            </ns3:GovernmentProcedure>
            <ns3:Origin>
              <ns3:CountryCode>US</ns3:CountryCode>
              <ns3:TypeCode>1</ns3:TypeCode>
            </ns3:Origin>
            <ns3:Packaging>
              <ns3:SequenceNumeric>1</ns3:SequenceNumeric>
              <ns3:MarksNumbersID>AGG01REMTIME39437</ns3:MarksNumbersID>
              <ns3:QuantityQuantity>2</ns3:QuantityQuantity>
              <ns3:TypeCode>NA</ns3:TypeCode>
            </ns3:Packaging>
            <ns3:ValuationAdjustment>
              <ns3:AdditionCode>0000</ns3:AdditionCode>
            </ns3:ValuationAdjustment>
          </ns3:GovernmentAgencyGoodsItem>
          <ns3:Importer>
            <ns3:ID>GB164538549000</ns3:ID>
          </ns3:Importer>
          <ns3:PreviousDocument>
            <ns3:CategoryCode>Y</ns3:CategoryCode>
            <ns3:ID>AGG01REMTIME39437</ns3:ID>
            <ns3:TypeCode>DCR</ns3:TypeCode>
          </ns3:PreviousDocument>
          <ns3:PreviousDocument>
            <ns3:CategoryCode>Y</ns3:CategoryCode>
            <ns3:ID>20190609</ns3:ID>
            <ns3:TypeCode>CLE</ns3:TypeCode>
          </ns3:PreviousDocument>
          <ns3:TradeTerms>
            <ns3:ConditionCode>CIF</ns3:ConditionCode>
            <ns3:LocationID>GBFXT</ns3:LocationID>
          </ns3:TradeTerms>
          <ns3:Warehouse>
            <ns3:ID>1234567999GB</ns3:ID>
            <ns3:TypeCode>U</ns3:TypeCode>
          </ns3:Warehouse>
        </ns3:GoodsShipment>
        <ns3:ObligationGuarantee>
          <ns3:ID>GuaranteeNotRequired</ns3:ID>
          <ns3:SecurityDetailsCode>0</ns3:SecurityDetailsCode>
        </ns3:ObligationGuarantee>
        <ns3:SupervisingOffice>
          <ns3:ID>GBBEL004</ns3:ID>
        </ns3:SupervisingOffice>
      </ns3:Declaration>
    </MetaData>
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

    <MetaData xmlns="urn:wco:datamodel:WCO:DocumentMetaData-DMS:2" xmlns:ns2="urn:wco:datamodel:WCO:Declaration_DS:DMS:2" xmlns:ns3="urn:wco:datamodel:WCO:DEC-DMS:2">
      <WCODataModelVersionCode>3.6</WCODataModelVersionCode>
      <WCOTypeName>DEC</WCOTypeName>
      <ResponsibleCountryCode>GB</ResponsibleCountryCode>
      <ResponsibleAgencyName>HMRC</ResponsibleAgencyName>
      <AgencyAssignedCustomizationCode>v2.1</AgencyAssignedCustomizationCode>
      <ns3:Declaration>
        <ns3:FunctionCode>9</ns3:FunctionCode>
        <ns3:FunctionalReferenceID>{referenceId}</ns3:FunctionalReferenceID>
        <ns3:TypeCode>IMZ</ns3:TypeCode>
        <ns3:GoodsItemQuantity>1</ns3:GoodsItemQuantity>
        <ns3:InvoiceAmount currencyID="GBP">864.95</ns3:InvoiceAmount>
        <ns3:TotalPackageQuantity>2</ns3:TotalPackageQuantity>
        <ns3:AdditionalDocument>
          <ns3:CategoryCode>1</ns3:CategoryCode>
          <ns3:ID>8107751</ns3:ID>
          <ns3:TypeCode>DAN</ns3:TypeCode>
        </ns3:AdditionalDocument>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>AEOC</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>CGU</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>DPO</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>EIR</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:AuthorisationHolder>
          <ns3:ID>GB164538549000</ns3:ID>
          <ns3:CategoryCode>CWP</ns3:CategoryCode>
        </ns3:AuthorisationHolder>
        <ns3:BorderTransportMeans>
          <ns3:RegistrationNationalityCode>US</ns3:RegistrationNationalityCode>
          <ns3:ModeCode>1</ns3:ModeCode>
        </ns3:BorderTransportMeans>
        <ns3:Declarant>
          <ns3:ID>GB164538549000</ns3:ID>
        </ns3:Declarant>
        <ns3:Exporter>
          <ns3:Name>SUPPLIER US</ns3:Name>
          <ns3:Address>
            <ns3:CityName>SUPPLIER CITY</ns3:CityName>
            <ns3:CountryCode>US</ns3:CountryCode>
            <ns3:Line>SUPPLIER STREET</ns3:Line>
            <ns3:PostcodeID>NA</ns3:PostcodeID>
          </ns3:Address>
        </ns3:Exporter>
        <ns3:GoodsShipment>
          <ns3:TransactionNatureCode>1</ns3:TransactionNatureCode>
          <ns3:Consignment>
            <ns3:ContainerCode>0</ns3:ContainerCode>
            <ns3:ArrivalTransportMeans>
              <ns3:ID>AGG01REMTIME39437</ns3:ID>
              <ns3:IdentificationTypeCode>11</ns3:IdentificationTypeCode>
            </ns3:ArrivalTransportMeans>
            <ns3:GoodsLocation>
              <ns3:Name>CWU1234567999GB</ns3:Name>
              <ns3:TypeCode>B</ns3:TypeCode>
              <ns3:Address>
                <ns3:TypeCode>Y</ns3:TypeCode>
                <ns3:CountryCode>GB</ns3:CountryCode>
              </ns3:Address>
            </ns3:GoodsLocation>
          </ns3:Consignment>
          <ns3:CustomsValuation>
            <ns3:ChargeDeduction>
              <ns3:ChargesTypeCode>AV</ns3:ChargesTypeCode>
              <ns3:OtherChargeDeductionAmount currencyID="GBP">74.52</ns3:OtherChargeDeductionAmount>
            </ns3:ChargeDeduction>
          </ns3:CustomsValuation>
          <ns3:Destination>
            <ns3:CountryCode>GB</ns3:CountryCode>
          </ns3:Destination>
          <ns3:ExportCountry>
            <ns3:ID>US</ns3:ID>
          </ns3:ExportCountry>
          <ns3:GovernmentAgencyGoodsItem>
            <ns3:SequenceNumeric>1</ns3:SequenceNumeric>
            <ns3:StatisticalValueAmount currencyID="GBP">864.95</ns3:StatisticalValueAmount>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBAEOCGB164538549000</ns3:ID>
              <ns3:TypeCode>501</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBCGUguaranteenotrequired-CCC</ns3:ID>
              <ns3:TypeCode>505</ns3:TypeCode>
              <ns3:LPCOExemptionCode>CC</ns3:LPCOExemptionCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBDPO8107751</ns3:ID>
              <ns3:TypeCode>506</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBEIRIGB164538549000</ns3:ID>
              <ns3:TypeCode>514</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>C</ns3:CategoryCode>
              <ns3:ID>GBCWPGB164538549000</ns3:ID>
              <ns3:TypeCode>517</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>N</ns3:CategoryCode>
              <ns3:ID>AGG01REMTIME39437</ns3:ID>
              <ns3:TypeCode>935</ns3:TypeCode>
              <ns3:LPCOExemptionCode>AC</ns3:LPCOExemptionCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>Y</ns3:CategoryCode>
              <ns3:ID>GBAEOC03000/15</ns3:ID>
              <ns3:TypeCode>023</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalDocument>
              <ns3:CategoryCode>Y</ns3:CategoryCode>
              <ns3:ID>GBAEOC03000/15</ns3:ID>
              <ns3:TypeCode>024</ns3:TypeCode>
            </ns3:AdditionalDocument>
            <ns3:AdditionalInformation>
              <ns3:StatementCode>00500</ns3:StatementCode>
              <ns3:StatementDescription>IMPORTER</ns3:StatementDescription>
            </ns3:AdditionalInformation>
            <ns3:Commodity>
              <ns3:Description>ALUMINIUM AND ARTICLES THEREOF. Aluminium plates, sheets and strip, of a thickness exceeding 0,2 �mm. Other. Of aluminium, not alloyed. Other. - S</ns3:Description>
              <ns3:Classification>
                <ns3:ID>76069100</ns3:ID>
                <ns3:IdentificationTypeCode>TSP</ns3:IdentificationTypeCode>
              </ns3:Classification>
              <ns3:Classification>
                <ns3:ID>90</ns3:ID>
                <ns3:IdentificationTypeCode>TRC</ns3:IdentificationTypeCode>
              </ns3:Classification>
              <ns3:DutyTaxFee>
                <ns3:DutyRegimeCode>100</ns3:DutyRegimeCode>
                <ns3:TypeCode>A00</ns3:TypeCode>
                <ns3:Payment>
                  <ns3:MethodCode>E</ns3:MethodCode>
                </ns3:Payment>
              </ns3:DutyTaxFee>
              <ns3:DutyTaxFee>
                <ns3:DutyRegimeCode>100</ns3:DutyRegimeCode>
                <ns3:TypeCode>B00</ns3:TypeCode>
                <ns3:Payment>
                  <ns3:MethodCode>E</ns3:MethodCode>
                </ns3:Payment>
              </ns3:DutyTaxFee>
              <ns3:GoodsMeasure>
                <ns3:GrossMassMeasure unitCode="KGM">105.000</ns3:GrossMassMeasure>
                <ns3:NetNetWeightMeasure unitCode="KGM">100.000</ns3:NetNetWeightMeasure>
              </ns3:GoodsMeasure>
              <ns3:InvoiceLine>
                <ns3:ItemChargeAmount currencyID="GBP">864.95</ns3:ItemChargeAmount>
              </ns3:InvoiceLine>
            </ns3:Commodity>
            <ns3:CustomsValuation>
              <ns3:MethodCode>1</ns3:MethodCode>
            </ns3:CustomsValuation>
            <ns3:GovernmentProcedure>
              <ns3:CurrentCode>40</ns3:CurrentCode>
              <ns3:PreviousCode>71</ns3:PreviousCode>
            </ns3:GovernmentProcedure>
            <ns3:GovernmentProcedure>
              <ns3:CurrentCode>000</ns3:CurrentCode>
            </ns3:GovernmentProcedure>
            <ns3:Origin>
              <ns3:CountryCode>US</ns3:CountryCode>
              <ns3:TypeCode>1</ns3:TypeCode>
            </ns3:Origin>
            <ns3:Packaging>
              <ns3:SequenceNumeric>1</ns3:SequenceNumeric>
              <ns3:MarksNumbersID>AGG01REMTIME39437</ns3:MarksNumbersID>
              <ns3:QuantityQuantity>2</ns3:QuantityQuantity>
              <ns3:TypeCode>NA</ns3:TypeCode>
            </ns3:Packaging>
            <ns3:ValuationAdjustment>
              <ns3:AdditionCode>0000</ns3:AdditionCode>
            </ns3:ValuationAdjustment>
          </ns3:GovernmentAgencyGoodsItem>
          <ns3:Importer>
            <ns3:ID>GB164538549000</ns3:ID>
          </ns3:Importer>
          <ns3:PreviousDocument>
            <ns3:CategoryCode>Y</ns3:CategoryCode>
            <ns3:ID>AGG01REMTIME39437</ns3:ID>
            <ns3:TypeCode>DCR</ns3:TypeCode>
          </ns3:PreviousDocument>
          <ns3:PreviousDocument>
            <ns3:CategoryCode>Y</ns3:CategoryCode>
            <ns3:ID>20190609</ns3:ID>
            <ns3:TypeCode>CLE</ns3:TypeCode>
          </ns3:PreviousDocument>
          <ns3:TradeTerms>
            <ns3:ConditionCode>CIF</ns3:ConditionCode>
            <ns3:LocationID>GBFXT</ns3:LocationID>
          </ns3:TradeTerms>
          <ns3:Warehouse>
            <ns3:ID>1234567999GB</ns3:ID>
            <ns3:TypeCode>U</ns3:TypeCode>
          </ns3:Warehouse>
        </ns3:GoodsShipment>
        <ns3:ObligationGuarantee>
          <ns3:ID>GuaranteeNotRequired</ns3:ID>
          <ns3:SecurityDetailsCode>0</ns3:SecurityDetailsCode>
        </ns3:ObligationGuarantee>
        <ns3:SupervisingOffice>
          <ns3:ID>GBBEL004</ns3:ID>
        </ns3:SupervisingOffice>
      </ns3:Declaration>
    </MetaData>
  }

}
