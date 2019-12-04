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

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.ValuationAdjustment
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ValuationAdjustmentXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class ValuationAdjustmentXmlWriterSpec extends WordSpec with Matchers {

  "ValuationAdjustment XML writer" should {
    "generate the ValuationAdjustment XML element" when {
      "all values are present" in {
        val valuationAdjustment = ValuationAdjustment(additionCode = "1234")
        val expectedXml = <ValuationAdjustment><AdditionCode>1234</AdditionCode></ValuationAdjustment>
        valuationAdjustment.toXmlOption shouldBe Some(expectedXml)
        valuationAdjustment.toXml shouldBe expectedXml
        Option(valuationAdjustment).toXml shouldBe expectedXml
      }
    }
  }
}
