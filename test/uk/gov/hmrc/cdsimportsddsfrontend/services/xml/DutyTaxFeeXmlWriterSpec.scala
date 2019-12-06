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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{DutyTaxFee, Payment}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.DutyTaxFeeXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

import scala.xml.Utility

class DutyTaxFeeXmlWriterSpec extends WordSpec with Matchers {

  "DutyTaxFee XML writer" should {
    "generate the DutyTaxFee XML element" when {
      "all values are present" in {
        val dutyTaxFee = DutyTaxFee(dutyRegimeCode = Some("100"), payment = Some(Payment("E")))
        val expectedXml = Utility.trim(
          <DutyTaxFee>
            <DutyRegimeCode>100</DutyRegimeCode>
            <Payment>
              <MethodCode>E</MethodCode>
            </Payment>
          </DutyTaxFee>
        )
        dutyTaxFee.toXmlOption shouldBe Some(expectedXml)
      }

      "dutyRegimeCode is present" in {
        val dutyTaxFee = DutyTaxFee(dutyRegimeCode = Some("100"), payment = None)
        val expectedXml = <DutyTaxFee><DutyRegimeCode>100</DutyRegimeCode></DutyTaxFee>
        dutyTaxFee.toXmlOption shouldBe Some(expectedXml)
      }

      "payment is present" in {
        val dutyTaxFee = DutyTaxFee(dutyRegimeCode = None, payment = Some(Payment("E")))
        val expectedXml = <DutyTaxFee><Payment><MethodCode>E</MethodCode></Payment></DutyTaxFee>
        dutyTaxFee.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the DutyTaxFee XML element" when {
      "none of the child values are present" in {
        val dutyTaxFee = DutyTaxFee(None, None)
        dutyTaxFee.toXmlOption shouldBe None
      }
    }
  }
}
