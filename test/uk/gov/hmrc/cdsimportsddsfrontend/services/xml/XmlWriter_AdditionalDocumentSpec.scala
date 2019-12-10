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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.AdditionalDocument
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlWriterInstances.additionalDocumentWriter

class XmlWriter_AdditionalDocumentSpec extends WordSpec with Matchers with OptionValues {

  val additionalDocumentAll = AdditionalDocument(
    categoryCode = Some("category_1"),
    typeCode = Some("typeCode_1"),
    id = Some("id_1"),
    lpco = Some("lpco_1"),
    name = Some("name_1"),
    submitter = None,
    effectiveDateTime = None,
    writeOff = None
  )

  val emptyFields = List(Some(""), None)

  "AdditionalDocument generateXml" should {
    "return AdditionalDocument node with all child elements populated" when {
      "all parameters are Some and not empty spaces" in {
        val expectedResultAll = Some(<AdditionalDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><Name>name_1</Name><TypeCode>typeCode_1</TypeCode><LPCOExemptionCode>lpco_1</LPCOExemptionCode></AdditionalDocument>)

        additionalDocumentAll.toXmlOption shouldBe expectedResultAll
      }
    }

    "not return an AdditionalDocument node when all fields are not present" in {
      val additionalDocumentEmpty = AdditionalDocument(
      categoryCode = None,
      typeCode = None,
      id = None,
      lpco = None,
      name = None,
      submitter = None,
      effectiveDateTime = None,
      writeOff = None
      )

      additionalDocumentEmpty.toXmlOption shouldBe None
    }

    "not return an element for a field which has an empty string" in {
      val additionalDocumentWithEmptyString = additionalDocumentAll.copy(typeCode = Some(""))
      val expectedResultWithEmptyString = Some(<AdditionalDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><Name>name_1</Name><LPCOExemptionCode>lpco_1</LPCOExemptionCode></AdditionalDocument>)

      additionalDocumentWithEmptyString.toXmlOption shouldBe expectedResultWithEmptyString
    }

    "return elements only for fields containing some text" in {
      val additionalDocumentMixed = additionalDocumentAll.copy(categoryCode = Some(""), lpco = Some(""))
      val expectedResultMixed = Some(<AdditionalDocument><ID>id_1</ID><Name>name_1</Name><TypeCode>typeCode_1</TypeCode></AdditionalDocument>)

      additionalDocumentMixed.toXmlOption shouldBe expectedResultMixed
    }

    emptyFields foreach { value =>
      s"omit CategoryCode if $value" in {
        val additionalDocumentWithoutCategoryCode = additionalDocumentAll.copy(categoryCode = value)
        val expectedResultWithoutCategoryCode =
          Some(<AdditionalDocument><ID>id_1</ID><Name>name_1</Name><TypeCode>typeCode_1</TypeCode><LPCOExemptionCode>lpco_1</LPCOExemptionCode></AdditionalDocument>)

        additionalDocumentWithoutCategoryCode.toXmlOption shouldBe expectedResultWithoutCategoryCode
      }
    }

    emptyFields foreach { value =>
      s"omit ID if $value" in {
        val additionalDocumentWithoutId = additionalDocumentAll.copy(id = value)
        val expectedResultWithoutId =Some(<AdditionalDocument><CategoryCode>category_1</CategoryCode><Name>name_1</Name><TypeCode>typeCode_1</TypeCode><LPCOExemptionCode>lpco_1</LPCOExemptionCode></AdditionalDocument>)

        additionalDocumentWithoutId.toXmlOption shouldBe expectedResultWithoutId
      }
    }

    emptyFields foreach { value =>
      s"omit TypeCode if $value" in {
        val additionalDocumentWithoutTypeCode = additionalDocumentAll.copy(typeCode = value)
        val expectedResultWithoutTypeCode =Some(<AdditionalDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><Name>name_1</Name><LPCOExemptionCode>lpco_1</LPCOExemptionCode></AdditionalDocument>)

        additionalDocumentWithoutTypeCode.toXmlOption shouldBe expectedResultWithoutTypeCode
      }
    }

    emptyFields foreach { value =>
      s"omit LPCOExemptionCode if $value" in {
        val additionalDocumentWithoutLpco = additionalDocumentAll.copy(lpco = value)
        val expectedResultWithoutLpco =Some(<AdditionalDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><Name>name_1</Name><TypeCode>typeCode_1</TypeCode></AdditionalDocument>)

        additionalDocumentWithoutLpco.toXmlOption shouldBe expectedResultWithoutLpco
      }
    }

    emptyFields foreach { value =>
      s"omit Name if $value" in {
        val additionalDocumentWithoutName = additionalDocumentAll.copy(name = value)
        val expectedResultWithoutName =Some(<AdditionalDocument><CategoryCode>category_1</CategoryCode><ID>id_1</ID><TypeCode>typeCode_1</TypeCode><LPCOExemptionCode>lpco_1</LPCOExemptionCode></AdditionalDocument>)

        additionalDocumentWithoutName.toXmlOption shouldBe expectedResultWithoutName
      }
    }
  }

}
