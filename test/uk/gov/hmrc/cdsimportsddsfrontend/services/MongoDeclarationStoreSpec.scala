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

import java.time.LocalDateTime

import org.scalatest.{Assertion, BeforeAndAfterEach, MustMatchers, WordSpec}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import play.modules.reactivemongo.ReactiveMongoComponent
import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Notification, SubmissionStatus}
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AppConfigReader, CdsImportsSpec, MongoSpecSupport}
import uk.gov.hmrc.mongo.MongoConnector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MongoDeclarationStoreSpec extends WordSpec with MustMatchers with BeforeAndAfterEach with AppConfigReader
  with MongoSpecSupport with DefaultAwaitTimeout with FutureAwaits {

  val reactiveMongoForTest = new ReactiveMongoComponent {
    override def mongoConnector: MongoConnector = mongoConnectorForTest
  }
  val declarationStore = new MongoDeclarationStore(reactiveMongoForTest, appConfig)

  override def beforeEach: Unit = {
    await(declarationStore.removeAll())
  }

  "DeclarationStore" should {
    "successfully store and then retrieve a notification" in {
      val notification = Notification("actionId", "mrn", LocalDateTime.now(), SubmissionStatus.ACCEPTED, Seq.empty, "payload")
      await (for {
        _ <- declarationStore.insert(notification)
        notifications <- declarationStore.findAll()
        _ <- Future.successful(notifications.head mustBe notification)
      } yield ())

    }
  }

  // TODO test DeclarationStore interface...

}
