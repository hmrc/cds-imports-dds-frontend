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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.response.DeclarationServiceResponse
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Declaration, Eori}
import uk.gov.hmrc.http._
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.Elem

@Singleton
class CustomsDeclarationsService @Inject()(appConfig: AppConfig, declarationXml: DeclarationXml)
                                          (implicit val httpClient: HttpClient, executionContext: ExecutionContext) {

  val log = Logger(this.getClass)

  def submit(eori: Eori, declaration: Declaration)(implicit hc: HeaderCarrier): Future[DeclarationServiceResponse] = {
    val xml = declarationXml.fromImportDeclaration(declaration)
    submit(eori, xml)
  }

  def submit(eori: Eori, xml: Elem)(implicit hc: HeaderCarrier): Future[DeclarationServiceResponse] = {
    httpClient.POSTString[CustomsDeclarationsResponse](
      appConfig.declarationsApi.submitEndpoint, xml.toString(), headers = headers(eori))(responseReader, hc, executionContext)
      .map { customsDeclarationsResponse: CustomsDeclarationsResponse =>
        log.info("Response from Declaration API: " + customsDeclarationsResponse);
        DeclarationServiceResponse(DeclarationXml.prettyPrintToHtml(xml), customsDeclarationsResponse.status, customsDeclarationsResponse.conversationId)
      }
  }

  private val responseReader: HttpReads[CustomsDeclarationsResponse] = new HttpReads[CustomsDeclarationsResponse] {
    override def read(method: String, url: String, response: HttpResponse): CustomsDeclarationsResponse = {
      CustomsDeclarationsResponse(response.status, response.allHeaders.get(CustomsHeaderNames.ConversationId).flatMap(_.headOption))
    }
  }

  private def headers(eori: String): Seq[(String, String)] = Seq(
    "X-Client-ID" -> appConfig.declarationsApi.clientId,
    HeaderNames.ACCEPT -> s"application/vnd.hmrc.${appConfig.declarationsApi.apiVersion}+xml",
    HeaderNames.CONTENT_TYPE -> ContentTypes.XML(Codec.utf_8),
    CustomsHeaderNames.EoriIdentifier -> eori
  )

}

object CustomsHeaderNames {
  val EoriIdentifier = "X-EORI-Identifier"
  val ConversationId = "X-Conversation-ID"
}

private case class CustomsDeclarationsResponse(status: Int, conversationId: Option[String] = None)
