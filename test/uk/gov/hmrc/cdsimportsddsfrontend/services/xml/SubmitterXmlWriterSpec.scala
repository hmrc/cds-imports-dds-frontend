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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Submitter
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances.submitterWriter

class SubmitterXmlWriterSpec extends WordSpec with Matchers {

  "Submitter XML writer" should {
    "generate the Submitter XML element" when {
      "the name value is present" in {
        val submitter = Submitter(Some("E"))
        val expectedXml = <Submitter><Name>E</Name></Submitter>
        submitter.toXmlOption shouldBe Some(expectedXml)
      }

      "the name value is not present" in {
        val submitter = Submitter(None)
        submitter.toXmlOption shouldBe None
      }
    }
  }
}
