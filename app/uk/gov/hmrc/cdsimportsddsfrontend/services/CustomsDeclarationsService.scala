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

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.http.{ContentTypes, HeaderNames}
import play.api.mvc.Codec
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{CustomsDeclarationsResponse, CustomsHeaderNames, Eori}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.CustomsDeclarationsResponse.responseReader
import uk.gov.hmrc.http._
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Elem

@Singleton
class CustomsDeclarationsService @Inject()(appConfig: AppConfig)(implicit val httpClient:HttpClient, executionContext: ExecutionContext) {

  val log = Logger(this.getClass)

  def submit(eori:Eori, declaration: Elem)(implicit hc:HeaderCarrier):Future[CustomsDeclarationsResponse] = {
    val updatedHeaderCarrier = hc.copy(extraHeaders = Seq(
      "X-Client-ID" -> appConfig.declarationsApi.clientId,
      HeaderNames.ACCEPT -> s"application/vnd.hmrc.${appConfig.declarationsApi.apiVersion}+xml",
      HeaderNames.CONTENT_TYPE -> ContentTypes.XML(Codec.utf_8),
      CustomsHeaderNames.XEoriIdentifierHeaderName -> eori
    ))

    httpClient.POSTString[CustomsDeclarationsResponse](appConfig.declarationsApi.submitEndpoint, declaration.toString())(responseReader,updatedHeaderCarrier,implicitly) //Calling POST will add quotes around the xml
      .map{a => log.info("Response from Declaration API: " + a);a}
  }



}


