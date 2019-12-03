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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.BorderTransportMeans
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.BorderTransportMeansXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class BorderTransportMeansXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "BorderTransportMeans XML writer" should {
    "generate the BorderTransportMeans XML element" when {
      "all values are present" in {
        val borderTransportMeans = BorderTransportMeans(registrationNationalityCode = Some("FR"), modeCode = Some("1"))
        val expectedXml = <BorderTransportMeans><RegistrationNationalityCode>FR</RegistrationNationalityCode><ModeCode>1</ModeCode></BorderTransportMeans>
        borderTransportMeans.toXmlOption shouldBe Some(expectedXml)
      }

      "registrationNationalityCode is present" in {
        val borderTransportMeans = BorderTransportMeans(registrationNationalityCode = Some("FR"), modeCode = None)
        val expectedXml = <BorderTransportMeans><RegistrationNationalityCode>FR</RegistrationNationalityCode></BorderTransportMeans>
        borderTransportMeans.toXmlOption shouldBe Some(expectedXml)
      }

      "modeCode is present" in {
        val borderTransportMeans = BorderTransportMeans(registrationNationalityCode = None, modeCode = Some("1"))
        val expectedXml = <BorderTransportMeans><ModeCode>1</ModeCode></BorderTransportMeans>
        borderTransportMeans.toXmlOption shouldBe Some(expectedXml)
      }
    }

    "not generate the BorderTransportMeans XML element" when {
      "none of the child values are present" in {
        val borderTransportMeans = BorderTransportMeans(None, None)
        borderTransportMeans.toXmlOption shouldBe None
      }
    }
  }
}
