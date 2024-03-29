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

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.LoadingLocation
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.LoadingLocationXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

class LoadingLocationXmlWriterSpec extends WordSpec with Matchers {

  "LoadingLocation XML writer" should {
    "generate the LoadingLocation XML element" when {
      "the ID value is present" in {

        val loadingLocation = LoadingLocation("LHR")
        val expectedXml =  <LoadingLocation><ID>LHR</ID></LoadingLocation>
        loadingLocation.toXmlOption shouldBe Some(expectedXml)
      }
    }
  }
}
