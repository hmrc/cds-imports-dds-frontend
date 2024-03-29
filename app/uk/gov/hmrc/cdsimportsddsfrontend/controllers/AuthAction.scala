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

import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import play.api.{Configuration, Environment}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.cdsimportsddsfrontend.config.{AppConfig, EoriWhitelist, ErrorHandler}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.SignedInUser
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.config.AuthRedirects

import scala.concurrent.{ExecutionContext, Future}

case class AuthenticatedRequest[A](request: Request[A], user: SignedInUser) extends WrappedRequest[A](request)

case class NotWhiteListed(msg: String = "User is not whitelisted") extends AuthorisationException(msg)


class AuthAction @Inject()(override val authConnector: AuthConnector,
                           appConfig: AppConfig,
                           whitelist: EoriWhitelist,
                           errorHandler: ErrorHandler,
                           mcc: MessagesControllerComponents)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionRefiner[Request, AuthenticatedRequest] with AuthorisedFunctions with AuthRedirects {

  override lazy val config: Configuration = appConfig.config
  override lazy val env: Environment = appConfig.environment

  implicit override val executionContext: ExecutionContext = mcc.executionContext
  override val parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser

  override protected def refine[A](request: Request[A]): Future[Either[Result, AuthenticatedRequest[A]]] = {
    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    authorised().retrieve(Retrievals.allEnrolments) {
      case allEnrolments =>
        implicit val enrollmentWriter = Json.writes[Enrolments]
        val eoriNumber = allEnrolments.getEnrolment("HMRC-CUS-ORG").flatMap(_.getIdentifier("EORINumber")) match {
          case Some(eori) => eori.value
          case None => throw InsufficientEnrolments()
        }

        if (whitelist.allows(eoriNumber)) {
          val cdsLoggedInUser = SignedInUser(eoriNumber, allEnrolments)
          val authenticatedRequest = AuthenticatedRequest(request, cdsLoggedInUser)
          Future.successful(Right(authenticatedRequest))
        }
        else
        {
          throw NotWhiteListed()
        }
    }
  } recover {
    case _: NoActiveSession =>
      Left(toGGLogin(continueUrl = request.path))
    case _: InsufficientEnrolments =>
      Left(Redirect(routes.UnauthorisedController.onPageLoad()))
    case _: NotWhiteListed =>
      Left(ServiceUnavailable(errorHandler.serviceUnavailableTemplate()(request)))
  }
}
