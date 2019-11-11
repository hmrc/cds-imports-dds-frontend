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

import org.scalatest.{AppendedClues, MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Address, Declaration, DeclarationParties, Party}

import scala.xml.Elem

class DeclarationXml_PartiesSpec extends WordSpec with MustMatchers with AppendedClues {

  "Declaration XML builder" when {
    "transforming parties data" should {
      val someParties = DeclarationParties(
        Some(Party(None, Some("GB987654321"), None)),
        Some(Party(Some("Barney"), Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG"))))
      )
      val declaration = Declaration(parties = someParties)

      "populate declarant ID" in {
        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)
        (xmlElement \ "Declaration" \ "Declarant" \ "ID").text mustBe "GB987654321"
      }

      "populate header-level exporter name and ID" in {
        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)
        (xmlElement \ "Declaration" \ "Exporter" \ "Name").text mustBe "Barney"
        (xmlElement \ "Declaration" \ "Exporter" \ "ID").text mustBe "GB12345678A"
      }

      "populate header-level exporter address" in {
        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)
        (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "Line").text mustBe "123 Foobar Lane"
        (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "CityName").text mustBe "Glasgow"
        (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "CountryCode").text mustBe "GB"
        (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "PostcodeID").text mustBe "G12 4GG"
      }

      "omit Declarant tag if no declarant provided" in {
        val someParties = DeclarationParties(None, Some(Party(Some("Barney"), Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
        val declaration = Declaration(parties = someParties)

        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)

        (xmlElement \ "Declaration" \ "Declarant").length mustBe 0 withClue ("Found unexpected Declarant tag")
      }

      "omit Exporter tag if no exporter provided" in {
        val someParties = DeclarationParties(None, None)
        val declaration = Declaration(parties = someParties)

        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)

        (xmlElement \ "Declaration" \ "Exporter").length mustBe 0 withClue ("Found unexpected Exporter tag")
      }

      "omit Exporter Name tag if no exporter name provided" in {
        val someParties = DeclarationParties(None, Some(Party(None, Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
        val declaration = Declaration(parties = someParties)

        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)

        (xmlElement \ "Declaration" \ "Exporter" \ "Name").length mustBe 0 withClue ("Found unexpected Exporter Name tag")
      }

      "omit Exporter ID tag if no exporter ID provided" in {
        val someParties = DeclarationParties(None, Some(Party(Some("Barney"), None, Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
        val declaration = Declaration(parties = someParties)

        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)

        (xmlElement \ "Declaration" \ "Exporter" \ "ID").length mustBe 0 withClue ("Found unexpected Exporter ID tag")
      }

      "omit Exporter address tag if no exporter address provided" in {
        val someParties = DeclarationParties(None, Some(Party(Some("Barney"), Some("GB12345678A"), None)))
        val declaration = Declaration(parties = someParties)

        val xmlElement: Elem = DeclarationXml.fromImportDeclaration(declaration)

        (xmlElement \ "Declaration" \ "Exporter" \ "Address").length mustBe 0 withClue ("Found unexpected Exporter Address tag")
      }
    }
  }
}
