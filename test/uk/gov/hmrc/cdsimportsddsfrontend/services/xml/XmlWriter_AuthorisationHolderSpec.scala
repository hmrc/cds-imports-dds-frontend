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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.AuthorisationHolder
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances._

class XmlWriter_AuthorisationHolderSpec extends WordSpec with Matchers with OptionValues {

  val authorisationHolderAll = AuthorisationHolder(
    identifier = Some("id_1"),
    categoryCode = Some("category_1")
  )

  "AuthorisationHolder.toXml" should {
    "return AuthorisationHolder node with text in all child elements" when {
      "all parameters are Some" in {
          val expectedResultAll =
            <AuthorisationHolder><ID>id_1</ID><CategoryCode>category_1</CategoryCode></AuthorisationHolder>
        authorisationHolderAll.toXml shouldBe (Some(expectedResultAll))
      }
    }

    "return no AuthorisationHolder node when all fields are None" in {
      val authorisationHolderEmpty = AuthorisationHolder(None, None)
      authorisationHolderEmpty.toXml shouldBe None
    }

    List(Some(""), None) foreach { emptyValue =>
      s"omit ID if $emptyValue" in {
        val authorisationHolderWithoutId = authorisationHolderAll.copy(identifier = emptyValue)
        val expectedResultWithoutId =
          <AuthorisationHolder><CategoryCode>category_1</CategoryCode></AuthorisationHolder>
        authorisationHolderWithoutId.toXml shouldBe Some(expectedResultWithoutId)
      }
    }

    List(Some(""), None) foreach { emptyValue =>
      s"omit CategoryCode if $emptyValue" in {
        val authorisationHolderWithoutCategoryCode = authorisationHolderAll.copy(categoryCode = emptyValue)
        val expectedResultWithoutCategoryCode =
          <AuthorisationHolder><ID>id_1</ID></AuthorisationHolder>
        authorisationHolderWithoutCategoryCode.toXml shouldBe Some(expectedResultWithoutCategoryCode)
      }
    }

  }

}
