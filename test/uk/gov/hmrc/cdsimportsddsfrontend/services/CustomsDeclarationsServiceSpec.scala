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

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse
import uk.gov.hmrc.cdsimportsddsfrontend.test.AppConfigReader
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CustomsDeclarationsServiceSpec extends WordSpec
  with AppConfigReader with MustMatchers with MockitoSugar with FutureAwaits with DefaultAwaitTimeout {

  val testEori = "GB1234567890"

  class Scenario() {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    val mockHttp:HttpClient = mock[HttpClient]
    val customsDeclarationsService:CustomsDeclarationsService = new CustomsDeclarationsService(appConfig)(mockHttp, implicitly)
  }

  "The service" should {
    "Post a declaration to the Declaration API" in new Scenario() {
      when[Future[String]](mockHttp.POST(any(),any(),any())(any(), any(), any(), any())).thenReturn(Future.successful("The Response"))
      val response = await(customsDeclarationsService.submit(testEori, <DeclaringMyStuff/>))
      response mustBe CustomsDeclarationsResponse(200,Some("The Response"))
    }
  }
}
