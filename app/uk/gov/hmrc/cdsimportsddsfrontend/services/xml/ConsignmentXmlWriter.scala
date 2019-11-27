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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Consignment
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ArrivalTransportMeansXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.GoodsLocationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances.maybeElement

import scala.xml.{Elem, Node}

object ConsignmentXmlWriter {

  implicit val consignmentXmlWriter: XmlWriter[Consignment] = new XmlWriter[Consignment] {
    override def toXml(value: Consignment): Option[Elem] = {
      val containerCode: Option[Node] = maybeElement("ContainerCode", value.containerCode)
      val arrivalTransportMeans: Option[Node] = value.arrivalTransportMeans.flatMap(_.toXml)
      val goodLocation: Option[Node] = value.goodsLocation.flatMap(_.toXml)

      val nodes: List[Node] = List(containerCode, arrivalTransportMeans, goodLocation).flattenOption

      Option(nodes).filter(_.nonEmpty).map {
        nonEmptyChildNodes => <Consignment>{nonEmptyChildNodes}</Consignment>
      }
    }
  }
}
