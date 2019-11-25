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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.ExportCountry
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances.maybeElement

import scala.xml.{Elem, Node}

object ExportCountryXmlWriter {

  implicit val exportCountryXmlWriter: XmlWriter[ExportCountry] = new XmlWriter[ExportCountry] {
    override def toXml(value: ExportCountry): Option[Elem] = {
      val id: Option[Node] = maybeElement("ID", value.id)

      val nodes: List[Node] = List(id).flattenOption
      Option(nodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <ExportCountry>{nonEmptyChildNodes}</ExportCountry>)
    }
  }
}

