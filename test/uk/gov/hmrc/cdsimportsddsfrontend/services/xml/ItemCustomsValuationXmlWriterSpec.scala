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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ChargeDeduction, CurrencyAmount, ItemCustomsValuation}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ItemCustomsValuationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

import scala.xml.Utility

class ItemCustomsValuationXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "ItemCustomsValuation XML writer" should {
    "generate the ItemCustomsValuation XML element" when {
      "all values are present" in {

        val itemCustomsValuation = ItemCustomsValuation(
          methodCode = Some("12"),
          chargeDeduction = Some(ChargeDeduction(typeCode = "AS",
            currencyAmount = CurrencyAmount(currency = "GBP", amount = "100")))
        )

        val expectedXml = Utility.trim({
          <CustomsValuation>
            <MethodCode>12</MethodCode>
            <ChargeDeduction>
              <ChargesTypeCode>AS</ChargesTypeCode>
              <OtherChargeDeductionAmount currencyID="GBP">100</OtherChargeDeductionAmount>
            </ChargeDeduction>
          </CustomsValuation>
        })

        itemCustomsValuation.toXml.map(Utility.trim(_)) shouldBe Some(expectedXml)
      }
    }

    "not generate the ItemCustomsValuation XML element" when {
      "non of the child values are present" in {
        val origin = ItemCustomsValuation(methodCode =  None)
        origin.toXml shouldBe None
      }
    }

  }

}
