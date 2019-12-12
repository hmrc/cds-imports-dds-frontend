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

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.SubmissionStatus._

class SubmissionStatusSpec extends WordSpec with Matchers {

  "Reads for status" should {
    "correctly retrieve a value for every scenario" in {
      retrieve("Pending") shouldBe PENDING
      retrieve("Cancellation Requested") shouldBe REQUESTED_CANCELLATION
      retrieve("01") shouldBe ACCEPTED
      retrieve("02") shouldBe RECEIVED
      retrieve("03") shouldBe REJECTED
      retrieve("05") shouldBe UNDERGOING_PHYSICAL_CHECK
      retrieve("06") shouldBe ADDITIONAL_DOCUMENTS_REQUIRED
      retrieve("07") shouldBe AMENDED
      retrieve("08") shouldBe RELEASED
      retrieve("09") shouldBe CLEARED
      retrieve("10") shouldBe CANCELLED
      retrieve("11", Some("39")) shouldBe CUSTOMS_POSITION_GRANTED
      retrieve("11", Some("41")) shouldBe CUSTOMS_POSITION_DENIED
      retrieve("13") shouldBe TAX_LIABILITY
      retrieve("14") shouldBe INSUFFICIENT_BALANCE_IN_DAN
      retrieve("15") shouldBe INSUFFICIENT_BALANCE_IN_DAN_REMINDER
      retrieve("17") shouldBe DECLARATION_HANDLED_EXTERNALLY
      retrieve("UnknownStatus") shouldBe UNKNOWN
      retrieve("WrongStatus") shouldBe UNKNOWN

    }
  }
}