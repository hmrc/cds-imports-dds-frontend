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

class DeclarationXml_TotalGrossMassMeasure extends WordSpec with MustMatchers {

  "TotalGrossMassMeasure data" should {
    "be populated in the XML at header level" when {
      "the gross mass is present in the data" in {
        val declaration = Declaration(
          totalGrossMassMeasure = Some("92")
        )

        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "TotalGrossMassMeasure").head.text mustBe "92"
      }
    }

    "be omitted from XML" when {
      "the no gross mass data exists" in {
        val declaration = Declaration(totalGrossMassMeasure = None)

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "TotalGrossMassMeasure" ).length mustBe 0

      }

    }
  }
}
