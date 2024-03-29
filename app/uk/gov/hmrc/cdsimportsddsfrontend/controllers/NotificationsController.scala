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

import cats.implicits._
import javax.inject.{Inject, Singleton}
import play.api.http.HeaderNames
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.domain._
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsHeaderNames, DeclarationStore}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.view_notifications

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class NotificationsController @Inject()(declarationStore: DeclarationStore, viewTemplate:view_notifications)
(implicit mcc: MessagesControllerComponents, appConfig:AppConfig)
  extends FrontendController(mcc) with I18nSupport {

  //This is called by the Customs Declarations API, notifying us about previously submitted declarations
  def handleNotification: Action[AnyContent] = Action async { implicit request =>
    val maybeXmlBody = request.body.asXml   //TODO Why does this not work with BodyParsers? Action(parse.xml)

    val notifications = for {
      authToken <- Either.fromOption(request.headers.get(HeaderNames.AUTHORIZATION), "No Authorization Header")
      conversationId <- Either.fromOption(request.headers.get(CustomsHeaderNames.ConversationId), "No X-Conversation-ID Header")
      xmlBody <- Either.fromOption(maybeXmlBody, "Body is not xml")
      notifications <- Notification.buildNotificationsFromRequest(NotificationApiRequestHeaders(AuthToken(authToken), ConversationId(conversationId)),xmlBody)
    } yield Future.sequence(notifications.map(declarationStore.putNotification))

    notifications match {
      case Left(message) => Future.successful(BadRequest(message))
      case Right(eventualNotificationPutResults) => {
        eventualNotificationPutResults.map { _.exists(!_) match {
            case true => InternalServerError
            case false => Accepted
          }
        }
      }
    }
  }


  def show = Action.async { implicit request =>
    declarationStore.getNotifications().map(ns => Ok(viewTemplate(ns)))
  }

}
