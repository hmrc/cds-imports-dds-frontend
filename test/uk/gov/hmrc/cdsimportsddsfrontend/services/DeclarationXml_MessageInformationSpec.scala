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

import com.gu.scalatest.JsoupShouldMatchers
import org.scalatest.MustMatchers
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Declaration
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}

import scala.xml.Elem

class DeclarationXml_MessageInformationSpec extends CdsImportsSpec with MustMatchers with AuthenticationBehaviours with JsoupShouldMatchers {

  "MessageInformation data" should {
    "be populated in the XML" in {
      val declaration = Declaration()

      val xmlElement: Elem = DeclarationXml.fromImportDeclaration("EORI", declaration)
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "TradeTerms" \ "ConditionCode").head.text mustBe "CFR"
      (xmlElement \ "Declaration" \ "TypeCode").head.text mustBe "IMZ"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "SequenceNumeric").head.text mustBe "1"
      (xmlElement \ "Declaration" \ "GoodsItemQuantity").head.text mustBe "1"
      (xmlElement \ "Declaration" \ "GoodsShipment" \ "GovernmentAgencyGoodsItem" \ "GovernmentProcedure" \ "CurrentCode").map(_.text) mustBe List("40","000")
    }
  }
}