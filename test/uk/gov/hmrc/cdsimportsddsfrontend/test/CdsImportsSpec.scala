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
import play.api.http.{DefaultFileMimeTypes, FileMimeTypesConfiguration, HttpConfiguration}
import play.api.i18n._
import play.api.mvc._
import play.api.test.Helpers.{stubBodyParser, stubLangs, stubPlayBodyParsers}
import play.api.test.{FakeRequest, NoMaterializer}
import play.api.{Configuration, Environment, Mode}
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, ErrorHandler, FeatureSwitchRegistry}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{error_template, main_template, _}
import uk.gov.hmrc.govukfrontend.views.html.components._
import uk.gov.hmrc.govukfrontend.views.html.layouts._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.config.{AssetsConfig, GTMConfig, OptimizelyConfig}
import uk.gov.hmrc.play.views.html.layouts._

import scala.concurrent.ExecutionContext


trait AppConfigReader {
  val env: Environment = Environment.simple()
  val configuration: Configuration = Configuration.load(env)

  val serviceConfig: ServicesConfig = new ServicesConfig(configuration, new RunMode(configuration, Mode.Dev))
  implicit val appConfig: AppConfig = new AppConfig(configuration, serviceConfig, env)

}

trait CdsImportsSpec extends WordSpec with MustMatchers with AppConfigReader {

  val langs = new DefaultLangs()

  val messagesApiProvider = new DefaultMessagesApiProvider(
    environment = env,
    config = configuration,
    langs = langs,
    httpConfiguration = new HttpConfiguration()
  )

  val messagesApi: MessagesApi = messagesApiProvider.get


  implicit val mcc: MessagesControllerComponents = {
    val executionContext = ExecutionContext.global
    DefaultMessagesControllerComponents(
      messagesActionBuilder = new DefaultMessagesActionBuilderImpl(stubBodyParser(), messagesApi)(executionContext),
      actionBuilder = DefaultActionBuilder(stubBodyParser())(ExecutionContext.global),
      parsers = stubPlayBodyParsers(NoMaterializer),
      messagesApi = messagesApi,
      langs = stubLangs(),
      fileMimeTypes = new DefaultFileMimeTypes(FileMimeTypesConfiguration(Map.empty)),
      executionContext = executionContext
    )
  }

  implicit val featureSwitchRegistry: FeatureSwitchRegistry = new FeatureSwitchRegistry(appConfig, mcc)

  implicit val mat = NoMaterializer
  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  //TODO maybe move these to it's own trait?
  //Manually creating injectable views
  val optimizelyConfig = new OptimizelyConfig(configuration)
  val optimizelySnippet = new OptimizelySnippet(optimizelyConfig)
  val assetsConfig = new AssetsConfig(configuration)
  val gtmConfig = new GTMConfig(configuration)
  val gtmSnippet = new GTMSnippet(gtmConfig)
  val head = new Head(optimizelySnippet, assetsConfig, gtmSnippet)
  val govukTemplate = new govukTemplate(new GovukHeader, new GovukFooter, new GovukSkipLink)
  val mainTemplate = new main_template(new govukLayout(govukTemplate, new GovukHeader, new GovukFooter, new GovukBackLink), new head, new scripts)

  implicit val errorHandler = new ErrorHandler(new error_template(mainTemplate), messagesApi, appConfig)

  val somePath = "/some/resource/path"

  val fakeRequest = FakeRequest("GET", somePath)
  val fakeRequestWithCSRF = addCsrfToken(fakeRequest)

  def addCsrfToken[T](fakeRequest: FakeRequest[T]) :RequestHeader = {
    import play.api.test.CSRFTokenHelper._
    FakeRequest().withCSRFToken
  }

  //  // FeatureSwitch depends on play config from a running app, so this wrapper must be used within a test body
  //  def withFeatureSwitchEnabled(featureSwitches: FeatureName*)(test: => Unit): Unit = {
  //    featureSwitches.foreach(_.enable())
  //    test
  //    featureSwitches.foreach(_.disable())
  //  }

}
