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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.WriteOff
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances.writeOffWriter

class WriteOffXmlWriterSpec extends WordSpec with Matchers {

  "WriteOff XML writer" should {
    "generate the WriteOff XML element" when {
      "the quantityQuantity value and unitCode value is present" in {
        val writeOff = WriteOff(Some("100"), Some("Qualifies"))
        val expectedXml = <WriteOff><QuantityQuantity unitCode="Qualifies">100</QuantityQuantity></WriteOff>
        writeOff.toXmlOption shouldBe Some(expectedXml)
      }

      "the quantityQuantity value is not present but the unitCode value is present" in {
        val writeOff = WriteOff(None, Some("Qualifies"))
        writeOff.toXmlOption shouldBe None
      }

      "the quantityQuantity value is present but the unitCode value is not present" in {
        val writeOff = WriteOff(Some("100"), None)
        val expectedXml = <WriteOff><QuantityQuantity>100</QuantityQuantity></WriteOff>
        writeOff.toXmlOption shouldBe Some(expectedXml)
      }
    }
  }
}
