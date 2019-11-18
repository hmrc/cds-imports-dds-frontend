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
    "transforming parties data" when {
      val someParties = DeclarationParties(
        Some(Party(None, Some("GB987654321"), None)),
        Some(Party(Some("Fred"), Some("GB12345678F"), Some(Address("123 Girder Street", "Edinburgh", "SC", "E12 4GG")))),
        Some(Party(Some("Barney"), Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))),
        Some(Party(Some("Wilma"), Some("GB14141414"), Some(Address("14 The Calls", "Leeds", "GB", "LS1 1AA")), Some("0113 25 26 27")))
      )
      val declaration = Declaration(parties = someParties)

      "transforming Declarant" should {
        "populate declarant ID" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "Declarant" \ "ID").text mustBe "GB987654321"
        }

        "omit Declarant tag if no declarant provided" in {
          val someParties = DeclarationParties(declarant = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Declarant").length mustBe 0 withClue ("Found unexpected Declarant tag")
        }
      }

      "transforming Exporter" should {
        "populate header-level exporter name and ID" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "Exporter" \ "Name").text mustBe "Barney"
          (xmlElement \ "Declaration" \ "Exporter" \ "ID").text mustBe "GB12345678A"
        }

        "populate header-level exporter address" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "Line").text mustBe "123 Foobar Lane"
          (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "CityName").text mustBe "Glasgow"
          (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "CountryCode").text mustBe "GB"
          (xmlElement \ "Declaration" \ "Exporter" \ "Address" \ "PostcodeID").text mustBe "G12 4GG"
        }

        "omit Exporter tag if no exporter provided" in {
          val someParties = DeclarationParties(exporter = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter").length mustBe 0 withClue ("Found unexpected Exporter tag")
        }

        "omit Exporter Name tag if no exporter name provided" in {
          val someParties = DeclarationParties(exporter = Some(Party(None, Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter" \ "Name").length mustBe 0 withClue ("Found unexpected Exporter Name tag")
        }

        "omit Exporter ID tag if no exporter ID provided" in {
          val someParties = DeclarationParties(exporter = Some(Party(Some("Barney"), None, Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter" \ "ID").length mustBe 0 withClue ("Found unexpected Exporter ID tag")
        }

        "omit Exporter address tag if no exporter address provided" in {
          val someParties = DeclarationParties(exporter = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter" \ "Address").length mustBe 0 withClue ("Found unexpected Exporter Address tag")
        }
      }

      "transforming Importer" should {
        "populate header-level importer name and ID" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Name").text mustBe "Fred"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "ID").text mustBe "GB12345678F"
        }

        "populate header-level importer address" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Address" \ "Line").text mustBe "123 Girder Street"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Address" \ "CityName").text mustBe "Edinburgh"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Address" \ "CountryCode").text mustBe "SC"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Address" \ "PostcodeID").text mustBe "E12 4GG"
        }

        "omit Importer tag if no importer provided" in {
          val someParties = DeclarationParties(importer = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer").length mustBe 0 withClue ("Found unexpected Importer tag")
        }

        "omit Importer Name tag if no importer name provided" in {
          val someParties = DeclarationParties(importer = Some(Party(None, Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Name").length mustBe 0 withClue ("Found unexpected Importer Name tag")
        }

        "omit Importer ID tag if no importer ID provided" in {
          val someParties = DeclarationParties(importer = Some(Party(Some("Barney"), None, Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "ID").length mustBe 0 withClue ("Found unexpected Importer ID tag")
        }

        "omit Importer address tag if no importer address provided" in {
          val someParties = DeclarationParties(importer = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Address").length mustBe 0 withClue ("Found unexpected Importer Address tag")
        }
      }

      "transforming Buyer" should {
        "populate header-level buyer name and ID" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Name").text mustBe "Wilma"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "ID").text mustBe "GB14141414"
        }

        "populate header-level buyer address" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Address" \ "Line").text mustBe "14 The Calls"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Address" \ "CityName").text mustBe "Leeds"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Address" \ "CountryCode").text mustBe "GB"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Address" \ "PostcodeID").text mustBe "LS1 1AA"
        }

        "populate header-level buyer phone number" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Communication" \ "ID").text mustBe "0113 25 26 27"
        }

        "omit Buyer tag if no buyer provided" in {
          val someParties = DeclarationParties(buyer = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer").length mustBe 0 withClue ("Found unexpected Buyer tag")
        }

        "omit Buyer Name tag if no buyer name provided" in {
          val someParties = DeclarationParties(buyer = Some(Party(None, Some("GB12345678A"), Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Name").length mustBe 0 withClue ("Found unexpected Buyer Name tag")
        }

        "omit Buyer ID tag if no buyer ID provided" in {
          val someParties = DeclarationParties(buyer = Some(Party(Some("Barney"), None, Some(Address("123 Foobar Lane", "Glasgow", "GB", "G12 4GG")))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "ID").length mustBe 0 withClue ("Found unexpected Buyer ID tag")
        }

        "omit Buyer address tag if no buyer address provided" in {
          val someParties = DeclarationParties(buyer = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Address").length mustBe 0 withClue ("Found unexpected Buyer Address tag")
        }

        "omit Buyer Communication tag if no buyer phone number provided" in {
          val someParties = DeclarationParties(buyer = Some(Party(Some("Barney"), Some("GB12345678A"), None, None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Communication").length mustBe 0 withClue ("Found unexpected Buyer Communication tag")
        }
      }
    }
  }
}
