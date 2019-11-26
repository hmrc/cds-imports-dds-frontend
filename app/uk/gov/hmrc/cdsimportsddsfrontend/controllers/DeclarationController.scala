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
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, ErrorHandler, FeatureSwitchRegistry}
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.DeclarationForm
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.DeclarationViewModel
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.declaration_result
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.declaration
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DeclarationController @Inject()(simplifiedDeclaration: declaration
                                      , resultTemplate: declaration_result
                                      , declarationService: CustomsDeclarationsService
                                      , declarationStore: DeclarationStore
                                      , authenticate: AuthAction)
                                     (implicit val appConfig: AppConfig,
                                                featureSwitchRegistry: FeatureSwitchRegistry,
                                                errorHandler: ErrorHandler,
                                                mcc: MessagesControllerComponents)
  extends FrontendController(mcc) with I18nSupport {

  def show(): Action[AnyContent] = (featureSwitchRegistry.SinglePageDeclaration.action andThen authenticate) async { implicit req =>
    Future.successful(Ok(simplifiedDeclaration(DeclarationForm.form.fill(DeclarationViewModel()))))
  }

  def submit(): Action[AnyContent] = (featureSwitchRegistry.SinglePageDeclaration.action andThen authenticate) async { implicit request =>
    val rawFormData = request.body.asFormUrlEncoded
    val trimmedFormData = trimTrailingWhitespace(rawFormData)
    DeclarationForm.form.bindFromRequest(trimmedFormData).fold(
      formWithErrors =>
        Future.successful(BadRequest(simplifiedDeclaration(formWithErrors))),

      validDeclaration => {
        declarationStore.deleteAllNotifications()
        declarationService.submit(request.user.eori, validDeclaration)
          .map(declaration => Ok(resultTemplate(declaration)))
      }
    )
  }

  private def trimTrailingWhitespace(maybeFormData: Option[Map[String, Seq[String]]]): Map[String, Seq[String]] = {
    maybeFormData.map { formData =>
      formData.map { case (key, values) => (key, values.map(_.trim)) }
    }.getOrElse(Map())
  }
}
