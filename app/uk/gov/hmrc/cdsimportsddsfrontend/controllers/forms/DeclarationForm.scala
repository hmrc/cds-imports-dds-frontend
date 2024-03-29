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

package uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms

import play.api.data.Form
import play.api.data.Forms._
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.DeclarationTypeFormMapping.declarationType
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.DocumentationAndReferencesFormMapping.documentationAndReferences
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.GoodsIdentificationFormMapping.goodsIdentification
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.PartiesFormMapping.parties
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.TransportInformationFormMapping.transportInformation
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.ValuationInformationAndTaxesFormMapping.valuationInformationAndTaxes
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.WhenAndWhereFormMapping.whenAndWhere
import uk.gov.hmrc.cdsimportsddsfrontend.controllers.forms.MiscellaneousFormMapping.miscellaneous

import uk.gov.hmrc.cdsimportsddsfrontend.controllers.model.DeclarationViewModel

object DeclarationForm extends FormValidators {

  val form: Form[DeclarationViewModel] = Form(
    mapping(
      declarationType,
      documentationAndReferences,
      parties,
      valuationInformationAndTaxes,
      whenAndWhere,
      goodsIdentification,
      transportInformation,
      miscellaneous
    )(DeclarationViewModel.apply)(DeclarationViewModel.unapply)
  )
}
