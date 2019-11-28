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

import org.scalatest.{Matchers, WordSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.domain._

class DeclarationViewModelSpec extends WordSpec with Matchers {

  "DeclarationViewModel" should {
    "convert to Declaration" in {
      val viewModel = DeclarationViewModel()

      val declaration = viewModel.toDeclaration()

      declaration.whenAndWhere shouldBe WhenAndWhere()

      declaration.commodity shouldBe Some(Commodity(Some("TSP no description required"),
        Some(GoodsMeasure(Some("100"), Some("100"), Some("105")))))

      declaration.packaging shouldBe Some(Packaging(Some("BF"), Some("1"), Some("TSP not required")))

      declaration.borderTransportMeans shouldBe Some(BorderTransportMeans(Some("US"), Some("1")))

      declaration.consignment shouldBe Some(Consignment(Some("0"), Some(ArrivalTransportMeans(Some("10"), Some("1023465738"))),
        Some(GoodsLocation(Some("FXTFXTFXT"), Some("A"), Some(Address(None, None, Some("GB"), None, Some("U")))))))
    }
  }
}