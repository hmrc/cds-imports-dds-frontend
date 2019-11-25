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
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlWriter

import scala.xml.{Elem, Node}

case class AuthorisationHolder(
                                identifier: Option[String] = None,
                                categoryCode: Option[String] = None
                              )

object AuthorisationHolder {
  implicit val authorisationHolderWriter: XmlWriter[AuthorisationHolder] = new XmlWriter[AuthorisationHolder] {
    override def toXml(value: AuthorisationHolder): Option[Elem] = {
      val id = maybeElement("ID", value.identifier)
      val categoryCode = maybeElement("CategoryCode", value.categoryCode)
      val childNodes: List[Node] = List(id, categoryCode).flattenOption

      Option(childNodes).filter(_.nonEmpty).map(nonEmptyChildNodes => <AuthorisationHolder>{nonEmptyChildNodes}</AuthorisationHolder>)
    }
  }
}