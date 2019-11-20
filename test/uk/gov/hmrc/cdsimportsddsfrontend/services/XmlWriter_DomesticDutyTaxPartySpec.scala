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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.DomesticDutyTaxParty
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlSyntax._
import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlWriterInstances._

class XmlWriter_DomesticDutyTaxPartySpec extends WordSpec with Matchers with OptionValues {

  val domesticDutyTaxPartyAll = DomesticDutyTaxParty(
    identifier = Some("id_1"),
    roleCode = Some("role_1")
  )

  "DomesticDutyTaxParty.toXml" should {
    "return DomesticDutyTaxParty node with text in all child elements" when {
      "all parameters are Some" in {
          val expectedResultAll =
            <DomesticDutyTaxParty><ID>id_1</ID><RoleCode>role_1</RoleCode></DomesticDutyTaxParty>
        domesticDutyTaxPartyAll.toXml shouldBe (Some(expectedResultAll))
      }
    }

    "return no DomesticDutyTaxParty node when all fields are None" in {
      val domesticDutyTaxPartyEmpty = DomesticDutyTaxParty(None, None)
      domesticDutyTaxPartyEmpty.toXml shouldBe None
    }

    List(Some(""), None) foreach { emptyValue =>
      s"omit ID if $emptyValue" in {
        val domesticDutyTaxPartyWithoutId = domesticDutyTaxPartyAll.copy(identifier = emptyValue)
        val expectedResultWithoutId =
          <DomesticDutyTaxParty><RoleCode>role_1</RoleCode></DomesticDutyTaxParty>
        domesticDutyTaxPartyWithoutId.toXml shouldBe Some(expectedResultWithoutId)
      }
    }

    List(Some(""), None) foreach { emptyValue =>
      s"omit RoleCode if $emptyValue" in {
        val domesticDutyTaxPartyWithoutRoleCode = domesticDutyTaxPartyAll.copy(roleCode = emptyValue)
        val expectedResultWithoutRoleCode =
          <DomesticDutyTaxParty><ID>id_1</ID></DomesticDutyTaxParty>
        domesticDutyTaxPartyWithoutRoleCode.toXml shouldBe Some(expectedResultWithoutRoleCode)
      }
    }

  }

}
