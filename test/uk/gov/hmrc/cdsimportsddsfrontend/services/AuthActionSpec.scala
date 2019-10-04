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
import com.gu.scalatest.JsoupShouldMatchers
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.api.test.Helpers._
import play.mvc.Http.Status
import uk.gov.hmrc.auth.core.{AffinityGroup, Enrolments}
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Name}
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.UnauthorisedController
import uk.gov.hmrc.cdsimportsddsfrontend.domain.SignedInUser
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.not_subscribed_to_cds
import scala.concurrent.Future

class AuthActionSpec extends CdsImportsSpec with AuthenticationBehaviours with JsoupShouldMatchers  {

  //val authAction = new AuthAction(authConnector = mockAuthConnector, appConfig = appConfig)
  val controller = new MyFakeController(mockAuthAction)(mcc)

  def notEnrolledUser(): SignedInUser = SignedInUser(
    Some(Credentials("2345235235","GovernmentGateway")),
    Some(Name(Some("Aldo"),Some("Rain"))),
    Some("amina@hmrc.co.uk"),
    "",
    Some(AffinityGroup.Individual),
    Some("Int-ba17b467-90f3-42b6-9570-73be7b78eb2b"),
    Enrolments(Set())
  )


  "the action" should {

    "redirect to the Government Gateway sign-in page when no authenticated user" in notSignedInScenario() {
      val response = controller.dummyAction()(req)
      status(response) must be (Status.SEE_OTHER)
      header(LOCATION, response) must be (Some(expectedSignInRedirectPathFor(req)))
    }

    "proceed with wrapped request containing signed in user details when authenticated" in signedInScenario { someRegisteredUser =>
      val resp = controller.dummyAction()(req)
      status(resp) must be (Status.OK)
      controller.theUser must be (Some(someRegisteredUser))
    }

    "redirect to not subscribed page when user is not subscribed to CDS" in signedInScenario(notEnrolledUser()) { notCDSUser =>
      val response = controller.dummyAction()(req)
      status(response) must be (Status.SEE_OTHER)
      header(LOCATION, response) mustBe Some("/cds-imports-dds-frontend/not-subscribed-for-cds")
    }

    "check page content on not subscribed page when user is not subscribed to CDS" in signedInScenario(notEnrolledUser()) { notSubscribedUser =>
      //pending   //TODO This testcase doesn't work: "java.lang.RuntimeException: There is no started application", either return GuiceOneAppPerSuite or figure it out
      val notSubscribedTemplate = new not_subscribed_to_cds(mainTemplate)
      val controller = new UnauthorisedController(notSubscribedTemplate)(appConfig,mcc)
      val response = controller.onPageLoad()(req)
      val html = contentAsString(response).asBodyFragment
      status(response) must be (Status.OK)
      html should include element withName("title").withValue("Not registered with CDS")
      html should include element withName("h1").withValue("To manage your customs payments you need to get access to CDS")
      html should include element withName("a").withValue("Economic Operator and Registration Identification (EORI) number")
        .withAttrValue("href", "/customs/register-for-cds")
      html should include element withName("a").withValue("get access to CDS here")
        .withAttrValue("href", "/customs/subscribe-for-cds")
    }
  }
}


class MyFakeController @Inject()(val authenticate: AuthAction)
                                (implicit val mcc: MessagesControllerComponents) extends FrontendController(mcc) {

  var theUser: Option[SignedInUser] = None

  def dummyAction: Action[AnyContent] = authenticate { implicit req =>
    theUser = Some(req.user)
    Ok
  }

}