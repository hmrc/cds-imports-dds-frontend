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

package uk.gov.hmrc.cdsimportsddsfrontend.domain

import uk.gov.hmrc.auth.core.{AffinityGroup, Enrolments}
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Name}

case class SignedInUser(credentials: Option[Credentials],
                        name: Option[Name],
                        email: Option[String],
                        eori: String,
                        affinityGroup: Option[AffinityGroup],
                        internalId: Option[String],
                        enrolments: Enrolments)