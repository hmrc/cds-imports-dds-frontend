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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Origin
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.OriginXmlWriter._

class OriginXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "Origin XM writer" should {
    "generate the Origin XML element" when {
      "all values are present" in {

        val origin = Origin(countryCode = Some("GB"), typeCode = Some("99"))
        val expectedXml =  <Origin><CountryCode>GB</CountryCode><TypeCode>99</TypeCode></Origin>
        origin.toXml shouldBe Some(expectedXml)
      }

      "country code is present" in {
        val origin = Origin(countryCode = Some("GB"), typeCode = None)
        val expectedXml =  <Origin><CountryCode>GB</CountryCode></Origin>
        origin.toXml shouldBe Some(expectedXml)
      }

      "type code is present" in {
        val origin = Origin(countryCode = None, typeCode = Some("99"))
        val expectedXml =  <Origin><TypeCode>99</TypeCode></Origin>
        origin.toXml shouldBe Some(expectedXml)
      }
    }

    "not generate the Origin XML element" when {
      "non of the child values are present" in {
        val origin = Origin(countryCode = None, typeCode = None)
        origin.toXml shouldBe None
      }
    }
  }
}
