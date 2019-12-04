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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{DutyTaxFree, Payment}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.DutyTaxFreeXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

import scala.xml.Utility

class DutyTaxFreeXmlWriterSpec extends WordSpec with Matchers {

  "DutyTaxFree XML writer" should {
    "generate the DutyTaxFree XML element" when {
      "all values are present" in {
        val dutyTaxFree = DutyTaxFree(dutyRegimeCode = Some("100"), quotaOrderId = Some("123"), payment = Some(Payment("E")))
        val expectedXml = Utility.trim(
          <DutyTaxFree>
            <DutyRegimeCode>100</DutyRegimeCode>
            <QuotaOrderId>123</QuotaOrderId>
            <Payment>
              <MethodCode>E</MethodCode>
            </Payment>
          </DutyTaxFree>
        )
        dutyTaxFree.toXmlOption shouldBe Some(expectedXml)
      }

      "dutyRegimeCode is present" in {
        val dutyTaxFree = DutyTaxFree(dutyRegimeCode = Some("100"), quotaOrderId = None, payment = None)
        val expectedXml = <DutyTaxFree><DutyRegimeCode>100</DutyRegimeCode></DutyTaxFree>
        dutyTaxFree.toXmlOption shouldBe Some(expectedXml)
      }

      "quotaOrderId is present" in {
        val dutyTaxFree = DutyTaxFree(dutyRegimeCode = None, quotaOrderId = Some("123"), payment = None)
        val expectedXml = <DutyTaxFree><QuotaOrderId>123</QuotaOrderId></DutyTaxFree>
        dutyTaxFree.toXmlOption shouldBe Some(expectedXml)
      }

      "payment is present" in {
        val dutyTaxFree = DutyTaxFree(dutyRegimeCode = None, quotaOrderId = None, payment = Some(Payment("E")))
        val expectedXml = <DutyTaxFree><Payment><MethodCode>E</MethodCode></Payment></DutyTaxFree>
        dutyTaxFree.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the DutyTaxFree XML element" when {
      "none of the child values are present" in {
        val dutyTaxFree = DutyTaxFree(None, None, None)
        dutyTaxFree.toXmlOption shouldBe None
      }
    }
  }
}
