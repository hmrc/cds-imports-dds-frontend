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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ChargeDeduction, CurrencyAmount}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances._

class XmlWriter_ChargeDeductionSpec extends WordSpec with Matchers with OptionValues {

  val chargeDeduction = ChargeDeduction("FOO", CurrencyAmount("SEK", "10203"))

  "ChargeDeduction.toXml" should {
    "return ChargeDeduction node with text in all child elements" in {
      val expectedXml =
        <ChargeDeduction>
          <ChargesTypeCode>FOO</ChargesTypeCode>
          <OtherChargeDeductionAmount currencyID="SEK">10203</OtherChargeDeductionAmount>
        </ChargeDeduction>
      chargeDeduction.toXmlOption shouldBe (Some(expectedXml))
    }

  }

}
