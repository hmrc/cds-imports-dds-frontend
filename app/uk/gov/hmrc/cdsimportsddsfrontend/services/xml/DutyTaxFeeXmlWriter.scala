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

import uk.gov.hmrc.cdsimportsddsfrontend.domain.DutyTaxFee
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.PaymentXmlWriter._
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.XmlSyntax._

import scala.xml.{Elem, Node, NodeSeq}

object DutyTaxFeeXmlWriter {

  implicit val dutyTaxFeeXmlWriter: XmlWriter[DutyTaxFee] = new XmlWriter[DutyTaxFee] {
    override def toXmlOption(value: DutyTaxFee): Option[Elem] = {
      val dutyRegimeCode: Option[Node] = maybeElement("DutyRegimeCode", value.dutyRegimeCode)
      val payment: Option[Node] = value.payment.flatMap(_.toXmlOption)

      List[Option[NodeSeq]](dutyRegimeCode, payment).flatten
        .reduceOption((a, b) => a ++ b)
        .map(elem => <DutyTaxFee>{elem}</DutyTaxFee>)
    }
  }
}
