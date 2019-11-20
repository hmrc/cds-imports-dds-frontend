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

import uk.gov.hmrc.cdsimportsddsfrontend.domain._

import scala.xml.{Elem, Node, Text}
import cats.implicits._

trait XmlWriter[A] {
  def toXml(value: A): Option[Elem]
}

object XmlWriterInstances {

  implicit val previousDocumentWriter: XmlWriter[PreviousDocument] = new XmlWriter[PreviousDocument] {
    override def toXml(value: PreviousDocument): Option[Elem] = {
      val categoryCode: Option[Node] = maybeElement("CategoryCode", value.categoryCode)
      val id = maybeElement("ID", value.id)
      val typeCode = maybeElement("TypeCode", value.typeCode)
      val lineNumeric = maybeElement("LineNumeric", value.lineNumeric)
      val childNodes: List[Node] = List(categoryCode, id, typeCode, lineNumeric).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <PreviousDocument>{nonEmptyChildNodes}</PreviousDocument>)
    }
  }

  implicit val authorisationHolderWriter: XmlWriter[AuthorisationHolder] = new XmlWriter[AuthorisationHolder] {
    override def toXml(value: AuthorisationHolder): Option[Elem] = {
      val id = maybeElement("ID", value.identifier)
      val categoryCode = maybeElement("CategoryCode", value.categoryCode)
      val childNodes: List[Node] = List(id, categoryCode).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <AuthorisationHolder>{nonEmptyChildNodes}</AuthorisationHolder>)
    }
  }

  implicit val domesticDutyTaxPartyWriter: XmlWriter[DomesticDutyTaxParty] = new XmlWriter[DomesticDutyTaxParty] {
    override def toXml(value: DomesticDutyTaxParty): Option[Elem] = {
      val id = maybeElement("ID", value.identifier)
      val roleCode = maybeElement("RoleCode", value.roleCode)
      val childNodes: List[Node] = List(id, roleCode).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <DomesticDutyTaxParty>{nonEmptyChildNodes}</DomesticDutyTaxParty>)
    }
  }

  implicit val additionalDocumentWriter: XmlWriter[AdditionalDocumentType] = new XmlWriter[AdditionalDocumentType] {
    override def toXml( value: AdditionalDocumentType ): Option[Elem] = {
      val categoryCode: Option[Node] = maybeElement("CategoryCode", value.categoryCode)
      val id = maybeElement("ID", value.id)
      val name = maybeElement("Name", value.name)
      val typeCode = maybeElement("TypeCode", value.typeCode)
      val lpcoExemptionCode = maybeElement("LPCOExemptionCode", value.lpco)
      val l: List[Node] = List(categoryCode, id, name, typeCode, lpcoExemptionCode).flattenOption

      Option(l).filter(_.nonEmpty).map(e => <AdditionalDocument>{e}</AdditionalDocument>)
    }
  }

  private def maybeElement(elementName: String, maybeElementValue: Option[String]): Option[Node] = {
    maybeElementValue.filter(_.nonEmpty).map { value =>
      Elem.apply(null, elementName, scala.xml.Null, scala.xml.TopScope, true, Text(value)) //scalastyle:ignore
    }
  }

}
