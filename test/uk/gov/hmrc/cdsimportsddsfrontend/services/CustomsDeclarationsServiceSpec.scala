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

package uk.gov.hmrc.cdsimportsddsfrontend.services

import org.mockito.ArgumentMatchers.{any, eq => meq}
import org.mockito.Mockito.when
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.Status
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.DeclarationViewModel
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.DeclarationXml
import uk.gov.hmrc.cdsimportsddsfrontend.test.AppConfigReader
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomsDeclarationsServiceSpec extends WordSpec
  with AppConfigReader with MustMatchers with MockitoSugar with FutureAwaits with DefaultAwaitTimeout {

  val testEori = "GB1234567890"

  class Scenario() {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    val mockHttp:HttpClient = mock[HttpClient]
    val mockDeclarationXml: DeclarationXml = mock[DeclarationXml]

    val customsDeclarationsService: CustomsDeclarationsService = new CustomsDeclarationsService(appConfig, mockDeclarationXml)(mockHttp, implicitly)
  }

  "The service" should {
    "Post a raw XML declaration to the Declaration API" in new Scenario() {
      val decApiResponse = CustomsDeclarationsResponse(200, Some("conversation id"))
      when[Future[CustomsDeclarationsResponse]](mockHttp.POSTString(any(),any(),any())(any(), any(), any())).thenReturn(Future.successful(decApiResponse))
      val response: DeclarationServiceResponse = await(customsDeclarationsService.submit(testEori, <DeclaringMyStuff/>, None))

      response.conversationId mustBe decApiResponse.conversationId
      response.status mustBe decApiResponse.status
      response.xml mustBe "&lt;DeclaringMyStuff/&gt;"
    }

    "maps view model to declaration, post to the Declaration API and save the submitted declaration" in new Scenario() {
      val decApiResponse = CustomsDeclarationsResponse(200, Some("conversation id"))
      val declarationViewModel = DeclarationViewModel()
      val lrn = declarationViewModel.documentationAndReferences.localReferenceNumber.getOrElse("")
      val saveDecHeaders = Seq(CustomsHeaderNames.EoriIdentifier -> testEori)

      when[Future[CustomsDeclarationsResponse]](mockHttp.POSTString(any(),any(),any())(any(), any(), any())).thenReturn(Future.successful(decApiResponse))

      when(mockDeclarationXml.fromImportDeclaration(meq(declarationViewModel.toDeclaration))).thenReturn(<DeclaringMyStuff/>)

      when[Future[HttpResponse]](mockHttp.POST(meq(appConfig.cdsImportsddsDeclarations), meq(lrn), meq(saveDecHeaders))
      (any(), any(), any(), any())).thenReturn(Future.successful(HttpResponse(Status.OK)))

      val response: DeclarationServiceResponse = await(customsDeclarationsService.submit(testEori, declarationViewModel))

      response.conversationId mustBe decApiResponse.conversationId
      response.status mustBe decApiResponse.status
      response.xml mustBe "&lt;DeclaringMyStuff/&gt;"
    }
  }
}
