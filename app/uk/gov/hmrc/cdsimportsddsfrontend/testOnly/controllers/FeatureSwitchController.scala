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

package uk.gov.hmrc.cdsimportsddsfrontend.testOnly.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, FeatureSwitchRegistry}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController


@Singleton
class FeatureSwitchController @Inject()(featureSwitchRegistry: FeatureSwitchRegistry)
                                       (implicit val appConfig: AppConfig, mcc: MessagesControllerComponents)
  extends FrontendController(mcc) {

  def enable(featureName: String): Action[AnyContent] = Action { implicit request =>
    featureSwitchRegistry.getOption(featureName) match {
      case Some(featureSwitch) =>
        featureSwitch.enable()
        Ok(s"Enabled feature $featureName")
      case _ =>
        BadRequest(s"No such feature $featureName")
    }
  }

  def disable(featureName: String): Action[AnyContent] = Action { implicit request =>
    featureSwitchRegistry.getOption(featureName) match {
      case Some(featureSwitch) =>
        featureSwitch.disable()
        Ok(s"Disabled feature $featureName")
      case _ =>
        BadRequest(s"No such feature $featureName")
    }
  }

}
