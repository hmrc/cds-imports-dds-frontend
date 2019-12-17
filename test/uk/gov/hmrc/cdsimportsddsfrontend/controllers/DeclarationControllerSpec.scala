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
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.DeclarationViewModel
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Address, AuthorisationHolder, DomesticDutyTaxParty, Party}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{CdsImportsSpec, Scenarios}

import scala.concurrent.Future

class DeclarationControllerSpec extends CdsImportsSpec
  with Scenarios
  with FutureAwaits
  with BeforeAndAfterEach {

  import DeclarationControllerSpec._

  override def beforeEach(): Unit = {
    featureSwitchRegistry.SinglePageDeclaration.enable()
  }

  "A GET Request" should {

    "return 404 when the SinglePageDeclaration feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      new GetScenario() {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      new GetScenario() {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "show the expected declaration form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType.declarationType")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalDeclarationType")
        body should include element withName("input").withAttrValue("name", "declarationType.goodsItemNumber")
        body should include element withName("input").withAttrValue("name", "declarationType.totalNumberOfItems")
        body should include element withName("input").withAttrValue("name", "declarationType.requestedProcedureCode")
        body should include element withName("input").withAttrValue("name", "declarationType.previousProcedureCode")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalProcedureCode")
      }
    }

    "show the expected header previous document form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].lineNumeric")
      }
    }


    "show the expected item previous document form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].lineNumeric")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].lineNumeric")
      }
    }

    "show the expected additional information form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.additionalInformation.code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.additionalInformation.description")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[0].code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[0].description")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[1].code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[1].description")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[2].code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[2].description")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[3].code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[3].description")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[4].code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[4].description")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[5].code")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.additionalInformation[5].description")
      }
    }

    "show the expected additional document form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[0].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[0].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[0].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[0].lpco")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[0].name")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[1].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[1].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[1].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[1].lpco")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[1].name")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[2].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[2].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[2].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[2].lpco")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[2].name")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[3].categoryCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[3].typeCode")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[3].id")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[3].lpco")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalDocument[3].name")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.localReferenceNumber")
      }
    }

    "show the expected additional payment form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalPayment[0].additionalDocPaymentID")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalPayment[0].additionalDocPaymentCategory")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalPayment[0].additionalDocPaymentType")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalPayment[1].additionalDocPaymentID")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalPayment[1].additionalDocPaymentCategory")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.additionalPayment[1].additionalDocPaymentType")
      }
    }

    "show the expected declaration field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_declarationType").withValue("1.1 Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_additionalDeclarationType").withValue("1.2 Additional Declaration Type")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_goodsItemNumber").withValue("1.6 Goods Item Number")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_totalNumberOfItems").withValue("1.9 Total Number Of Items")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_requestedProcedureCode").withValue("1.10 Requested Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_previousProcedureCode").withValue("1.10 Previous Procedure Code")
        body should include element withClass("govuk-label").withAttrValue("for", "declarationType_additionalProcedureCode").withValue("1.11 Additional Procedure Code (000 or C07)")
      }
    }

    "show the item previous document expected field labels" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_0_categoryCode").withValue("Category")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_0_id").withValue("Identifier")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_0_typeCode").withValue("Type")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_0_lineNumeric").withValue("Reference")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_1_categoryCode").withValue("Category")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_1_id").withValue("Identifier")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_1_typeCode").withValue("Type")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_1_lineNumeric").withValue("Reference")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_2_categoryCode").withValue("Category")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_2_id").withValue("Identifier")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_2_typeCode").withValue("Type")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_2_lineNumeric").withValue("Reference")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_3_categoryCode").withValue("Category")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_3_id").withValue("Identifier")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_3_typeCode").withValue("Type")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_3_lineNumeric").withValue("Reference")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_4_categoryCode").withValue("Category")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_4_id").withValue("Identifier")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_4_typeCode").withValue("Type")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_4_lineNumeric").withValue("Reference")

        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_5_categoryCode").withValue("Category")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_5_id").withValue("Identifier")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_5_typeCode").withValue("Type")
        body should include element withClass("govuk-label").withAttrValue("for", "documentationAndReferences_item_previousDocument_5_lineNumeric").withValue("Reference")
      }
    }

    "show the expected pre-populated declaration field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "declarationType.declarationType").withAttrValue("value", "IM")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalDeclarationType").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "declarationType.goodsItemNumber").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "declarationType.totalNumberOfItems").withAttrValue("value", "1")
        body should include element withName("input").withAttrValue("name", "declarationType.requestedProcedureCode").withAttrValue("value", "40")
        body should include element withName("input").withAttrValue("name", "declarationType.previousProcedureCode").withAttrValue("value", "00")
        body should include element withName("input").withAttrValue("name", "declarationType.additionalProcedureCode").withAttrValue("value", "000")
      }
    }

    "show the expected pre-populated header previous document field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].id").withAttrValue("value", "20191101")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].typeCode").withAttrValue("value", "CLE")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[0].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].id").withAttrValue("value", "9GB201909014000")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].typeCode").withAttrValue("value", "DCR")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[1].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].id").withAttrValue("value", "20191101")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].typeCode").withAttrValue("value", "CLE")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[2].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].id").withAttrValue("value", "9GB201909014000")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].typeCode").withAttrValue("value", "DCR")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.header.previousDocument[3].lineNumeric").withAttrValue("value", "1")
      }
    }

    "show the expected pre-populated item previous document field values" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].id").withAttrValue("value", "20191101")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].typeCode").withAttrValue("value", "CLE")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[0].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].categoryCode").withAttrValue("value", "Y")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].id").withAttrValue("value", "9GB201909014000")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].typeCode").withAttrValue("value", "DCR")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[1].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].id").withAttrValue("value", "20191103")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].typeCode").withAttrValue("value", "ZZZ")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[2].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].id").withAttrValue("value", "9GB201909014002")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].typeCode").withAttrValue("value", "235")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[3].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].id").withAttrValue("value", "9GB201909014003")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].typeCode").withAttrValue("value", "ZZZ")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[4].lineNumeric").withAttrValue("value", "1")

        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].categoryCode").withAttrValue("value", "Z")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].id").withAttrValue("value", "9GB201909014004")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].typeCode").withAttrValue("value", "270")
        body should include element withName("input").withAttrValue("name", "documentationAndReferences.item.previousDocument[5].lineNumeric").withAttrValue("value", "1")
      }
    }
  }

  "in section 3" should {
    "show the expected form fields" in signedInScenario { user =>
      new GetScenario() {
        status(response) mustBe Status.OK
        body should include element withName("input").withAttrValue("name", "parties.exporter.name")
        body should include element withName("input").withAttrValue("name", "parties.exporter.identifier")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.city")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.exporter.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.importer.name")
        body should include element withName("input").withAttrValue("name", "parties.importer.identifier")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.city")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.importer.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.buyer.name")
        body should include element withName("input").withAttrValue("name", "parties.buyer.identifier")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.city")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.buyer.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.buyer.phoneNumber")
        body should include element withName("input").withAttrValue("name", "parties.seller.name")
        body should include element withName("input").withAttrValue("name", "parties.seller.identifier")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.streetAndNumber")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.city")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.countryCode")
        body should include element withName("input").withAttrValue("name", "parties.seller.address.postcode")
        body should include element withName("input").withAttrValue("name", "parties.seller.phoneNumber")
        body should include element withName("input").withAttrValue("name", "parties.declarant.identifier")
        body should include element withName("input").withAttrValue("name", "parties.authorisationHolder[0].identifier")
        body should include element withName("input").withAttrValue("name", "parties.authorisationHolder[0].categoryCode")
        body should include element withName("input").withAttrValue("name", "parties.authorisationHolder[1].identifier")
        body should include element withName("input").withAttrValue("name", "parties.authorisationHolder[1].categoryCode")
        body should include element withName("input").withAttrValue("name", "parties.domesticDutyTaxParty[0].identifier")
        body should include element withName("input").withAttrValue("name", "parties.domesticDutyTaxParty[0].roleCode")
        body should include element withName("input").withAttrValue("name", "parties.domesticDutyTaxParty[1].identifier")
        body should include element withName("input").withAttrValue("name", "parties.domesticDutyTaxParty[1].roleCode")
      }
    }
  }


  "A POST Request" should {
    "return 404 when the SinglePageDeclaration feature is disabled" in {
      featureSwitchRegistry.SinglePageDeclaration.disable()
      val formData = Map[String, Seq[String]]()
      new PostScenario(formData) {
        status(response) must be(Status.NOT_FOUND)
      }
    }

    "return 503 when the SinglePageDeclaration feature is suspended" in {
      featureSwitchRegistry.SinglePageDeclaration.suspend()
      val formData = Map[String, Seq[String]]()
      new PostScenario(formData) {
        status(response) must be(Status.SERVICE_UNAVAILABLE)
      }
    }

    "post to the declaration service when all required fields are present" in signedInScenario { user =>
      val formData = mandatoryFormData
      when(mockDeclarationService.submit(any(), any[DeclarationViewModel])(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK
      }
    }

    "post the expected additional information data to the declaration service" in signedInScenario { user =>

      val formData =
        mandatoryFormData ++
        previousDocumentHeaderFormData ++
        previousDocumentItemFormData ++
        documentationFormData

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.documentationAndReferences.headerAdditionalInformation.code mustBe (Some("additionalInfoCode"))
        actualDeclaration.documentationAndReferences.headerAdditionalInformation.description mustBe (Some("additionalInfoDescription"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(0).code mustBe (Some("itemAdditionalInfoCode 1"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(0).description mustBe (Some("itemAdditionalInfoDescription 1"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(1).code mustBe (Some("itemAdditionalInfoCode 2"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(1).description mustBe (Some("itemAdditionalInfoDescription 2"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(2).code mustBe (Some("itemAdditionalInfoCode 3"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(2).description mustBe (Some("itemAdditionalInfoDescription 3"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(3).code mustBe (Some("itemAdditionalInfoCode 4"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(3).description mustBe (Some("itemAdditionalInfoDescription 4"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(4).code mustBe (Some("itemAdditionalInfoCode 5"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(4).description mustBe (Some("itemAdditionalInfoDescription 5"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(5).code mustBe (Some("itemAdditionalInfoCode 6"))
        actualDeclaration.documentationAndReferences.itemAdditionalInformation(5).description mustBe (Some("itemAdditionalInfoDescription 6"))
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(0).categoryCode mustBe Some("header_categoryCode_1")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(0).id mustBe Some("header_id_1")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(0).typeCode mustBe Some("header_typeCode_1")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(0).lineNumeric mustBe Some("header_lineNumeric_1")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(1).categoryCode mustBe Some("header_categoryCode_2")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(1).id mustBe Some("header_id_2")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(1).typeCode mustBe Some("header_typeCode_2")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(1).lineNumeric mustBe Some("header_lineNumeric_2")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(2).categoryCode mustBe Some("header_categoryCode_3")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(2).id mustBe Some("header_id_3")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(2).typeCode mustBe Some("header_typeCode_3")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(2).lineNumeric mustBe Some("header_lineNumeric_3")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(3).categoryCode mustBe Some("header_categoryCode_4")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(3).id mustBe Some("header_id_4")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(3).typeCode mustBe Some("header_typeCode_4")
        actualDeclaration.documentationAndReferences.headerPreviousDocuments(3).lineNumeric mustBe Some("header_lineNumeric_4")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(0).categoryCode mustBe Some("item_categoryCode_1")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(0).id mustBe Some("item_id_1")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(0).typeCode mustBe Some("item_typeCode_1")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(0).lineNumeric mustBe Some("item_lineNumeric_1")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(1).categoryCode mustBe Some("item_categoryCode_2")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(1).id mustBe Some("item_id_2")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(1).typeCode mustBe Some("item_typeCode_2")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(1).lineNumeric mustBe Some("item_lineNumeric_2")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(2).categoryCode mustBe Some("item_categoryCode_3")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(2).id mustBe Some("item_id_3")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(2).typeCode mustBe Some("item_typeCode_3")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(2).lineNumeric mustBe Some("item_lineNumeric_3")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(3).categoryCode mustBe Some("item_categoryCode_4")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(3).id mustBe Some("item_id_4")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(3).typeCode mustBe Some("item_typeCode_4")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(3).lineNumeric mustBe Some("item_lineNumeric_4")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(4).categoryCode mustBe Some("item_categoryCode_5")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(4).id mustBe Some("item_id_5")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(4).typeCode mustBe Some("item_typeCode_5")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(4).lineNumeric mustBe Some("item_lineNumeric_5")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(5).categoryCode mustBe Some("item_categoryCode_6")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(5).id mustBe Some("item_id_6")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(5).typeCode mustBe Some("item_typeCode_6")
        actualDeclaration.documentationAndReferences.itemPreviousDocuments(5).lineNumeric mustBe Some("item_lineNumeric_6")
      }
    }

    "post the expected Buyer to the declaration service" in signedInScenario { user =>
      val formData = mandatoryFormData ++ Map(
        "parties.buyer.name" -> Seq("Foo Ltd"),
        "parties.buyer.address.streetAndNumber" -> Seq("123 Wembley Way"),
        "parties.buyer.address.city" -> Seq("London"),
        "parties.buyer.address.countryCode" -> Seq("GB"),
        "parties.buyer.address.postcode" -> Seq("W1A 1AA"),
        "parties.buyer.phoneNumber" -> Seq("0115 582 9384"),
        "parties.buyer.identifier" -> Seq("GB1966")
      )

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.buyer mustBe(Some(Party(
          name = Some("Foo Ltd"),
          identifier = Some("GB1966"),
          address = Some(Address(Some("123 Wembley Way"), Some("London"), Some("GB"), Some("W1A 1AA"), None)),
          phoneNumber = Some("0115 582 9384"))))
      }
    }

    "post the expected Seller to the declaration service" in signedInScenario { user =>
      val formData = mandatoryFormData ++ Map(
        "parties.seller.name" -> Seq("Bar Ltd"),
        "parties.seller.address.streetAndNumber" -> Seq("321 Arcade Av"),
        "parties.seller.address.city" -> Seq("Leeds"),
        "parties.seller.address.countryCode" -> Seq("GB"),
        "parties.seller.address.postcode" -> Seq("LS1 7DP"),
        "parties.seller.phoneNumber" -> Seq("0113 876 4567"),
        "parties.seller.identifier" -> Seq("GB2001")
      )

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.seller mustBe(Some(Party(
          name = Some("Bar Ltd"),
          identifier = Some("GB2001"),
          address = Some(Address(Some("321 Arcade Av"), Some("Leeds"), Some("GB"), Some("LS1 7DP"), None)),
          phoneNumber = Some("0113 876 4567"))))
      }
    }

    "post the expected AuthorisationHolders to the declaration service" in signedInScenario { user =>
      val formData = mandatoryFormData ++ Map(
        "parties.authorisationHolder[0].identifier" -> Seq("GB1966"),
        "parties.authorisationHolder[0].categoryCode" -> Seq("FOO"),
        "parties.authorisationHolder[1].identifier" -> Seq("GB1945"),
        "parties.authorisationHolder[1].categoryCode" -> Seq("BAR")
      )

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.authorisationHolders mustBe(Seq(
          AuthorisationHolder(Some("GB1966"), Some("FOO")),
          AuthorisationHolder(Some("GB1945"), Some("BAR"))
        ))
      }
    }

    "post the expected DomesticDutyTaxParties to the declaration service" in signedInScenario { user =>
      val formData = mandatoryFormData ++ Map(
        "parties.domesticDutyTaxParty[0].identifier" -> Seq("GB1966"),
        "parties.domesticDutyTaxParty[0].roleCode" -> Seq("FOO"),
        "parties.domesticDutyTaxParty[1].identifier" -> Seq("GB1945"),
        "parties.domesticDutyTaxParty[1].roleCode" -> Seq("BAR")
      )

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.domesticDutyTaxParties mustBe(Seq(
          DomesticDutyTaxParty(Some("GB1966"), Some("FOO")),
          DomesticDutyTaxParty(Some("GB1945"), Some("BAR"))
        ))
      }
    }

    "trims whitespace from buyer before binding the form" in signedInScenario { user =>
      val formData = mandatoryFormData ++ Map(
        "parties.buyer.name" -> Seq("   Foo Ltd     "),
        "parties.buyer.address.streetAndNumber" -> Seq("123 Wembley Way     "),
        "parties.buyer.address.city" -> Seq("London     "),
        "parties.buyer.address.countryCode" -> Seq("GB     "),
        "parties.buyer.address.postcode" -> Seq("W1A 1AA     "),
        "parties.buyer.phoneNumber" -> Seq("     "),
        "parties.buyer.identifier" -> Seq("     ")
      )

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.buyer mustBe(Some(Party(
          name = Some("Foo Ltd"),
          identifier = None,
          address = Some(Address(Some("123 Wembley Way"), Some("London"), Some("GB"), Some("W1A 1AA"), None)),
          phoneNumber = None)))
      }
    }

    "trims whitespace from seller before binding the form" in signedInScenario { user =>
      val formData = mandatoryFormData ++ Map(
        "parties.seller.name" -> Seq("   Foo Ltd     "),
        "parties.seller.address.streetAndNumber" -> Seq("123 Wembley Way     "),
        "parties.seller.address.city" -> Seq("London     "),
        "parties.seller.address.countryCode" -> Seq("GB     "),
        "parties.seller.address.postcode" -> Seq("W1A 1AA     "),
        "parties.seller.phoneNumber" -> Seq("     "),
        "parties.seller.identifier" -> Seq("     ")
      )

      val captor: ArgumentCaptor[DeclarationViewModel] = ArgumentCaptor.forClass(classOf[DeclarationViewModel])
      when(mockDeclarationService.submit(any(), captor.capture())(any()))
        .thenReturn(Future.successful(DeclarationServiceResponse("<foo></foo>", 200, Some("Good"))))
      when(mockDeclarationStore.deleteAllNotifications()(any())).thenReturn(Future.successful(true))
      new PostScenario(formData) {
        status(response) mustBe Status.OK

        val actualDeclaration = captor.getValue
        actualDeclaration.parties.seller mustBe(Some(Party(
          name = Some("Foo Ltd"),
          identifier = None,
          address = Some(Address(Some("123 Wembley Way"), Some("London"), Some("GB"), Some("W1A 1AA"), None)),
          phoneNumber = None)))
      }
    }

    "fail when mandatory fields are missing" in signedInScenario { user =>
      val formData = Map[String, Seq[String]]()
      new PostScenario(formData) {
        status(response) mustBe Status.BAD_REQUEST
        body should include element withName("a").withAttrValue("id", "declarationType.declarationType-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.additionalDeclarationType-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.goodsItemNumber-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.totalNumberOfItems-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.requestedProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.previousProcedureCode-error").withValue("This field is required")
        body should include element withName("a").withAttrValue("id", "declarationType.additionalProcedureCode-error").withValue("This field is required")
      }
    }
  }
}

object DeclarationControllerSpec {

  val mandatoryFormData: Map[String, Seq[String]] = Map(
    "declarationType.declarationType" -> Seq("declarationType"),
    "declarationType.additionalDeclarationType" -> Seq("additionalDeclarationType"),
    "declarationType.goodsItemNumber" -> Seq("goodsItemNumber"),
    "declarationType.totalNumberOfItems" -> Seq("totalNumberOfItems"),
    "declarationType.requestedProcedureCode" -> Seq("requestedProcedureCode"),
    "declarationType.previousProcedureCode" -> Seq("previousProcedureCode"),
    "declarationType.additionalProcedureCode" -> Seq("additionalProcedureCode"),
    "documentationAndReferences.localReferenceNumber" -> Seq("localRef")
  )

  private def previousDocumentFormDataRow(level: String, index: Int) = {
    Map(
      s"documentationAndReferences.${level}.previousDocument[$index].categoryCode" -> Seq(s"${level}_categoryCode_${index+1}"),
      s"documentationAndReferences.${level}.previousDocument[$index].typeCode" -> Seq(s"${level}_typeCode_${index+1}"),
      s"documentationAndReferences.${level}.previousDocument[$index].id" -> Seq(s"${level}_id_${index+1}"),
      s"documentationAndReferences.${level}.previousDocument[$index].lineNumeric" -> Seq(s"${level}_lineNumeric_${index+1}"))
  }

  val previousDocumentHeaderFormData: Map[String, Seq[String]] =
    (0 to 3).flatMap(i => previousDocumentFormDataRow("header", i)).toMap

  val previousDocumentItemFormData: Map[String, Seq[String]] =
    (0 to 5).flatMap(i => previousDocumentFormDataRow("item", i)).toMap

  val documentationFormData = Map(
    "documentationAndReferences.header.additionalInformation.code" -> Seq("additionalInfoCode"),
    "documentationAndReferences.header.additionalInformation.description" -> Seq("additionalInfoDescription"),

    "documentationAndReferences.item.additionalInformation[0].code" -> Seq("itemAdditionalInfoCode 1"),
    "documentationAndReferences.item.additionalInformation[0].description" -> Seq("itemAdditionalInfoDescription 1"),
    "documentationAndReferences.item.additionalInformation[1].code" -> Seq("itemAdditionalInfoCode 2"),
    "documentationAndReferences.item.additionalInformation[1].description" -> Seq("itemAdditionalInfoDescription 2"),
    "documentationAndReferences.item.additionalInformation[2].code" -> Seq("itemAdditionalInfoCode 3"),
    "documentationAndReferences.item.additionalInformation[2].description" -> Seq("itemAdditionalInfoDescription 3"),
    "documentationAndReferences.item.additionalInformation[3].code" -> Seq("itemAdditionalInfoCode 4"),
    "documentationAndReferences.item.additionalInformation[3].description" -> Seq("itemAdditionalInfoDescription 4"),
    "documentationAndReferences.item.additionalInformation[4].code" -> Seq("itemAdditionalInfoCode 5"),
    "documentationAndReferences.item.additionalInformation[4].description" -> Seq("itemAdditionalInfoDescription 5"),
    "documentationAndReferences.item.additionalInformation[5].code" -> Seq("itemAdditionalInfoCode 6"),
    "documentationAndReferences.item.additionalInformation[5].description" -> Seq("itemAdditionalInfoDescription 6"),

    "documentationAndReferences.localReferenceNumber" -> Seq("localRef"),

    "documentationAndReferences.additionalPayment[0].additionalDocPaymentID" -> Seq("123456"),
    "documentationAndReferences.additionalPayment[0].additionalDocPaymentCategory" -> Seq("1"),
    "documentationAndReferences.additionalPayment[0].additionalDocPaymentType" -> Seq("DAN"),

    "documentationAndReferences.additionalPayment[1].additionalDocPaymentID" -> Seq("123456"),
    "documentationAndReferences.additionalPayment[1].additionalDocPaymentCategory" -> Seq("1"),
    "documentationAndReferences.additionalPayment[1].additionalDocPaymentType" -> Seq("DAN")
  )

}