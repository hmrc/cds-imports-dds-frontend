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

class FeatureSwitchRegistrySpec extends CdsImportsSpec
  with GuiceOneServerPerSuite with BeforeAndAfterEach with AppendedClues {

  override def beforeEach: Unit = {
    System.clearProperty("features.hello-world")
  }

  "FeatureSwitchRegistry" should {
    "allow lookup of features by name" in {
      featureSwitchRegistry.getOption("hello-world") must be(Some(featureSwitchRegistry.HelloWorld))
    }

    "return None if the requested feature switch is not found" in {
      featureSwitchRegistry.getOption("goodbye-cruel-world") must be(None)
    }
  }

  "FeatureSwitch" should {
    val featureSwitch = featureSwitchRegistry.HelloWorld

    "have a fully-qualified property name" in {
      featureSwitch.configPropertyName must be("features.hello-world")
    }

    "isEnabled" should {
      "return true if the system property is defined as 'enabled'" in {
        System.setProperty("features.hello-world", "enabled")
        featureSwitch.isEnabled must be(true)
      }

      "return false if the system property is defined as 'disabled'" in {
        System.setProperty("features.hello-world", "disabled")
        featureSwitch.isEnabled must be(false)
      }
    }

    "support dynamic toggling" in {
      featureSwitch.enable()
      featureSwitch.isEnabled must be(true) withClue ", feature hello-world is not turned on"
      featureSwitch.disable()
      featureSwitch.isEnabled must be(false) withClue ", feature hello-world is not turned off"
    }

  }

}
