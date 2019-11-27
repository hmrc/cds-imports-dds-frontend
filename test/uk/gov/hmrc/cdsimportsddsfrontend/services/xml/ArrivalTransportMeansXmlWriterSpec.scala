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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.ArrivalTransportMeans
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ArrivalTransportMeansXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class ArrivalTransportMeansXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "ArrivalTransportMeans XML writer" should {
    "generate the ArrivalTransportMeans XML element" when {
      "all values are present" in {
        val arrivalTransportMeans = ArrivalTransportMeans(identificationTypeCode = Some("10"), id = Some("1"))
        val expectedXml = <ArrivalTransportMeans><ID>1</ID><IdentificationTypeCode>10</IdentificationTypeCode></ArrivalTransportMeans>
        arrivalTransportMeans.toXml shouldBe Some(expectedXml)
      }

      "identificationTypeCode is present" in {
        val arrivalTransportMeans = ArrivalTransportMeans(identificationTypeCode = Some("10"), id = None)
        val expectedXml = <ArrivalTransportMeans><IdentificationTypeCode>10</IdentificationTypeCode></ArrivalTransportMeans>
        arrivalTransportMeans.toXml shouldBe Some(expectedXml)
      }

      "id is present" in {
        val arrivalTransportMeans = ArrivalTransportMeans(identificationTypeCode = None, id = Some("1"))
        val expectedXml = <ArrivalTransportMeans><ID>1</ID></ArrivalTransportMeans>
        arrivalTransportMeans.toXml shouldBe Some(expectedXml)
      }
    }

    "not generate the ArrivalTransportMeans XML element" when {
      "none of the child values are present" in {
        val arrivalTransportMeans = ArrivalTransportMeans(None, None)
        val expectedXml = <ArrivalTransportMeans><IdentificationTypeCode>10</IdentificationTypeCode><ID>1</ID></ArrivalTransportMeans>
        arrivalTransportMeans.toXml shouldBe None
      }
    }
  }
}
