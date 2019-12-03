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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Address, GoodsLocation}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.GoodsLocationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class GoodsLocationXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "GoodsLocation XML writer" should {
    "generate the GoodsLocation XML element" when {
      "all values are present" in {
        val goodsLocation = GoodsLocation(name = Some("FXFXT"), typeCode = Some("A"), address = Some(Address(None, None, Some("GB"), None, Some("U"))))
        val expectedXml = <GoodsLocation><Name>FXFXT</Name><TypeCode>A</TypeCode><Address><TypeCode>U</TypeCode><CountryCode>GB</CountryCode></Address></GoodsLocation>
        goodsLocation.toXmlOption shouldBe Some(expectedXml)
      }

      "name is present" in {
        val goodsLocation = GoodsLocation(name = Some("FXFXT"), typeCode = None, address = None)
        val expectedXml = <GoodsLocation><Name>FXFXT</Name></GoodsLocation>
        goodsLocation.toXmlOption shouldBe Some(expectedXml)
      }

      "type code is present" in {
        val goodsLocation = GoodsLocation(name = None, typeCode = Some("A"), address = None)
        val expectedXml = <GoodsLocation><TypeCode>A</TypeCode></GoodsLocation>
        goodsLocation.toXmlOption shouldBe Some(expectedXml)
      }

      "address is present" in {
        val goodsLocation = GoodsLocation(name = None, typeCode = None, address = Some(Address(None, None, Some("GB"), None, Some("U"))))
        val expectedXml = <GoodsLocation><Address><TypeCode>U</TypeCode><CountryCode>GB</CountryCode></Address></GoodsLocation>
        goodsLocation.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the Goods Location XML element" when {
      "none of the child values are present" in {
        val goodsLocation = GoodsLocation(name = None, typeCode = None, address = None)
        goodsLocation.toXmlOption shouldBe None
      }
    }
  }
}
