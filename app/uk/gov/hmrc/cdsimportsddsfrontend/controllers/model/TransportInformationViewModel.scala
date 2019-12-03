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
                                         modeOfTransportCode: Option[String] = Some("4"),
                                         arrivalTransportIdType: Option[String] = Some("10"),
                                         arrivalTransportId: Option[String] = Some("1023465738"),
                                         nationalityOfMeansOfTransportAtBorder: Option[String] = Some("US")) {

  def toBorderTransportMeans: BorderTransportMeans = {
    BorderTransportMeans(nationalityOfMeansOfTransportAtBorder, modeOfTransportCode)
  }

  def toArrivalTransportMeans: ArrivalTransportMeans = {
    ArrivalTransportMeans(arrivalTransportIdType, arrivalTransportId)
  }
}
