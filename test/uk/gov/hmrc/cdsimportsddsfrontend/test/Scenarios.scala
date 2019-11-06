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

package uk.gov.hmrc.cdsimportsddsfrontend.test

import akka.util.ByteString
import org.jsoup.nodes.Element
import play.api.libs.streams.Accumulator
import play.api.mvc.{AnyContentAsFormUrlEncoded, Request, Result}
import play.api.test.Helpers.contentAsString
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.DeclarationController
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{declaration, declaration_result}
import uk.gov.hmrc.govukfrontend.views.html.components.GovukButton

import scala.concurrent.Future

trait Scenarios extends AuthenticationBehaviours {
  self: CdsImportsSpec =>

  val mockDeclarationService: CustomsDeclarationsService = mock[CustomsDeclarationsService]
  val mockDeclarationStore: DeclarationStore = mock[DeclarationStore]

  class BaseScenario {
    val govukButton = new GovukButton()
    val formTemplate = new declaration(mainTemplate, govukButton)
    val resultTemplate = new declaration_result(mainTemplate)
    val controller = new DeclarationController(formTemplate, resultTemplate, mockDeclarationService, mockDeclarationStore, mockAuthAction)
  }

  class GetScenario extends BaseScenario {
    val response: Accumulator[ByteString, Result] = controller.show().apply(fakeRequestWithCSRF)
    val body: Element = contentAsString(response).asBodyFragment
  }

  class PostScenario(formData: Map[String, Seq[String]]) extends BaseScenario {
    val formRequest: Request[AnyContentAsFormUrlEncoded] = fakeRequestWithCSRF.withBody(AnyContentAsFormUrlEncoded(formData))
    val response: Future[Result] = controller.submit().apply(formRequest)
    val body: Element = contentAsString(response).asBodyFragment
  }

}
