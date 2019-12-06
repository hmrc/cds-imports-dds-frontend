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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{CurrencyAmount, InvoiceLine}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.InvoiceLineXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class InvoiceLineXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "InvoiceLine XML writer" should {
    "generate the InvoiceLine XML element" when {
      "all values are present" in {

        val invoiceLine = InvoiceLine(Some(CurrencyAmount("EUR", "100")))
        val expectedXml =  <InvoiceLine><ItemChargeAmount currencyID="EUR">100</ItemChargeAmount></InvoiceLine>
        invoiceLine.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the InvoiceLine XML element" when {
      "non of the child values are present" in {
        val invoiceLine = InvoiceLine(itemChargeAmount = None)
        invoiceLine.toXmlOption shouldBe None
      }
    }
  }
}
