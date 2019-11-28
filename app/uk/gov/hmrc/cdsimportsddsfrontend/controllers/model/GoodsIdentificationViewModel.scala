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

import uk.gov.hmrc.cdsimportsddsfrontend.domain.{Classification, GoodsMeasure, Packaging}

case class GoodsIdentificationViewModel(
                                         netMass: Option[String] = Some("100"),
                                         supplementaryUnits: Option[String] = Some("100"),
                                         grossMass: Option[String] = Some("105"),
                                         description: Option[String] = Some("TSP no description required"),
                                         typeOfPackages: Option[String] = Some("BF"),
                                         numberOfPackages: Option[String] = Some("1"),
                                         shippingMarks: Option[String] = Some("TSP not required"),
                                         combinedNomenclatureCodeId: Option[String] = Some("18061015"),
                                         combinedNomenclatureCodeIdentificationType: Option[String] = Some("TSP"),
                                         taricCodeId: Option[String] = Some("10"),
                                         taricCodeIdentificationType: Option[String] = Some("TRC"),
                                         taricAdditionalCodeId: Option[String] = Some("1234"),
                                         taricAdditionalCodeIdentificationType: Option[String] = Some("TRA"),
                                         nationalAdditionalCodeId: Option[String] = Some("VATZ"),
                                         nationalAdditionalCodeIdentificationType: Option[String] = Some("GN")
                                       ) {

  def toGoodsMeasure: GoodsMeasure = GoodsMeasure(
    netNetWeightMeasure = this.netMass,
    tariffQuantity = this.supplementaryUnits,
    grossMassMeasure = this.grossMass
  )

  def toPackaging: Packaging = Packaging(
    typeCode = this.typeOfPackages,
    quantityQuantity = this.numberOfPackages,
    marksNumberId = this.shippingMarks
  )

  def toClassification(): Seq[Classification] = {
    Seq(Classification(combinedNomenclatureCodeId, combinedNomenclatureCodeIdentificationType),
      Classification(taricCodeId, taricCodeIdentificationType),
      Classification(taricAdditionalCodeId, taricAdditionalCodeIdentificationType),
        Classification(nationalAdditionalCodeId, nationalAdditionalCodeIdentificationType))
  }
}
