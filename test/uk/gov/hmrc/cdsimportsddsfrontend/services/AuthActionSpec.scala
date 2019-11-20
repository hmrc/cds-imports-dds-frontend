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
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.when
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.api.test.Helpers._
import play.mvc.Http.Status
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Name}
import uk.gov.hmrc.auth.core.{AffinityGroup, Enrolments}
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.{AuthAction, UnauthorisedController}
import uk.gov.hmrc.cdsimportsddsfrontend.domain.SignedInUser
import uk.gov.hmrc.cdsimportsddsfrontend.test.{AuthenticationBehaviours, CdsImportsSpec}
import uk.gov.hmrc.cdsimportsddsfrontend.views.html.not_subscribed_to_cds
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

class AuthActionSpec extends CdsImportsSpec with AuthenticationBehaviours with JsoupShouldMatchers {

  val controller = new MyFakeController(mockAuthAction)(mcc)

  def notSubscribedUser(): SignedInUser = SignedInUser("", Enrolments(Set())
  )


  "the action" should {

    "redirect to the Government Gateway sign-in page when user has not authenticated" in notSignedInScenario() {
      val response = controller.dummyAction()(fakeRequest)
      status(response) must be (Status.SEE_OTHER)
      header(LOCATION, response) must be (Some(expectedSignInRedirectPathFor(fakeRequest)))
    }

    "proceed with wrapped request containing signed in user details when authenticated and subscribed to CDS" in signedInScenario { someRegisteredUser =>
      val resp = controller.dummyAction()(fakeRequest)
      status(resp) must be (Status.OK)
      controller.theUser must be (Some(someRegisteredUser))
    }

    "redirect to not subscribed page when user is authenticated but not subscribed to CDS" in signedInScenario(notSubscribedUser()) { notCDSUser =>
      val response = controller.dummyAction()(fakeRequest)
      status(response) must be (Status.SEE_OTHER)
      header(LOCATION, response).get must endWith("/not-subscribed-for-cds")
    }

    "render a Service Unavailable page when user is authenticated and subscribed to CDS but not whitelisted" in signedInScenario { notWhitelistedUser =>
      when(mockWhiteList.allows(ArgumentMatchers.any())).thenReturn(false)
      val response = controller.dummyAction()(fakeRequest)
      status(response) must be (Status.SERVICE_UNAVAILABLE)
      val html = contentAsString(response).asBodyFragment
      html should include element withName("title").withValue("Sorry, the service is unavailable")
      html should include element withName("h1").withValue("Sorry, the service is unavailable")
    }

    "check page content on not subscribed page when user is not subscribed to CDS" in signedInScenario(notSubscribedUser()) { notSubscribedUser =>
      val notSubscribedTemplate = new not_subscribed_to_cds(mainTemplate)
      val controller = new UnauthorisedController(notSubscribedTemplate)(appConfig,mcc)
      val response = controller.onPageLoad()(fakeRequest)
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