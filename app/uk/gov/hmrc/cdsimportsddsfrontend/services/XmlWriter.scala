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

import scala.xml.{Elem, Node, Text}

trait XmlWriter[A] {
  def toXml(value: A): Option[Elem]

  def maybeElement(elementName: String, maybeElementValue: Option[String]): Option[Node] = {
    maybeElementValue.filter(_.nonEmpty).map(value => element(elementName, value))
  }

  def element(elementName: String, elementValue: String): Node = {
    Elem.apply(null, elementName, scala.xml.Null, scala.xml.TopScope, true, Text(elementValue)) //scalastyle:ignore
  }
}
