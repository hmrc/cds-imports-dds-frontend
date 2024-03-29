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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.model

import uk.gov.hmrc.cdsimportsddsfrontend.domain.{CurrencyAmount, ObligationGuarantee}

case class MiscellaneousViewModel(quotaOrderNumber: Option[String] = None,
                                  guaranteeType: Option[String] = None,
                                  grn: Option[String] = None,
                                  otherGRN: Option[String] = None,
                                  accessCode: Option[String] = None,
                                  importDutyAndOtherCharges: Option[CurrencyAmount] = None,
                                  customsOffice: Option[String] = None,
                                  natureOfTransaction: Option[String] = Some("1"),
                                  statisticalValue: Option[CurrencyAmount] = Some(CurrencyAmount("GBP", "100")),
                                  writeOffViewModels: Seq[WriteOffViewModel] = Seq.empty
                                 ) {

  def toObligationGuarantee: ObligationGuarantee = ObligationGuarantee(
    amountAmount = this.importDutyAndOtherCharges,
    id = this.otherGRN,
    referenceId = this.grn,
    securityDetailsCode = this.guaranteeType,
    accessCode = this.accessCode,
    guaranteeOffice = this.customsOffice
  )
}
