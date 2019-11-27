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

import org.scalatest.{MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

import scala.xml.Elem

class DeclarationXml_BorderTransportMeansSpec extends WordSpec with MustMatchers {

  "BorderTransportMeans data" should {
    "be populated in the XML" when {
      "the borderTransportMeans is present in the declaration data" in {
        val declaration = Declaration(borderTransportMeans = Some(BorderTransportMeans()))
        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

        (xml \ "Declaration" \ "BorderTransportMeans" \ "RegistrationNationalityCode").head.text mustBe "FR"
        (xml \ "Declaration" \ "BorderTransportMeans" \ "ModeCode").head.text mustBe "1"
      }
    }

    "be omitted from XML" when {
      "the borderTransportMeans is not  present in the declaration data" in {
        val declaration = Declaration(borderTransportMeans = None)
        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

        (xml \ "Declaration" \ "BorderTransportMeans").length mustBe 0
      }
    }
  }
}
