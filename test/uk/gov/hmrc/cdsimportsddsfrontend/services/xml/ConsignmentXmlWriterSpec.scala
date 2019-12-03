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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Address, ArrivalTransportMeans, Consignment, GoodsLocation, LoadingLocation}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.ConsignmentXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

import scala.xml.Utility

class ConsignmentXmlWriterSpec extends WordSpec with Matchers with OptionValues {

  "Consignment XML writer" should {
    "generate the Consignment XML element" when {
      "all values are present" in {
        val consignment = Consignment(containerCode = Some("0"),
          arrivalTransportMeans = Some(ArrivalTransportMeans(identificationTypeCode = Some("10"), id = Some("1"))),
          goodsLocation = Some(GoodsLocation(name = Some("FXFXT"), typeCode = Some("A"), address = Some(Address(None, None, Some("GB"), None, Some("U"))))),
          loadingLocation = Some(LoadingLocation("LGW"))
        )

        val expectedXml = Utility.trim({
          <Consignment>
            <ContainerCode>0</ContainerCode>
            <ArrivalTransportMeans><ID>1</ID><IdentificationTypeCode>10</IdentificationTypeCode></ArrivalTransportMeans>
            <GoodsLocation><Name>FXFXT</Name><TypeCode>A</TypeCode><Address><TypeCode>U</TypeCode><CountryCode>GB</CountryCode></Address></GoodsLocation>
            <LoadingLocation><ID>LGW</ID></LoadingLocation>
          </Consignment>
        })

        consignment.toXml shouldBe Some(expectedXml)
      }

      "container code is present" in {
        val consignment = Consignment(containerCode = Some("0"), arrivalTransportMeans = None, goodsLocation = None, loadingLocation = None)
        val expectedXml = <Consignment><ContainerCode>0</ContainerCode></Consignment>

        consignment.toXml shouldBe Some(expectedXml)
      }

      "arrival transport means is present" in {
        val consignment = Consignment(containerCode = None,
          arrivalTransportMeans = Some(ArrivalTransportMeans(identificationTypeCode = Some("10"), id = Some("1"))),
          goodsLocation = None,
          loadingLocation = None)

        val expectedXml = Utility.trim({
          <Consignment>
            <ArrivalTransportMeans><ID>1</ID><IdentificationTypeCode>10</IdentificationTypeCode></ArrivalTransportMeans>
          </Consignment>
        })

        consignment.toXml shouldBe Some(expectedXml)
      }

      "goods location is present" in {
        val consignment = Consignment(containerCode = None,
          arrivalTransportMeans = None,
          goodsLocation = Some(GoodsLocation(name = Some("FXFXT"), typeCode = Some("A"), address = Some(Address(None, None, Some("GB"), None, Some("U"))))),
          loadingLocation = None)

        val expectedXml = Utility.trim({
          <Consignment>
            <GoodsLocation>
              <Name>FXFXT</Name>
              <TypeCode>A</TypeCode>
              <Address>
                <TypeCode>U</TypeCode>
                <CountryCode>GB</CountryCode>
              </Address>
            </GoodsLocation>
          </Consignment>
        })

        consignment.toXml shouldBe Some(expectedXml)
      }

      "loading location is present" in {
        val consignment = Consignment(
          containerCode = None,
          arrivalTransportMeans = None,
          goodsLocation = None,
          loadingLocation = Some(LoadingLocation("LGW"))
        )

        val expectedXml = Utility.trim({
          <Consignment>
            <LoadingLocation><ID>LGW</ID></LoadingLocation>
          </Consignment>
        })

        consignment.toXml shouldBe Some(expectedXml)
      }
    }

    "not generate the Consignment XML element" when {
      "none of the child values are present" in {
        val consignment = Consignment(None, None, None, None)
        consignment.toXml shouldBe None
      }
    }
  }
}
