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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Payment
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.PaymentXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class PaymentXmlWriterSpec extends WordSpec with Matchers {

  "Payment XML writer" should {
    "generate the Payment XML element" when {
      "the methodCode value is present" in {

        val payment = Payment("E")
        val expectedXml = <Payment><MethodCode>E</MethodCode></Payment>
        payment.toXml shouldBe Some(expectedXml)
      }
    }
  }
}
