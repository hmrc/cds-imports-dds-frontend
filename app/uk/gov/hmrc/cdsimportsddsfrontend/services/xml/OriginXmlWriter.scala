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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Origin

import scala.xml.{Elem, Node}

object OriginXmlWriter {

  implicit val originXmlWriter: XmlWriter[Origin] = new XmlWriter[Origin] {
    override def toXml(value: Origin): Option[Elem] = {
      val countryCode: Option[Node] = maybeElement("CountryCode", value.countryCode)
      val typeCode = maybeElement("TypeCode", value.typeCode)

      val nodes: List[Node] = List(countryCode, typeCode).flattenOption
      Option(nodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <Origin>{nonEmptyChildNodes}</Origin>)
    }
  }
}
