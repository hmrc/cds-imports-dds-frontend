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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Address
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.AddressXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class AddressXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "Address XML writer" should {
    "generate the Address XML element" when {
      "all values are present" in {
        val address = Address(streetAndNumber= Some("StreetNumber"), city = Some("Paris"), countryCode = Some("FR"), postcode = Some("75018"), typeCode = Some("U"))
        val expectedXml = <Address><TypeCode>U</TypeCode><CityName>Paris</CityName><CountryCode>FR</CountryCode><Line>StreetNumber</Line><PostcodeID>75018</PostcodeID></Address>
        address.toXml shouldBe Some(expectedXml)
      }

      "streetAndNumber is present" in {
        val address = Address(streetAndNumber= Some("StreetNumber"), city = None, countryCode = None, postcode = None, typeCode = None)
        val expectedXml = <Address><Line>StreetNumber</Line></Address>
        address.toXml shouldBe Some(expectedXml)
      }

      "city is present" in {
        val address = Address(streetAndNumber= None, city = Some("Paris"), countryCode = None, postcode = None, typeCode = None)
        val expectedXml = <Address><CityName>Paris</CityName></Address>
        address.toXml shouldBe Some(expectedXml)
      }

      "country code is present" in {
        val address = Address(streetAndNumber= None, city = None, countryCode = Some("GB"), postcode = None, typeCode = None)
        val expectedXml = <Address><CountryCode>GB</CountryCode></Address>
        address.toXml shouldBe Some(expectedXml)
      }

      "post code is present" in {
        val address = Address(streetAndNumber= None, city = None, countryCode = None, postcode = Some("75018"), typeCode = None)
        val expectedXml = <Address><PostcodeID>75018</PostcodeID></Address>
        address.toXml shouldBe Some(expectedXml)
      }

      "type code is present" in {
        val address = Address(streetAndNumber= None, city = None, countryCode = None, postcode = None, typeCode = Some("U"))
        val expectedXml = <Address><TypeCode>U</TypeCode></Address>
        address.toXml shouldBe Some(expectedXml)
      }
    }

    "not generate the Address XML element" when {
      "none of the child values are present" in {
        val address = Address(streetAndNumber= None, city = None, countryCode = None, postcode = None, typeCode = None)
        address.toXml shouldBe None
      }
    }
  }
}
