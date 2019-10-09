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

import play.api.i18n.{Lang, Messages, MessagesImpl}
import javax.inject.{Inject, Singleton}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.cdsimportsddsfrontend.config.AppConfig
import uk.gov.hmrc.cdsimportsddsfrontend.services.AuthAction
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.submit_declaration
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.Future

@Singleton
class SubmitDeclarationController @Inject() ( submitTemplate: submit_declaration, authenticate: AuthAction  )
                                            ( implicit val appConfig: AppConfig,
                                              val mcc: MessagesControllerComponents ) extends FrontendController(mcc) {

  val renderTemplate: Action[AnyContent] = Action { implicit request =>
    Ok(submitTemplate())
  }

  val declarationForm: Form[SubmitDeclarationModel] = Form(
    mapping(
      "declaration-data" -> nonEmptyText
    )(SubmitDeclarationModel.apply)(SubmitDeclarationModel.unapply)
  )

  val submit: Action[AnyContent] = Action { implicit request =>
    declarationForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("ERROR! Submit failed" + formWithErrors)
      },
      data => {
        Ok("SUCCESS! Form submitted")
      }
    )
  }

}

case class SubmitDeclarationModel(textarea: String)

