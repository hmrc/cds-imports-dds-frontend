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

import cats.implicits._
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import scala.xml.{Attribute, Elem, MetaData, Node, NodeSeq, Text}

trait XmlWriter[A] {

  def toXml(value: A): NodeSeq = {
    toXmlOption(value).getOrElse(NodeSeq.Empty)
  }

  def toXmlOption(value: A): Option[Elem]

  protected def maybeElement(elementName: String, maybeElementValue: Option[String], attribute: Option[Attribute] = None): Option[Node] = {
    maybeElementValue.filter(_.nonEmpty).map(value => element(elementName, value, attribute))
  }

  protected def element(elementName: String, elementValue: String, attribute: Option[Attribute] = None ): Elem = {
    elementContruct(elementName, attribute, Text(elementValue))
  }

  protected def maybeDateTimeElement(elementName: String, maybeElementValue: Option[String], attribute: Option[Attribute] = None): Option[Node] = {
    maybeElementValue.filter(_.nonEmpty).map { value =>
      val formatAttrib = Attribute.apply(pre = "", key = "formatCode", "304", scala.xml.Null)
      val dtElement = Elem.apply("p1", "DateTimeString", formatAttrib, scala.xml.TopScope, true, Text(value))

      elementContruct(elementName, attribute, dtElement)
    }
  }

  private def elementContruct(elementName: String, attribute: Option[Attribute], child: Node): Elem = {
    val attributes: MetaData = attribute match {
      case Some(attrValue) => attrValue
      case None  => scala.xml.Null
    }

    Elem.apply(null, elementName, attributes, scala.xml.TopScope, true, child) //scalastyle:ignore
  }
}

object XmlWriterInstances {

  implicit val previousDocumentWriter: XmlWriter[PreviousDocument] = new XmlWriter[PreviousDocument] {
    override def toXmlOption(value: PreviousDocument): Option[Elem] = {
      val categoryCode: Option[Node] = maybeElement("CategoryCode", value.categoryCode)
      val id = maybeElement("ID", value.id)
      val typeCode = maybeElement("TypeCode", value.typeCode)
      val lineNumeric = maybeElement("LineNumeric", value.lineNumeric)
      val childNodes: List[Node] = List(categoryCode, id, typeCode, lineNumeric).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <PreviousDocument>{nonEmptyChildNodes}</PreviousDocument>)
    }
  }

  implicit val authorisationHolderWriter: XmlWriter[AuthorisationHolder] = new XmlWriter[AuthorisationHolder] {
    override def toXmlOption(value: AuthorisationHolder): Option[Elem] = {
      val id = maybeElement("ID", value.identifier)
      val categoryCode = maybeElement("CategoryCode", value.categoryCode)
      val childNodes: List[Node] = List(id, categoryCode).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <AuthorisationHolder>{nonEmptyChildNodes}</AuthorisationHolder>)
    }
  }

  implicit val domesticDutyTaxPartyWriter: XmlWriter[DomesticDutyTaxParty] = new XmlWriter[DomesticDutyTaxParty] {
    override def toXmlOption(value: DomesticDutyTaxParty): Option[Elem] = {
      val id = maybeElement("ID", value.identifier)
      val roleCode = maybeElement("RoleCode", value.roleCode)
      val childNodes: List[Node] = List(id, roleCode).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <DomesticDutyTaxParty>{nonEmptyChildNodes}</DomesticDutyTaxParty>)
    }
  }

  implicit val additionalDocumentWriter: XmlWriter[AdditionalDocument] = new XmlWriter[AdditionalDocument] {
    override def toXmlOption(value: AdditionalDocument ): Option[Elem] = {
      val categoryCode: Option[Node] = maybeElement("CategoryCode", value.categoryCode)
      val id = maybeElement("ID", value.id)
      val name = maybeElement("Name", value.name)
      val typeCode = maybeElement("TypeCode", value.typeCode)
      val lpcoExemptionCode = maybeElement("LPCOExemptionCode", value.lpco)
      val effectiveDateTime = maybeDateTimeElement("EffectiveDateTime", value.effectiveDateTime)
      val submitter = value.submitter.flatMap( _.toXmlOption)
      val writeOff = value.writeOff.flatMap( _.toXmlOption)

      val l: List[Node] = List(categoryCode, effectiveDateTime, id, name, typeCode, lpcoExemptionCode, submitter, writeOff).flattenOption

      Option(l).filter(_.nonEmpty).map(e => <AdditionalDocument>{e}</AdditionalDocument>)
    }
  }

  implicit val submitterWriter: XmlWriter[Submitter] = new XmlWriter[Submitter] {
    override def toXmlOption(value: Submitter ): Option[Elem] = {
      val name: Option[Node] = maybeElement("Name", value.name)

      name.map(e => <Submitter>{e}</Submitter>)
    }
  }

  implicit val writeOffWriter: XmlWriter[WriteOff] = new XmlWriter[WriteOff] {
    override def toXmlOption(value: WriteOff ): Option[Elem] = {
      val optAttrib = value.unitCode.map( unitCode => Attribute.apply(pre = "", key = "unitCode", unitCode, scala.xml.Null))
      val quantityQuantity: Option[Node] = maybeElement("QuantityQuantity", value.quantityQuantity, optAttrib)

      quantityQuantity.map(e => <WriteOff>{e}</WriteOff>)
    }
  }

  implicit val chargeDeductionWriter: XmlWriter[ChargeDeduction] = new XmlWriter[ChargeDeduction] {
    override def toXmlOption(value: ChargeDeduction): Option[Elem] = {
      Some(
        <ChargeDeduction>
          <ChargesTypeCode>{value.typeCode}</ChargesTypeCode>
          <OtherChargeDeductionAmount currencyID={value.currencyAmount.currency}>{value.currencyAmount.amount}</OtherChargeDeductionAmount>
        </ChargeDeduction>
      )
    }
  }

  def currencyAmountWriterFor(elementName: String): XmlWriter[CurrencyAmount] = new XmlWriter[CurrencyAmount] {
    override def toXmlOption(value: CurrencyAmount): Option[Elem] = {
      val currencyAttr = Option(Attribute(pre = "", key = "currencyID", value = value.currency, scala.xml.Null))
      Option(element(elementName, value.amount, currencyAttr))
    }
  }

  implicit val statisticalValueAmountWriter: XmlWriter[CurrencyAmount] = currencyAmountWriterFor("StatisticalValueAmount")

  implicit val itemChargeAmountWriter: XmlWriter[CurrencyAmount] = currencyAmountWriterFor("ItemChargeAmount")
}
