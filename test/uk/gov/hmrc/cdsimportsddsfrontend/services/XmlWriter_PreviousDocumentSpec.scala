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

package uk.gov.hmrc.cdsimportsddsfrontend.services

import org.scalatest.{Matchers, OptionValues, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.PreviousDocument
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlWriterInstances._

class XmlWriter_PreviousDocumentSpec extends WordSpec with Matchers with OptionValues {

  val previousDocumentAll = PreviousDocument(
    categoryCode = Some("category_1"),
    id = Some("id_1"),
    typeCode = Some("typeclass_1"),
    lineNumeric = Some("linenumeric_1")
  )

  "PreviousDocument generateXML " should {
    "return PreviousDocument node with text in all child elements" when {
      "all parameters are Some" in {
          val expectedResultAll =
            <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><TypeCode>typeclass_1</TypeCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>
        previousDocumentAll.toXml shouldBe (Some(expectedResultAll))
      }
    }

    "return no PreviousDocument node when all fields are None" in {
      val previousDocumentEmpty = PreviousDocument(
        categoryCode = None,
        id = None,
        typeCode = None,
        lineNumeric = None
      )
      previousDocumentEmpty.toXml shouldBe None
    }

    "return no element for a field which has an empty string" in {
      val previousDocumentWithEmptyString = previousDocumentAll.copy(typeCode = Some(""))
      val expectedResultWithEmptyString =
        <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>
      previousDocumentWithEmptyString.toXml shouldBe Some(expectedResultWithEmptyString)
    }

    "return elements only for fields containing some text" in {
      val previousDocumentMixed = previousDocumentAll.copy(categoryCode = None, lineNumeric = Some(""))
      val expectedResultMixed =
        <PreviousDocument><ID>id_1</ID><TypeCode>typeclass_1</TypeCode></PreviousDocument>
      previousDocumentMixed.toXml shouldBe Some(expectedResultMixed)
    }

    List(Some(""), None) foreach { value =>
      s"omit CategoryCode if $value" in {
        val previousDocumentWithoutCategoryCode = previousDocumentAll.copy(categoryCode = value)
        val expectedResultWithoutCategoryCode =
          <PreviousDocument><ID>id_1</ID><TypeCode>typeclass_1</TypeCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>
        previousDocumentWithoutCategoryCode.toXml shouldBe Some(expectedResultWithoutCategoryCode)
      }
    }

    List(Some(""), None) foreach { value =>
      s"omit ID if $value" in {
        val previousDocumentWithoutId = previousDocumentAll.copy(id = value)
        val expectedResultWithoutId =
          <PreviousDocument><CategoryCode>category_1</CategoryCode><TypeCode>typeclass_1</TypeCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>
        previousDocumentWithoutId.toXml shouldBe Some(expectedResultWithoutId)
      }
    }

    List(Some(""), None) foreach { value =>
      s"omit TypeCode if $value" in {
        val previousDocumentWithoutTypeCode = previousDocumentAll.copy(typeCode = value)
        val expectedResultWithoutTypeCode =
          <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>
        previousDocumentWithoutTypeCode.toXml shouldBe Some(expectedResultWithoutTypeCode)
      }
    }

    List(Some(""), None) foreach { value =>
      s"omit LineNumeric if $value" in {
        val previousDocumentWithoutLineNumeric = previousDocumentAll.copy(lineNumeric = value)
        val expectedResultWithoutLineNumeric =
          <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><TypeCode>typeclass_1</TypeCode></PreviousDocument>
        previousDocumentWithoutLineNumeric.toXml shouldBe Some(expectedResultWithoutLineNumeric)
      }
    }

  }

}
