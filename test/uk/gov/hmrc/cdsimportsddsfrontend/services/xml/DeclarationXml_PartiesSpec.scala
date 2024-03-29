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

import org.scalatest.{AppendedClues, MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

import scala.xml.Elem

class DeclarationXml_PartiesSpec extends WordSpec with MustMatchers with AppendedClues {

  "Declaration XML builder" when {
    "transforming parties data" when {
      val someParties = DeclarationPartiesViewModel(
        Some(Party(None, Some("GB987654321"), None)),
        Some(Party(Some("Fred"), Some("GB12345678F"), Some(Address(Some("123 Girder Street"), Some("Edinburgh"), Some("SC"), Some("E12 4GG"), None)))),
        Some(Party(Some("Barney"), Some("GB12345678A"), Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))),
        Some(Party(Some("Wilma"), Some("GB14141414"), Some(Address(Some("14 The Calls"), Some("Leeds"), Some("GB"), Some("LS1 1AA"), None)), Some("0113 25 26 27"))),
        Some(Party(Some("Pebbles"), Some("GB4587342"), Some(Address(Some("21 Mountain View"), Some("York"), Some("GB"), Some("YK1 7ZX"), None)), Some("0114 123 456"))),
        Seq(AuthorisationHolder(Some("Shaggy"), Some("DUDE")), AuthorisationHolder(Some("Scoob"), Some("DOG"))),
        Seq(DomesticDutyTaxParty(Some("Velma"), Some("GIRL")), DomesticDutyTaxParty(Some("Fred"), Some("GUY")))
      )
      val declaration = Declaration(parties = someParties)

      "transforming Declarant" should {
        "populate declarant ID" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "Declarant" \ "ID").text mustBe "GB987654321"
        }

        "omit Declarant tag if no declarant provided" in {
          val someParties = DeclarationPartiesViewModel(declarant = None)
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
          val someParties = DeclarationPartiesViewModel(exporter = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter").length mustBe 0 withClue ("Found unexpected Exporter tag")
        }

        "omit Exporter Name tag if no exporter name provided" in {
          val someParties = DeclarationPartiesViewModel(exporter = Some(Party(None, Some("GB12345678A"), Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter" \ "Name").length mustBe 0 withClue ("Found unexpected Exporter Name tag")
        }

        "omit Exporter ID tag if no exporter ID provided" in {
          val someParties = DeclarationPartiesViewModel(exporter = Some(Party(Some("Barney"), None, Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "Exporter" \ "ID").length mustBe 0 withClue ("Found unexpected Exporter ID tag")
        }

        "omit Exporter address tag if no exporter address provided" in {
          val someParties = DeclarationPartiesViewModel(exporter = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
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
          val someParties = DeclarationPartiesViewModel(importer = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer").length mustBe 0 withClue ("Found unexpected Importer tag")
        }

        "omit Importer Name tag if no importer name provided" in {
          val someParties = DeclarationPartiesViewModel(importer = Some(Party(None, Some("GB12345678A"), Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "Name").length mustBe 0 withClue ("Found unexpected Importer Name tag")
        }

        "omit Importer ID tag if no importer ID provided" in {
          val someParties = DeclarationPartiesViewModel(importer = Some(Party(Some("Barney"), None, Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Importer" \ "ID").length mustBe 0 withClue ("Found unexpected Importer ID tag")
        }

        "omit Importer address tag if no importer address provided" in {
          val someParties = DeclarationPartiesViewModel(importer = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
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
          val someParties = DeclarationPartiesViewModel(buyer = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer").length mustBe 0 withClue ("Found unexpected Buyer tag")
        }

        "omit Buyer Name tag if no buyer name provided" in {
          val someParties = DeclarationPartiesViewModel(buyer = Some(Party(None, Some("GB12345678A"), Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Name").length mustBe 0 withClue ("Found unexpected Buyer Name tag")
        }

        "omit Buyer ID tag if no buyer ID provided" in {
          val someParties = DeclarationPartiesViewModel(buyer = Some(Party(Some("Barney"), None, Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "ID").length mustBe 0 withClue ("Found unexpected Buyer ID tag")
        }

        "omit Buyer address tag if no buyer address provided" in {
          val someParties = DeclarationPartiesViewModel(buyer = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Address").length mustBe 0 withClue ("Found unexpected Buyer Address tag")
        }

        "omit Buyer Communication tag if no buyer phone number provided" in {
          val someParties = DeclarationPartiesViewModel(buyer = Some(Party(Some("Barney"), Some("GB12345678A"), None, None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Buyer" \ "Communication").length mustBe 0 withClue ("Found unexpected Buyer Communication tag")
        }
      }

      "transforming Seller" should {
        "populate header-level seller name and ID" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Name").text mustBe "Pebbles"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "ID").text mustBe "GB4587342"
        }

        "populate header-level seller address" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Address" \ "Line").text mustBe "21 Mountain View"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Address" \ "CityName").text mustBe "York"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Address" \ "CountryCode").text mustBe "GB"
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Address" \ "PostcodeID").text mustBe "YK1 7ZX"
        }

        "populate header-level seller phone number" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Communication" \ "ID").text mustBe "0114 123 456"
        }

        "omit Seller tag if no seller provided" in {
          val someParties = DeclarationPartiesViewModel(seller = None)
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller").length mustBe 0 withClue ("Found unexpected Seller tag")
        }

        "omit Seller Name tag if no seller name provided" in {
          val someParties = DeclarationPartiesViewModel(seller = Some(Party(None, Some("GB12345678A"), Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Name").length mustBe 0 withClue ("Found unexpected Seller Name tag")
        }

        "omit Seller ID tag if no seller ID provided" in {
          val someParties = DeclarationPartiesViewModel(seller = Some(Party(Some("Barney"), None, Some(Address(Some("123 Foobar Lane"), Some("Glasgow"), Some("GB"), Some("G12 4GG"), None)))))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "ID").length mustBe 0 withClue ("Found unexpected Seller ID tag")
        }

        "omit Seller address tag if no seller address provided" in {
          val someParties = DeclarationPartiesViewModel(seller = Some(Party(Some("Barney"), Some("GB12345678A"), None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Address").length mustBe 0 withClue ("Found unexpected Seller Address tag")
        }

        "omit Seller Communication tag if no seller phone number provided" in {
          val someParties = DeclarationPartiesViewModel(seller = Some(Party(Some("Barney"), Some("GB12345678A"), None, None)))
          val declaration = Declaration(parties = someParties)

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "GoodsShipment" \ "Seller" \ "Communication").length mustBe 0 withClue ("Found unexpected Seller Communication tag")
        }
      }

      "transforming AuthorisationHolder" should {

        "populate header-level AuthorisationHolders" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "AuthorisationHolder" \ "ID").map(_.text) mustBe Seq("Shaggy", "Scoob")
          (xmlElement \ "Declaration" \ "AuthorisationHolder" \ "CategoryCode").map(_.text) mustBe Seq("DUDE", "DOG")
        }

        "omit AuthorisationHolder tag if no authorisationHolder provided" in {
          val declaration = Declaration(parties = DeclarationPartiesViewModel(authorisationHolders = Seq()))

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "AuthorisationHolder").length mustBe 0 withClue ("Found unexpected AuthorisationHolder tag")
        }

        "omit AuthorisationHolder ID tag if not provided" in {
          val declaration = Declaration(parties = DeclarationPartiesViewModel(authorisationHolders = Seq(AuthorisationHolder(None, Some("FOO")))))

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "AuthorisationHolder" \ "ID").length mustBe 0 withClue(
            "Found unexpected AuthorisationHolder ID tag")
        }

        "omit AuthorisationHolder CategoryCode tag if not provided" in {
          val declaration = Declaration(parties = DeclarationPartiesViewModel(authorisationHolders = Seq(AuthorisationHolder(Some("FOO"), None))))

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          (xmlElement \ "Declaration" \ "AuthorisationHolder" \ "CategoryCode").length mustBe 0 withClue(
            "Found unexpected AuthorisationHolder CategoryCode tag")
        }
      }

      "transforming DomesticDutyTaxParty" should {

        "populate item-level DomesticDutyTaxParties" in {
          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          val item = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
          (item \ "DomesticDutyTaxParty" \ "ID").map(_.text) mustBe Seq("Velma", "Fred")
          (item \ "DomesticDutyTaxParty" \ "RoleCode").map(_.text) mustBe Seq("GIRL", "GUY")
        }

        "omit DomesticDutyTaxParty tag if no domesticDutyTaxParty provided" in {
          val declaration = Declaration(parties = DeclarationPartiesViewModel(domesticDutyTaxParties = Seq()))

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          val item = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
          (item \ "DomesticDutyTaxParty").length mustBe 0 withClue ("Found unexpected DomesticDutyTaxParty tag")
        }

        "omit DomesticDutyTaxParty ID tag if not provided" in {
          val declaration = Declaration(parties = DeclarationPartiesViewModel(domesticDutyTaxParties = Seq(DomesticDutyTaxParty(None, Some("FOO")))))

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          val item = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
          (item \ "DomesticDutyTaxParty" \ "ID").length mustBe 0 withClue("Found unexpected DomesticDutyTaxParty ID tag")
        }

        "omit DomesticDutyTaxParty RoleCode tag if not provided" in {
          val declaration = Declaration(parties = DeclarationPartiesViewModel(domesticDutyTaxParties = Seq(DomesticDutyTaxParty(Some("FOO"), None))))

          val xmlElement: Elem = (new DeclarationXml).fromImportDeclaration(declaration)

          val item = xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem"
          (item \ "DomesticDutyTaxParty" \ "RoleCode").length mustBe 0 withClue("Found unexpected DomesticDutyTaxParty CategoryCode tag")
        }
      }
    }
  }
}
