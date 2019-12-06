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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CurrencyExchange
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.CurrencyExchangeXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class CurrencyExchangeXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "CurrencyExchange XML writer" should {
    "generate the CurrencyExchange XML element" when {
      "all values are present" in {

        val origin = CurrencyExchange(Some("1.48"))
        val expectedXml =  <CurrencyExchange><RateNumeric>1.48</RateNumeric></CurrencyExchange>
        origin.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the CurrencyExchange XML element" when {
      "non of the child values are present" in {
        val origin = CurrencyExchange(rateNumeric = None)
        origin.toXmlOption shouldBe None
      }
    }
  }
}
