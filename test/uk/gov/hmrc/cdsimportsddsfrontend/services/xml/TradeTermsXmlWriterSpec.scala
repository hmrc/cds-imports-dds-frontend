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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.TradeTerms
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.TradeTermsXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class TradeTermsXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "TradeTerms XML writer" should {
    "generate the TradeTerms XML element" when {
      "all values are present" in {

        val tradeTerms = TradeTerms(conditionCode = Some("ABC"), locationID = Some("ID123"), locationName = Some("loc name"))
        val expectedXml =  <TradeTerms><ConditionCode>ABC</ConditionCode><LocationID>ID123</LocationID><LocationName>loc name</LocationName></TradeTerms>
        tradeTerms.toXmlOption shouldBe Some(expectedXml)
      }

      "condition code is present" in {
        val tradeTerms = TradeTerms(conditionCode = Some("ABC"), None, None)
        val expectedXml =  <TradeTerms><ConditionCode>ABC</ConditionCode></TradeTerms>
        tradeTerms.toXmlOption shouldBe Some(expectedXml)
      }

      "location id is present" in {
        val tradeTerms = TradeTerms(None, locationID = Some("ID123"), None)
        val expectedXml =  <TradeTerms><LocationID>ID123</LocationID></TradeTerms>
        tradeTerms.toXmlOption shouldBe Some(expectedXml)
      }

      "location name is present" in {
        val tradeTerms = TradeTerms(None, None, locationName = Some("loc name"))
        val expectedXml =  <TradeTerms><LocationName>loc name</LocationName></TradeTerms>
        tradeTerms.toXmlOption shouldBe Some(expectedXml)
      }

    }

    "not generate the TradeTerms XML element" when {
      "non of the child values are present" in {
        val tradeTerms = TradeTerms(None, None, None)
        tradeTerms.toXmlOption shouldBe None
      }
    }
  }
}
