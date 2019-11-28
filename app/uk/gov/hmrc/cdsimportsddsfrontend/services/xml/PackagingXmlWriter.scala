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

import uk.gov.hmrc.cdsimportsddsfrontend.domain.Packaging

import scala.xml.{Elem, Node}

object PackagingXmlWriter {

  implicit val packagingXmlWriter: XmlWriter[Packaging] = new XmlWriter[Packaging] {
    override def toXml(value: Packaging): Option[Elem] = {
      val sequenceNumeric = Some(<SequenceNumeric>1</SequenceNumeric>)
      val marksNumberId = maybeElement("MarksNumbersID", value.marksNumberId)
      val quantityQuantity = maybeElement("QuantityQuantity", value.quantityQuantity)
      val typeCode = maybeElement("TypeCode", value.typeCode)

      val nodes = List(sequenceNumeric, marksNumberId, quantityQuantity, typeCode).flatten
      val nonEmptyNodes = nodes.filter(_.nonEmpty)
      nonEmptyNodes match {
        case nodes if nodes == List(<SequenceNumeric>1</SequenceNumeric>) => None
        case nodes if nodes.nonEmpty => Some(<Packaging>{nodes}</Packaging>)
        case _ => None
      }
    }
  }
}
