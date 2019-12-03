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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Classification
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ClassificationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class ClassificationXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "Classification XML writer" should {
    "generate the Classification XML element" when {
      "all values are present" in {
        val classification = Classification(id = Some("76071111"), identificationTypeCode = Some("TSP"))
        val expectedXml = <Classification><ID>76071111</ID><IdentificationTypeCode>TSP</IdentificationTypeCode></Classification>
        classification.toXmlOption shouldBe Some(expectedXml)
      }

      "id is present" in {
        val classification = Classification(id = Some("76071111"), identificationTypeCode = None)
        val expectedXml = <Classification><ID>76071111</ID></Classification>
        classification.toXmlOption shouldBe Some(expectedXml)
      }

      "identificationTypeCode is present" in {
        val classification = Classification(id = None, identificationTypeCode = Some("TSP"))
        val expectedXml = <Classification><IdentificationTypeCode>TSP</IdentificationTypeCode></Classification>
        classification.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the Classification XML element" when {
      "none of the child values are present" in {
        val classification = Classification(None, None)
        classification.toXmlOption shouldBe None
      }
    }
  }
}
