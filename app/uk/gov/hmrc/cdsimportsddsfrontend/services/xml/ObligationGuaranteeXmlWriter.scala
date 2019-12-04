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

import cats.implicits._
import uk.gov.hmrc.cdsimportsddsfrontend.domain.ObligationGuarantee

import scala.xml.{Attribute, Elem, Node}

object ObligationGuaranteeXmlWriter {

  implicit val obligationGuaranteeXmlWriter: XmlWriter[ObligationGuarantee] = new XmlWriter[ObligationGuarantee] {
    override def toXml(value: ObligationGuarantee): Option[Elem] = {

      val currencyAttr = Attribute.apply(pre = "", key = "currencyID", value= value.amountAmount.fold("")(_.currency), scala.xml.Null)
      val amountAmount = maybeElement("AmountAmount", value.amountAmount.map(_.amount), Some(currencyAttr))
      val id = maybeElement("ID", value.id)
      val referenceId = maybeElement("ReferenceID", value.referenceId)
      val securityDetailsCode = maybeElement("SecurityDetailsCode", value.securityDetailsCode)
      val accessCode = maybeElement("AccessCode", value.accessCode)
      val guaranteeOfficeID = maybeElement("ID", Some(""), None)
      val guaranteeOffice = value.guaranteeOffice match {
        case Some(officeId) => Some(<GuaranteeOffice ><ID>{officeId}</ID></GuaranteeOffice>)
        case _ => None
      }
      val nodes = List(amountAmount, id, referenceId, securityDetailsCode, accessCode, guaranteeOffice).flattenOption

      Option(nodes).filter(_.nonEmpty).map {
        nonEmptyChildNodes => <ObligationGuarantee>{nonEmptyChildNodes}</ObligationGuarantee>
      }

    }
  }
}