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

import org.scalatest.{Matchers, OptionValues, WordSpec}
import org.scalatest.{MustMatchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ObligationGuaranteeXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._


import scala.xml.Elem
class DeclarationXml_ObligationGuaranteeSpec extends WordSpec with MustMatchers {

  "ObligationGuarantee node" should {
    "be in the generated XML" when {
      "the amountAmount is present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(Some(CurrencyAmount("GBP", "1")), None, None, None, None, None))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "AmountAmount").text mustBe "1"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "AmountAmount" \ "@currencyID").head.text mustBe "GBP"
      }

      "the id is present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(None, Some("2"), None, None, None, None))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "ID").text mustBe "2"
      }

      "the referenceId is present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(None, None, Some("3"), None, None, None))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "ReferenceID").text mustBe "3"
      }

      "the securityDetailsCode is present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(None, None, None, Some("4"), None, None))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "SecurityDetailsCode").text mustBe "4"
      }

      "the accessCode is present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(None, None, None, None, Some("5"), None))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "AccessCode").text mustBe "5"
      }

      "the guaranteeOffice is present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(None, None, None, None, None, Some("6")))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "GuaranteeOffice").text mustBe "6"
      }

      "the amountAmount, id, referenceId, securityDetailsCode, accessCode, and guaranteeOffice are present in the data" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(Some(CurrencyAmount("EUR", "11")), Some("12"), Some("13"), Some("14"), Some("15"), Some("Shipley")))
        )
        val xml: Elem = DeclarationXml().fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee" \ "AmountAmount").text mustBe "11"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "AmountAmount" \ "@currencyID").head.text mustBe "EUR"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "ID").text mustBe "12"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "ReferenceID").text mustBe "13"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "SecurityDetailsCode").text mustBe "14"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "AccessCode").text mustBe "15"
        (xml \ "Declaration" \ "ObligationGuarantee" \ "GuaranteeOffice").text mustBe "Shipley"
      }
    }

    "be omitted from the XML" when {
      "ObligationGuarantee member data are all none" in {
        val declaration = Declaration(
          obligationGuarantee = Some(ObligationGuarantee(None, None, None, None, None, None))
        )

        val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
        (xml \ "Declaration" \ "ObligationGuarantee").length mustBe 0
      }

      "the ObligationGuarantee data itself does not exist" in {
          val declaration = Declaration(
            obligationGuarantee = None
          )

          val xml: Elem = (new DeclarationXml).fromImportDeclaration(declaration)
          (xml \ "Declaration" \ "ObligationGuarantee").length mustBe 0
      }
    }
  }
}

