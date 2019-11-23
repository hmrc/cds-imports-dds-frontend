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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms

import java.io.StringReader

import play.api.data.Forms.{mapping, of}
import play.api.data.format.Formats.parsing
import play.api.data.format.Formatter
import play.api.data.{Form, FormError, Mapping}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.XmlDeclaration

import scala.xml.{Elem, InputSource}


object XmlDeclarationFormMapping {

  implicit val xmlFormatter: XmlFormatter = new XmlFormatter()
  val xml: Mapping[Elem] = of[Elem]

  val form: Form[XmlDeclaration] = Form(
    mapping(
      "declaration-data" -> xml
    )(XmlDeclaration.apply)(XmlDeclaration.unapply)
  )

}


class XmlFormatter extends Formatter[Elem] {

  def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Elem] =
    parsing(parseXml, "declaration.not.xml", Nil)(key, data)

  def unbind(key: String, value: Elem): Map[String, String] = {
    Map(key -> value.toString())
  }

  private def parseXml(xmlString: String): Elem = {
    scala.xml.XML.load(new InputSource(new StringReader(xmlString)))
  }

}