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

import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import uk.gov.hmrc.cdsimportsddsfrontend.config.{ErrorHandler, FeatureSwitchRegistry}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.services.AuthAction
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.hello_world

import scala.concurrent.Future

@Singleton
class HelloWorldController @Inject()(helloWordTemplate: hello_world)
                                    (implicit appConfig: AppConfig,
                                     featureSwitchRegistry: FeatureSwitchRegistry,
                                     mcc: MessagesControllerComponents,
                                     errorHandler: ErrorHandler)
  extends FrontendController(mcc) with I18nSupport {

  val helloWorld: Action[AnyContent] = featureSwitchRegistry.HelloWorld.action async { implicit request =>
    Future.successful(Ok(helloWordTemplate()))
  }

}

@Singleton
class TestController @Inject()(authenticate: AuthAction, mcc: MessagesControllerComponents) extends FrontendController(mcc) {

  val test: Action[AnyContent] = authenticate.async { implicit request =>
    Future.successful(Ok("You are logged in"))
  }
}
