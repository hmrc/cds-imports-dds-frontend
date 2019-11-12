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

package uk.gov.hmrc.cdsimportsddsfrontend.domain

import cats.implicits._
import scala.xml.Node

case class PreviousDocument(
                        categoryCode: Option[String] = Some(""),
                        id: Option[String] = Some(""),
                        typeCode: Option[String] = Some(""),
                        lineNumeric: Option[String] = Some("") ) {

  def toXml(): Option[Node] = {
    val cc: Option[Node] = maybeElement("CategoryCode", this.categoryCode)
    val id = maybeElement("ID", this.id)
    val tc = maybeElement("TypeCode", this.typeCode)
    val ln = maybeElement("LineNumeric", this.lineNumeric)
    val l: List[Node] = List(cc, id, tc, ln).flattenOption
    if (!l.isEmpty) {
      Some(<PreviousDocument>{l}</PreviousDocument>)}
    else
      None
  }
}