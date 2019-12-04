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

import uk.gov.hmrc.cdsimportsddsfrontend.domain.ObligationGuarantee

import scala.xml.{Attribute, Elem, NodeSeq}

object ObligationGuaranteeXmlWriter {

  implicit val obligationGuaranteeXmlWriter: XmlWriter[ObligationGuarantee] = new XmlWriter[ObligationGuarantee] {
    override def toXmlOption(value: ObligationGuarantee): Option[Elem] = {

      val currencyAttr = Attribute.apply(pre = "", key = "currencyID", value= value.amountAmount.fold("")(_.currency), scala.xml.Null)
      val amountAmount = maybeElement("AmountAmount", value.amountAmount.map(_.amount), Some(currencyAttr))
      val id = maybeElement("ID", value.id)
      val referenceId = maybeElement("ReferenceID", value.referenceId)
      val securityDetailsCode = maybeElement("SecurityDetailsCode", value.securityDetailsCode)
      val accessCode = maybeElement("AccessCode", value.accessCode)
      val guaranteeOffice = value.guaranteeOffice match {
        case Some(officeId) => Some(<GuaranteeOffice ><ID>{officeId}</ID></GuaranteeOffice>)
        case _ => None
      }

      List[Option[NodeSeq]](amountAmount, id, referenceId, securityDetailsCode, accessCode, guaranteeOffice).flatten
        .reduceOption((a, b) => a ++ b)
        .map(elem => <ObligationGuarantee>{elem}</ObligationGuarantee>)
    }
  }
}