/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.individualsbenefitsandcreditsapi.service

import play.api.hal.Hal.{linksSeq, state}
import play.api.hal.{HalLink, HalResource}
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.JsValue

import java.util.UUID
import javax.inject.Inject

class ScopesHelper @Inject()(scopesService: ScopesService) {

  /**
    * @param scopes The list of scopes associated with the user
    * @param endpoint The endpoint that the user has called
    * @return A google fields-style query string with the fields determined by the provided endpoint and scopes
    */
  def getQueryStringFor(scopes: Seq[String], endpoint: String): String =
    PathTree(scopesService.getValidItemsFor(scopes, endpoint)).toString

  /**
    * @param endpoint The endpoint that the user has called
    * @param scopes The list of scopes associated with the user
    * @param data The data to be returned from the endpoint
    * @return A HalResource containing data, and a list of valid links determined by the provided scopes
    */
  def getHalResponse(endpoint: String,
                     scopes: List[String],
                     data: JsValue): HalResource = {
    val hateoasLinks = scopesService
      .getLinks(scopes)
      .map(link => HalLink(link._1, link._2))
      .toList ++
      Seq(HalLink("self", scopesService.getEndpointLink(endpoint).get))

    state(data) ++ linksSeq(hateoasLinks)
  }

  def getHalLinks(matchId: UUID, scopes: Iterable[String]): HalResource =
    linksSeq(
      scopesService
        .getEndpoints(scopes)
        .map(
          endpoint =>
            HalLink(rel = endpoint.name,
                    href = endpoint.link.replaceAllLiterally("<matchId>",
                                                             s"$matchId"),
                    title = Some(endpoint.title)))
        .toSeq)
}
