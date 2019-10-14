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

import java.io.StringReader

import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.services.{AuthAction, CustomsDeclarationsService, DeclarationXml}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.{submit_declaration,declaration_result}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import scala.xml.InputSource


@Singleton
class SubmitDeclarationController @Inject()(submitTemplate: submit_declaration,
                                            resultTemplate: declaration_result,
                                            authenticate: AuthAction,
                                            declarationService: CustomsDeclarationsService)
                                           (implicit val appConfig: AppConfig,
                                            mcc: MessagesControllerComponents) extends FrontendController(mcc) with I18nSupport {

  val renderTemplate: Action[AnyContent] = authenticate.async { implicit request =>
    val exampleXml = DeclarationXml.build(request.user.eori).toString()
    val form = SubmitDeclarationModel.form.fill(SubmitDeclarationModel(exampleXml))
    Future.successful(Ok(submitTemplate(form)))
  }

  val submit: Action[AnyContent] = authenticate.async { implicit request =>
    SubmitDeclarationModel.form.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(submitTemplate(formWithErrors)))
      },
      data => {
        val xml = scala.xml.XML.load(new InputSource(new StringReader(data.textarea)))
        declarationService.submit(request.user.eori, xml)
          .map(declaration => Ok(resultTemplate(declaration)))
      }
    )
  }

}

case class SubmitDeclarationModel(textarea: String) //TODO Make this into xml, then we don't have to re-parse it!

object SubmitDeclarationModel {

  val FieldName = "declaration-data"

  val verifyXML: String => Boolean = xml => Try(scala.xml.XML.load(new InputSource(new StringReader(xml)))).isSuccess

  val form: Form[SubmitDeclarationModel] = Form(
    mapping(
      FieldName -> nonEmptyText.verifying("declaration.not.xml", verifyXML)
    )(SubmitDeclarationModel.apply)(SubmitDeclarationModel.unapply)
  )

}