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

package uk.gov.hmrc.cdsimportsddsfrontend.test

import java.net.URLEncoder

import com.gu.scalatest.JsoupShouldMatchers
import org.hamcrest.Description
import org.mockito.Mockito._
import org.mockito.{ArgumentMatcher, ArgumentMatchers}
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.Request
import play.api.test.DefaultAwaitTimeout
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals._
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Name, Retrieval, ~}
import uk.gov.hmrc.cdsimportsddsfrontend.config.EoriWhitelist
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.AuthAction
import uk.gov.hmrc.cdsimportsddsfrontend.domain.SignedInUser
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future
import scala.util.Random

trait AuthenticationBehaviours extends MockitoSugar {
  self: CdsImportsSpec =>

  val EORI_LENGTH = 8

  def randomEori: String = Random.alphanumeric.take(EORI_LENGTH).mkString

  def subscribedUser(eori: String): SignedInUser = SignedInUser(
    eori,
    Enrolments(Set(
      Enrolment("IR-SA", List(EnrolmentIdentifier("UTR", "111111111")), "Activated", None),
      Enrolment("IR-CT", List(EnrolmentIdentifier("UTR", "222222222")), "Activated", None),
      Enrolment("HMRC-CUS-ORG", List(EnrolmentIdentifier("EORINumber", eori)), "Activated", None)
    ))
  )

  val hasNoAuthHeader = new ArgumentMatcher[HeaderCarrier] {
    def matches(item: HeaderCarrier): Boolean = item != null && item.authorization.isEmpty

    def describeTo(description: Description): Unit = description.appendText("has no Authorization header")
  }

  val mockWhiteList: EoriWhitelist = mock[EoriWhitelist]
  when(mockWhiteList.allows(ArgumentMatchers.any())).thenReturn(true)

  val mockAuthConnector: AuthConnector = mock[AuthConnector]
  val mockAuthAction: AuthAction = new AuthAction(mockAuthConnector, appConfig, mockWhiteList, errorHandler, mcc)

  def signedInScenario(test: SignedInUser => Unit): Unit = {
    signedInScenario(subscribedUser(randomEori))(test)
  }

  def signedInScenario(user: SignedInUser)(test: SignedInUser => Unit): Unit = {
    when(
      mockAuthConnector
        .authorise(
          ArgumentMatchers.any(),
          ArgumentMatchers.eq(allEnrolments))(ArgumentMatchers.any(), ArgumentMatchers.any()
        )
    ).thenReturn(
      Future.successful(user.enrolments)
    )
    test(user)
  }

  def notSignedInScenario()(test: => Unit): Unit = {
    when(
      mockAuthConnector
        .authorise(
          ArgumentMatchers.any(),
          ArgumentMatchers.any[Retrieval[_]])(ArgumentMatchers.argThat(hasNoAuthHeader), ArgumentMatchers.any()
        )
    ).thenReturn(
      Future.failed(new NoActiveSession("A user is not logged in") {})
    )
    test
  }

  def expectedSignInRedirectPathFor(request: Request[_]): String = {
    val encodedContinueUrl = URLEncoder.encode(request.uri, "UTF-8")
    val expectedRedirectPath = s"/gg/sign-in?continue=$encodedContinueUrl&origin=cds-imports-dds-frontend"
    expectedRedirectPath
  }

}
