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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import play.api.test.FutureAwaits
import play.api.test.Helpers.status
import play.mvc.Http.Status
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}


class DeclarationController_goodsIdentification  extends CdsImportsSpec
with Scenarios with FutureAwaits with BeforeAndAfterEach {

  import DeclarationControllerSpec._

  override def beforeEach(): Unit = {
  featureSwitchRegistry.SinglePageDeclaration.enable ()
  }

  "A GET Request" should {
    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "goodsIdentification.netMass")
        body should include element withName("input").withAttrValue("name", "goodsIdentification.supplementaryUnits")
        body should include element withName("input").withAttrValue("name", "goodsIdentification.grossMass")
        body should include element withName("input").withAttrValue("name", "goodsIdentification.description")
        body should include element withName("input").withAttrValue("name", "goodsIdentification.typesOfPackage")
        body should include element withName("input").withAttrValue("name", "goodsIdentification.numberOfPackages")
        body should include element withName("input").withAttrValue("name", "goodsIdentification.shippingMarks")
      }
    }
  }

}
