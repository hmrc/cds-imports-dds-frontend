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
import uk.gov.hmrc.cdsimportsddsfrontend.domain.Declaration
import uk.gov.hmrc.cdsimportsddsfrontend.services.{AuthAction, CustomsDeclarationsService, DeclarationStore, DeclarationXml}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{declaration_result, simplified_declaration}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SimplifiedDeclarationController @Inject()(simplifiedDeclaration: simplified_declaration
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
    Future.successful(Ok(simplifiedDeclaration(DeclarationForm.form.fill(Declaration()))))
  }

  def submit(): Action[AnyContent] = (featureSwitchRegistry.SinglePageDeclaration.action andThen authenticate) async { implicit request =>
    DeclarationForm.form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(simplifiedDeclaration(formWithErrors))),
      validDeclaration => {
        val xml = DeclarationXml.fromImportDeclaration(request.user.eori, validDeclaration)
        declarationStore.deleteAllNotifications()
        declarationService.submit(request.user.eori, xml)
          .map(declaration => Ok(resultTemplate(declaration, xml)))
      }
    )
  }
}
