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

import javax.inject.Inject
import play.api.Configuration
import uk.gov.hmrc.individualsbenefitsandcreditsapi.config.{
  ApiConfig,
  EndpointConfig
}

class ScopesService @Inject()(configuration: Configuration) {

  private[service] lazy val apiConfig =
    configuration.get[ApiConfig]("api-config")

  def getAllScopes: List[String] = apiConfig.scopes.map(_.name).sorted

  def getScopeItemsKeys(scope: String): List[String] =
    apiConfig
      .getScope(scope)
      .map(s => s.fields)
      .getOrElse(List())

  def getScopeItems(scope: String): List[String] =
    getScopeItemsKeys(scope)
      .flatMap(fieldId =>
        apiConfig.endpoints.flatMap(e => e.fields.get(fieldId)))

  def getEndpointFieldKeys(endpointKey: String): Iterable[String] =
    apiConfig
      .getEndpoint(endpointKey)
      .map(endpoint => endpoint.fields.keys.toList.sorted)
      .getOrElse(List())

  def getFieldNames(keys: Iterable[String]): Iterable[String] =
    apiConfig.endpoints
      .map(e => e.fields)
      .flatMap(value => keys.map(value.get))
      .flatten

  def getValidItemsFor(scopes: Seq[String],
                       endpoint: String): Iterable[String] = {
    val uniqueDataFields = scopes.flatMap(getScopeItemsKeys).distinct
    val endpointDataItems = getEndpointFieldKeys(endpoint).toSet
    val authorizedDataItemsOnEndpoint =
      uniqueDataFields.filter(endpointDataItems.contains)
    getFieldNames(authorizedDataItemsOnEndpoint)
  }

  def getValidFieldsForCacheKey(scopes: Iterable[String],
                                endpoints: List[String]): String = {

    val uniqueDataFields = scopes.flatMap(getScopeItemsKeys).toList.distinct
    val endpointDataItems =
      endpoints.flatMap(e => getEndpointFieldKeys(e).toSet)
    val keys = uniqueDataFields.filter(endpointDataItems.contains)

    keys.nonEmpty match {
      case true => keys.reduce(_ + _)
      case _    => ""
    }
  }

  def getAccessibleEndpoints(scopes: Iterable[String]): Iterable[String] = {
    val scopeKeys = scopes.flatMap(s => getScopeItemsKeys(s))
    apiConfig.endpoints
      .filter(endpoint =>
        endpoint.fields.keySet.exists(scopeKeys.toList.contains))
      .map(endpoint => endpoint.name)
  }

  def getEndpointLink(endpoint: String): Option[String] =
    apiConfig.getEndpoint(endpoint).map(c => c.link)

  def getLinks(scopes: List[String]): Map[String, String] =
    getAccessibleEndpoints(scopes)
      .flatMap(endpoint =>
        apiConfig.getEndpoint(endpoint).map(c => (c.name, c.link)))
      .toMap

  def getEndpoints(scopes: Iterable[String]): Iterable[EndpointConfig] =
    getAccessibleEndpoints(scopes)
      .flatMap(endpoint => apiConfig.getEndpoint(endpoint))

  def getEndPointScopes(endpointKey: String): Iterable[String] = {
    val keys = apiConfig
      .getEndpoint(endpointKey)
      .map(endpoint => endpoint.fields.keys.toList.sorted)
      .getOrElse(List())

    apiConfig.scopes
      .filter(
        s => s.fields.toSet.intersect(keys.toSet).nonEmpty
      )
      .map(
        s => s.name
      )
      .sorted
  }
}
