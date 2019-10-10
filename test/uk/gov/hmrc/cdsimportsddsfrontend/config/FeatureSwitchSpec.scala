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

package uk.gov.hmrc.cdsimportsddsfrontend.config

import org.scalatest.{AppendedClues, BeforeAndAfterEach}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import uk.gov.hmrc.cdsimportsddsfrontend.test.CdsImportsSpec

class FeatureSwitchSpec extends CdsImportsSpec
  with GuiceOneServerPerSuite with BeforeAndAfterEach with AppendedClues {

  override def beforeEach: Unit = {
    System.clearProperty("features.hello-world")
  }

  val featureSwitch = new FeatureSwitch(appConfig, mcc)

  "FeatureSwitch" should {

    "have the correct system property name for a feature" in {
      featureSwitch.HelloWorld.configPropertyName must be("features.hello-world")
    }

    "be able to look up a feature by name" in {
      featureSwitch.forName("hello-world") must be (featureSwitch.HelloWorld)
    }

    "be ENABLED if the system property is defined as 'enabled'" in {
      System.setProperty("features.hello-world", "enabled")
      featureSwitch.forName("hello-world").isEnabled must be(true)
    }

    "be DISABLED if the system property is not defined" in {
      featureSwitch.forName("hello-world").isEnabled must be(false)
    }

    "be DISABLED if the system property is defined as 'disabled'" in {
      System.setProperty("features.hello-world", "disabled")
      featureSwitch.forName("hello-world").isEnabled must be(false)
    }

    "support dynamic toggling" in {
      System.setProperty("features.hello-world", "false")
      featureSwitch.HelloWorld.enable()
      featureSwitch.forName("hello-world").isEnabled must be(true) withClue ", feature hello-world is not turned on"
      featureSwitch.HelloWorld.disable()
      featureSwitch.forName("hello-world").isEnabled must be(false) withClue ", feature hello-world is not turned off"
    }

  }

}
