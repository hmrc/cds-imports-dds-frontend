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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.GoodsMeasure

import scala.xml.{Attribute, Elem, Node}

object GoodsMeasureXmlWriter {

  implicit val goodsMeasureXmlWriter: XmlWriter[GoodsMeasure] = new XmlWriter[GoodsMeasure] {
    override def toXml(value: GoodsMeasure): Option[Elem] = {
      val attr = Attribute.apply(pre = "", key = "unitCode", value= "KGM", scala.xml.Null)

      val grossMassMeasure: Option[Node] = maybeElement("GrossMassMeasure", value.grossMassMeasure, Some(attr))
      val netNetWeightMeasure: Option[Node] = maybeElement("NetNetWeightMeasure", value.netNetWeightMeasure, Some(attr))
      val tariffQuantity: Option[Node] = maybeElement("TariffQuantity", value.tariffQuantity)

      val nodes = List(grossMassMeasure, netNetWeightMeasure, tariffQuantity).flattenOption
      Option(nodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <GoodsMeasure>{nonEmptyChildNodes}</GoodsMeasure>)
    }
  }
}

