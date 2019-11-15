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

package uk.gov.hmrc.cdsimportsddsfrontend

import uk.gov.hmrc.cdsimportsddsfrontend.domain.PreviousDocument

import cats.implicits._
import scala.xml.{Elem, Node, Text}

package object services {

//  implicit def maybeElement(name: String, maybeValue: Option[String]): Option[Node] = {
//    if (maybeValue.exists(_.trim.nonEmpty)) {
//      Some(Elem.apply(null, name, scala.xml.Null, scala.xml.TopScope, true, Text(maybeValue.getOrElse("").trim)))
//    } else {
//      None
//    }
//  }

//  implicit class PreviousDocumentExtension(val previousDocument: PreviousDocument) {
//    def toXml(): Option[Node] = {
//      val categoryCode: Option[Node] = maybeElement("CategoryCode", previousDocument.categoryCode)
//      val id = maybeElement("ID", previousDocument.id)
//      val typeCode = maybeElement("TypeCode", previousDocument.typeCode)
//      val lineNumeric = maybeElement("LineNumeric", previousDocument.lineNumeric)
//      val l: List[Node] = List(categoryCode, id, typeCode, lineNumeric).flattenOption
//
//      Option(l).filter(_.nonEmpty).map(e => <PreviousDocument>{e}</PreviousDocument>)
//    }
//  }
}
