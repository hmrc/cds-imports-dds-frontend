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

import uk.gov.hmrc.cdsimportsddsfrontend.domain.PreviousDocument
import scala.xml.{Elem, Node, Text}
import cats.implicits._

trait XmlWriter[A] {
  def toXml(value: A): Option[Elem]
}

object XmlWriterInstances {

    implicit def maybeElement(name: String, maybeValue: Option[String]): Option[Node] = {
      if (maybeValue.exists(_.trim.nonEmpty)) {
        Some(Elem.apply(null, name, scala.xml.Null, scala.xml.TopScope, true, Text(maybeValue.getOrElse("").trim))) // scalastyle:ignore
      } else {
        None
      }
    }

  implicit val previousDocumentWriter: XmlWriter[PreviousDocument] = new XmlWriter[PreviousDocument] {
    override def toXml( value: PreviousDocument ): Option[Elem] = {
      val categoryCode: Option[Node] = maybeElement("CategoryCode", value.categoryCode)
      val id = maybeElement("ID", value.id)
      val typeCode = maybeElement("TypeCode", value.typeCode)
      val lineNumeric = maybeElement("LineNumeric", value.lineNumeric)
      val l: List[Node] = List(categoryCode, id, typeCode, lineNumeric).flattenOption

      Option(l).filter(_.nonEmpty).map(e => <PreviousDocument>{e}</PreviousDocument>)
    }
  }
}
