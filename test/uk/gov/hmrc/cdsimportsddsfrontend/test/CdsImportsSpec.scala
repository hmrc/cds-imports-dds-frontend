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
import play.api.http.HttpConfiguration
import play.api.{Configuration, Environment, Mode}
import play.api.i18n._
import play.api.mvc.MessagesRequest
import play.api.test.FakeRequest
import play.filters.csrf.CSRF.Token
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, ErrorHandler}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{govuk_wrapper, main_template}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.bootstrap.tools.Stubs
import uk.gov.hmrc.play.config.{AssetsConfig, GTMConfig, OptimizelyConfig}
import uk.gov.hmrc.play.views.html.helpers.ReportAProblemLink
import uk.gov.hmrc.play.views.html.layouts._
import views.html.layouts.GovUkTemplate

import scala.concurrent.ExecutionContext
import play.api.http.{DefaultFileMimeTypes, FileMimeTypesConfiguration}
import play.api.mvc.{DefaultActionBuilder, DefaultMessagesActionBuilderImpl, DefaultMessagesControllerComponents, MessagesControllerComponents}
import play.api.test.Helpers.{stubBodyParser, stubLangs, stubMessagesApi, stubPlayBodyParsers}
import play.api.test.NoMaterializer


trait AppConfigReader {
  val env:Environment = Environment.simple()
  val configuration:Configuration = Configuration.load(env)

  val serviceConfig:ServicesConfig = new ServicesConfig(configuration, new RunMode(configuration, Mode.Dev))
  val appConfig:AppConfig = new AppConfig(configuration, serviceConfig, env)

}

trait CdsImportsSpec extends WordSpec with AppConfigReader with MustMatchers {

  val langs = new DefaultLangs()

  val messagesApiProvider = new DefaultMessagesApiProvider(
    environment = env,
    config = configuration,
    langs = langs,
    httpConfiguration = new HttpConfiguration()
  )

  val messagesApi:MessagesApi = messagesApiProvider.get


  val mcc: MessagesControllerComponents = {
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

  //implicit val errorHandler = new ErrorHandler(messagesApi,appConfig)
  //implicit val ec: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
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
  val footer = new Footer(assetsConfig)
  val govukWrapper = new govuk_wrapper(head, new HeaderNav(), footer, new FooterLinks(), new ServiceInfo(),
    new MainContentHeader(), new MainContent(), new ReportAProblemLink(), new GovUkTemplate())
  val mainTemplate = new main_template(new Sidebar(), new Article(), govukWrapper)

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
