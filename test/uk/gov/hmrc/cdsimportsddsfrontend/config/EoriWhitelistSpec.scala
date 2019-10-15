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

package uk.gov.hmrc.cdsimportsddsfrontend.config

import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.cdsimportsddsfrontend.test.CdsImportsSpec

class EoriWhitelistSpec extends CdsImportsSpec with MockitoSugar {

  private val mockConfiguration = mock[Configuration]

  "EoriWhitelist.allows" should {
    "return true if whitelist is non-empty and contains given EORI" in {
      when(mockConfiguration.getOptional[Seq[String]]("whitelist.eori")).thenReturn(Some(Seq("ALICE", "GB12345678", "BOB")))
      val whitelist = new EoriWhitelist(mockConfiguration)
      whitelist.allows("GB12345678") must be(true)
    }

    "return false if whitelist is non-empty and does not contain given EORI" in {
      when(mockConfiguration.getOptional[Seq[String]]("whitelist.eori")).thenReturn(Some(Seq("RITA", "SUE", "BOB")))
      val whitelist = new EoriWhitelist(mockConfiguration)
      whitelist.allows("GB12345678") must be(false)
    }

    "return true if whitelist is empty" in {
      when(mockConfiguration.getOptional[Seq[String]]("whitelist.eori")).thenReturn(Some(Seq.empty))
      val whitelist = new EoriWhitelist(mockConfiguration)
      whitelist.allows("E V E R Y O N E ! ! !") must be(true)
    }

    "return true if whitelist is not configured" in {
      when(mockConfiguration.getOptional[Seq[String]]("whitelist.eori")).thenReturn(None)
      val whitelist = new EoriWhitelist(mockConfiguration)
      whitelist.allows("E V E R Y O N E ! ! !") must be(true)
    }
  }
}
