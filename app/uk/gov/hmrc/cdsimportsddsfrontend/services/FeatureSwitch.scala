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

import com.google.inject.Inject
import play.api.mvc.Results.NotFound
import play.api.mvc.{ActionBuilder, ActionFilter, AnyContent, BodyParser, MessagesControllerComponents, Request, Result}
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, ErrorHandler}

import scala.concurrent.{ExecutionContext, Future}

class FeatureSwitch @Inject()(appConfig: AppConfig, mcc: MessagesControllerComponents) {

  private val configuration = appConfig.config

  def forName(name: String): FeatureName = {
    name match {
      case HelloWorld.name => HelloWorld
    }
  }

  sealed trait FeatureName {

    val name: String

    val configPropertyName: String = s"features.$name"

    def isEnabled: Boolean = {
      val configValue = sys.props.get(configPropertyName).orElse(configuration.getOptional[String](configPropertyName))
      configValue match {
        case Some("enabled") => true
        case _ => false
      }
    }

    def enable() {
      setProp("enabled")
    }

    def disable() {
      setProp("disabled")
    }

    // TODO suspend()

    private def setProp(value: String) {
      sys.props += ((configPropertyName, value))
    }

    def action(implicit errorHandler: ErrorHandler): ActionBuilder[Request, AnyContent] with ActionFilter[Request] = {
      new ActionBuilder[Request, AnyContent] with ActionFilter[Request] {

        implicit override val executionContext: ExecutionContext = mcc.executionContext
        override val parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser

        def filter[A](input: Request[A]): Future[Option[Result]] = Future.successful {
          if (isEnabled) {
            None
          }
          else {
            Some(NotFound(errorHandler.notFoundTemplate(input)))
          }
        }
      }
    }
  }

  case object HelloWorld extends {val name = "hello-world"} with FeatureName

}
