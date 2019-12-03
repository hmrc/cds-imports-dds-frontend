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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.ExportCountry
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ExportCountryXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class ExportCountryXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "ExportCountry XML writer" should {
    "generate the ExportCountry XML element" when {
      "the ID value is present" in {

        val exportCountry = ExportCountry(id = Some("GB"))
        val expectedXml =  <ExportCountry><ID>GB</ID></ExportCountry>
        exportCountry.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the ExportCountry XML element" when {
      "the ID value is not present" in {
        val exportCountry = ExportCountry(id = None)
        exportCountry.toXmlOption shouldBe None
      }
    }
  }
}
