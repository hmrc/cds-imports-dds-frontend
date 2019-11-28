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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.GoodsMeasure
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.GoodsMeasureXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class GoodsMeasureXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "GoodsMeasure XM writer" should {
    "generate the GoodsMeasure XML element" when {
      "all values are present" in {

        val actual = GoodsMeasure(netNetWeightMeasure = Some("101"), tariffQuantity = Some("99"), grossMassMeasure = Some("88"))
        val expectedXml =  <GoodsMeasure><GrossMassMeasure unitCode="KGM">88</GrossMassMeasure><NetNetWeightMeasure unitCode="KGM">101</NetNetWeightMeasure><TariffQuantity>99</TariffQuantity></GoodsMeasure>
        actual.toXml shouldBe Some(expectedXml)
      }

      "gross mass measure is present" in {
        val actual = GoodsMeasure(grossMassMeasure = Some("88"))
        val expectedXml =  <GoodsMeasure><GrossMassMeasure unitCode="KGM">88</GrossMassMeasure></GoodsMeasure>
        actual.toXml shouldBe Some(expectedXml)
      }

      "net net weight measure is present" in {
        val actual = GoodsMeasure(netNetWeightMeasure = Some("102"))
        val expectedXml =  <GoodsMeasure><NetNetWeightMeasure unitCode="KGM">102</NetNetWeightMeasure></GoodsMeasure>
        actual.toXml shouldBe Some(expectedXml)
      }

      "tariff quantity is present" in {
        val actual = GoodsMeasure(tariffQuantity = Some("11"))
        val expectedXml =  <GoodsMeasure><TariffQuantity>11</TariffQuantity></GoodsMeasure>
        actual.toXml shouldBe Some(expectedXml)
      }

    }

    "not generate the GoodsMeasure XML element" when {
      "non of the child values are present" in {
        val goodsMeasure = GoodsMeasure()
        goodsMeasure.toXml shouldBe None
      }
    }
  }
}
