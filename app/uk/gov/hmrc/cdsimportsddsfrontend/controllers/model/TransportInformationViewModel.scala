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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.model

import uk.gov.hmrc.cdsimportsddsfrontend.domain.{ArrivalTransportMeans, BorderTransportMeans}

case class TransportInformationViewModel(container: Option[String] = Some("0"),
                                         modeOfTransport: Option[String] = Some("1"),
                                         transportIdentificationType: Option[String] = Some("10"),
                                         transportId: Option[String] = Some("1023465738"),
                                         registrationNationalityCode: Option[String] = Some("US")) {
  def toBorderTransportMeans(): BorderTransportMeans = {
    BorderTransportMeans(registrationNationalityCode, modeOfTransport)
  }

  def toArrivalTransportMeans(): ArrivalTransportMeans = {
    ArrivalTransportMeans(transportIdentificationType, transportId)
  }
}