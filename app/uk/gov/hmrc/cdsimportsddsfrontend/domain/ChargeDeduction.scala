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

import uk.gov.hmrc.cdsimportsddsfrontend.services.XmlWriter

import scala.xml.Elem

case class ChargeDeduction(
                            typeCode: String,
                            currencyAmount: CurrencyAmount
                          )

object ChargeDeduction {
  implicit val chargeDeductionWriter: XmlWriter[ChargeDeduction] = new XmlWriter[ChargeDeduction] {
    override def toXml(value: ChargeDeduction): Option[Elem] = {
      Some(
        <ChargeDeduction>
          <ChargesTypeCode>{value.typeCode}</ChargesTypeCode>
          <OtherChargeDeductionAmount currencyID={value.currencyAmount.currency}>{value.currencyAmount.amount}</OtherChargeDeductionAmount>
        </ChargeDeduction>
      )
    }
  }
}