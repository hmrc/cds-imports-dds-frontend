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
import scala.xml.{Elem, Node}

object ObligationGuaranteeXmlWriter {

  implicit val obligationGuaranteeXmlWriter: XmlWriter[ObligationGuarantee] = new XmlWriter[ObligationGuarantee] {
    override def toXml(value: ObligationGuarantee): Option[Elem] = {
      val amountAmount: Option[Node] = maybeElement("AmountAmount", value.amountAmount)
      val id: Option[Node] = maybeElement("ID", value.id)
      val referenceId: Option[Node] = maybeElement("ReferenceID", value.referenceId)
      val securityDetailsCode: Option[Node] = maybeElement("SecurityDetailsCode", value.securityDetailsCode)
      val accessCode: Option[Node] = maybeElement("AccessCode", value.accessCode)
      val guaranteeOffice: Option[Node] = maybeElement("GuaranteeOffice", value.guaranteeOffice)

      val nodes: List[Node] = List(amountAmount, id, referenceId, securityDetailsCode, accessCode, guaranteeOffice).flattenOption

      Option(nodes).filter(_.nonEmpty).map {
        nonEmptyChildNodes => <ObligationGuarantee>{nonEmptyChildNodes}</ObligationGuarantee>
      }
    }
  }
}
