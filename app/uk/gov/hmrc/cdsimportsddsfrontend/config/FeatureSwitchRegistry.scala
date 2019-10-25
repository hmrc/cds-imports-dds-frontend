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

package uk.gov.hmrc.cdsimportsddsfrontend.config

import com.google.inject.Inject
import play.api.mvc.Results.{NotFound, ServiceUnavailable}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

sealed trait FeatureState
case object Enabled extends { val configValue = "enabled" } with FeatureState
case object Disabled extends { val configValue = "disabled" } with FeatureState
case object Suspended extends { val configValue = "suspended" } with FeatureState


class FeatureSwitchRegistry @Inject()(appConfig: AppConfig, controllerComponents: ControllerComponents) {

  private val configuration = appConfig.config

  def getOption(name: String): Option[FeatureSwitch] = {
    name match {
      case HelloWorld.name => Some(HelloWorld)
      case SinglePageDeclaration.name => Some(SinglePageDeclaration)
      case _ => None
    }
  }

  sealed trait FeatureSwitch {

    val name: String

    val configPropertyName: String = s"features.$name"

    def isEnabled: Boolean = {
      maybeConfigValue match {
        case Some(Enabled.configValue) => true
        case _ => false
      }
    }

    def isSuspended: Boolean = {
      maybeConfigValue match {
        case Some(Suspended.configValue) => true
        case _ => false
      }
    }

    def enable() {
      setProp(Enabled.configValue)
    }

    def disable() {
      setProp(Disabled.configValue)
    }

    def suspend() {
      setProp(Suspended.configValue)
    }

    private def setProp(value: String) {
      sys.props += ((configPropertyName, value))
    }

    def action(implicit errorHandler: ErrorHandler): ActionBuilder[Request, AnyContent] with ActionFilter[Request] = {
      new ActionBuilder[Request, AnyContent] with ActionFilter[Request] {

        implicit override val executionContext: ExecutionContext = controllerComponents.executionContext
        override val parser: BodyParser[AnyContent] = controllerComponents.parsers.defaultBodyParser

        def filter[A](request: Request[A]): Future[Option[Result]] = Future.successful {
          maybeConfigValue match {
            case Some(Enabled.configValue) => None
            case Some(Suspended.configValue) => Some(ServiceUnavailable(errorHandler.internalServerErrorTemplate(request)))
            case _ => Some(NotFound(errorHandler.notFoundTemplate(request)))
          }
        }
      }
    }

    private def maybeConfigValue = {
      sys.props.get(configPropertyName).orElse(configuration.getOptional[String](configPropertyName))
    }
  }

  case object HelloWorld extends {val name = "hello-world"} with FeatureSwitch
  case object SinglePageDeclaration extends {val name = "single-page-declaration"} with FeatureSwitch

}
