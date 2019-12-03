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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Packaging
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.PackagingXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class PackagingXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "Packaging XML writer" should {
    "generate the Packaging XML element" when {
      "all values are present" in {

        val packaging = Packaging(typeCode = Some("TC"), quantityQuantity = Some("88"), marksNumberId = Some("MK123"))
        val expectedXml = <Packaging><SequenceNumeric>1</SequenceNumeric><MarksNumbersID>MK123</MarksNumbersID><QuantityQuantity>88</QuantityQuantity><TypeCode>TC</TypeCode></Packaging>
        packaging.toXmlOption shouldBe Some(expectedXml)
      }

      "MarksNumbersID is the only value present" in {

        val packaging = Packaging(marksNumberId = Some("MK1234"))
        val expectedXml = <Packaging><SequenceNumeric>1</SequenceNumeric><MarksNumbersID>MK1234</MarksNumbersID></Packaging>
        packaging.toXmlOption shouldBe Some(expectedXml)
      }

      "QuantityQuantity is the only value present" in {

        val packaging = Packaging(quantityQuantity = Some("99"))
        val expectedXml = <Packaging><SequenceNumeric>1</SequenceNumeric><QuantityQuantity>99</QuantityQuantity></Packaging>
        packaging.toXmlOption shouldBe Some(expectedXml)
      }

      "TypeCode is the only value present" in {

        val packaging = Packaging(typeCode = Some("TC1"))
        val expectedXml = <Packaging><SequenceNumeric>1</SequenceNumeric><TypeCode>TC1</TypeCode></Packaging>
        packaging.toXmlOption shouldBe Some(expectedXml)
      }

    }

    "not generate the Packaging XML element" when {
      "non of the child values are present" in {
        val packaging = Packaging()
        packaging.toXmlOption shouldBe None
      }
    }
  }
}
