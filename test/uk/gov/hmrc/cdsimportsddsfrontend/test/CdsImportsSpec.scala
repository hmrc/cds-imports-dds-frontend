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

import org.scalatest.{MustMatchers, WordSpec}
import play.api.{Configuration, Environment, Mode}
import play.api.i18n.{DefaultLangs, DefaultMessagesApi}
import play.api.test.FakeRequest
import play.filters.csrf.CSRF.Token
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, ErrorHandler}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.bootstrap.tools.Stubs

import scala.concurrent.ExecutionContext

trait CdsImportsSpec extends WordSpec with MustMatchers {

  private val env           = Environment.simple()
  private val configuration = Configuration.load(env)

  private val serviceConfig = new ServicesConfig(configuration, new RunMode(configuration, Mode.Dev))
  val appConfig     = new AppConfig(configuration, serviceConfig,env)

  val mcc = Stubs.stubMessagesControllerComponents()

  implicit val messagesApi = new DefaultMessagesApi()  //(env, configuration, new DefaultLangs(configuration))
  //implicit val appConfig = new AppConfig(configuration, serviceConfig, env)
  //implicit val errorHandler = new ErrorHandler(messagesApi,appConfig)
  //implicit val ec: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global


  val somePath = "/some/resource/path"
  val req = FakeRequest("GET", somePath)

//  val csrfReq = addCsrfToken(req)
//
//  def addCsrfToken[T](fakeRequest: FakeRequest[T]): FakeRequest[T] = {
//    fakeRequest.copyFakeRequest(tags = fakeRequest.tags ++ Map(
//      Token.NameRequestTag -> "csrf-token-input-name",
//      Token.RequestTag -> java.util.UUID.randomUUID().toString
//    ))
//  }
//
//  // FeatureSwitch depends on play config from a running app, so this wrapper must be used within a test body
//  def withFeatureSwitchEnabled(featureSwitches: FeatureName*)(test: => Unit): Unit = {
//    featureSwitches.foreach(_.enable())
//    test
//    featureSwitches.foreach(_.disable())
//  }

}
