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

import javax.inject._
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.bson.BSONObjectID
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Notification
import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats
import com.google.inject.ImplementedBy

import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[MongoDeclarationStore])
trait DeclarationStore {

  def putNotification(notification: Notification)(implicit ec: ExecutionContext): Future[Boolean]

  def getNotifications()(implicit ec: ExecutionContext): Future[Seq[Notification]]

  def deleteAllNotifications()(implicit ec: ExecutionContext): Future[Boolean]

}

@Singleton
class MongoDeclarationStore @Inject()(mongoComponent: ReactiveMongoComponent, appConfig: AppConfig)
  extends ReactiveRepository[Notification, BSONObjectID](
    collectionName = "declarationStore",
    mongo = mongoComponent.mongoConnector.db,
    domainFormat = Notification.notificationFormat,
    idFormat = ReactiveMongoFormats.objectIdFormats
  ) with DeclarationStore {

  override def putNotification(notification: Notification)
                              (implicit ec: ExecutionContext): Future[Boolean] = {
    insert(notification).map(_.ok)
  }

  override def getNotifications()(implicit ec: ExecutionContext): Future[Seq[Notification]] = {
    findAll()
  }

  override def deleteAllNotifications()(implicit ec: ExecutionContext): Future[Boolean] = {
    removeAll().map(_.ok)
  }

}
