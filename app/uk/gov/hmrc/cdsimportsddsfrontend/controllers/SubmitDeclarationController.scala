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
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.XmlDeclarationFormMapping
import uk.gov.hmrc.cdsimportsddsfrontend.domain.XmlDeclaration
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.services.xml.DeclarationXml
import uk.gov.hmrc.cdsimportsddsfrontend.services.{CustomsDeclarationsService, DeclarationStore}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{declaration_result, submit_declaration}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class SubmitDeclarationController @Inject()(submitTemplate: submit_declaration,
                                            resultTemplate: declaration_result,
                                            authenticate: AuthAction,
                                            declarationService: CustomsDeclarationsService,
                                            declarationStore: DeclarationStore)
                                           (implicit val appConfig: AppConfig,
                                            mcc: MessagesControllerComponents) extends FrontendController(mcc) with I18nSupport {

  val show: Action[AnyContent] = authenticate.async { implicit request =>
    val exampleXml = DeclarationXml.goodDeclaration()
    val form = XmlDeclarationFormMapping.form.fill(XmlDeclaration(exampleXml))
    Future.successful(Ok(submitTemplate(form)))
  }

  val submit: Action[AnyContent] = authenticate.async { implicit request =>
    XmlDeclarationFormMapping.form.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(submitTemplate(formWithErrors)))
      },
      validatedForm => {
        declarationStore.deleteAllNotifications()
        declarationService.submit(request.user.eori, validatedForm.declarationXml)
          .map(declarationServiceResponse => Ok(resultTemplate(declarationServiceResponse)))
      }
    )
  }

}
