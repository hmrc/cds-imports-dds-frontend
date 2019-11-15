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

package uk.gov.hmrc.cdsimportsddsfrontend.domain

import org.scalatest.{Matchers, OptionValues, WordSpec}
//import uk.gov.hmrc.cdsimportsddsfrontend.services._
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlWriterInstances._

class PreviousDocumentSpec extends WordSpec with Matchers with OptionValues {

  val expectedResultAll =
    <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><TypeCode>typeclass_1</TypeCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>

  val expectedResultWithEmptyString =
      <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>

  val expectedResultMixed =
    <PreviousDocument><ID>id_1</ID><TypeCode>typeclass_1</TypeCode></PreviousDocument>

  val expectedResultWithoutCategoryCode =
    <PreviousDocument><ID>id_1</ID><TypeCode>typeclass_1</TypeCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>

  val expectedResultWithoutId =
    <PreviousDocument><CategoryCode>category_1</CategoryCode><TypeCode>typeclass_1</TypeCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>

  val expectedResultWithoutTypeCode =
    <PreviousDocument><CategoryCode>category_1</CategoryCode><LineNumeric>linenumeric_1</LineNumeric></PreviousDocument>

  val expectedResultLineNumeric =
    <PreviousDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><TypeCode>typeclass_1</TypeCode></PreviousDocument>

  val previousDocumentAll = PreviousDocument(
    categoryCode = Some("category_1"),
    id = Some("id_1"),
    typeCode = Some("typeclass_1"),
    lineNumeric = Some("linenumeric_1")
  )

  val previousDocumentEmpty = PreviousDocument(
    categoryCode = None,
    id = None,
    typeCode = None,
    lineNumeric = None
  )

  val previousDocumentWithEmptyString = previousDocumentAll.copy(typeCode = Some(""))
  val previousDocumentWithNone = previousDocumentAll.copy(categoryCode = None)
  val previousDocumentMixed = previousDocumentAll.copy(categoryCode = None, lineNumeric = Some("   "))
  val previousDocumentWithoutCategoryCode = previousDocumentAll.copy(categoryCode = None)
  val previousDocumentWithoutId = previousDocumentAll.copy(id = None)
  val previousDocumentWithoutTypeCode = previousDocumentAll.copy(typeCode = None)
  val previousDocumentWithoutLineNumeric = previousDocumentAll.copy(lineNumeric = None)

  "PreviousDocument generateXML " should {
    "return PreviousDocument node with text in all child elements" when {
      "all parameters are Some" in {
        previousDocumentAll.toXml shouldBe (Some(expectedResultAll))
      }
    }

    "return no PreviousDocument node when all fields are None" in {
      previousDocumentEmpty.toXml shouldBe None
    }

    "return no element for a field which has an empty space" in {
      previousDocumentWithEmptyString.toXml shouldBe Some(expectedResultWithEmptyString)
    }

    "return an elements only for fields contain some text" in {
      previousDocumentMixed.toXml shouldBe Some(expectedResultMixed)
    }

    "return all elements  text" in {
      previousDocumentMixed.toXml shouldBe Some(expectedResultMixed)
    }


  }

}