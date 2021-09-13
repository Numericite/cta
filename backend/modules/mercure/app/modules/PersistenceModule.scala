/**
 * Copyright 2015 Mohiva Organisation (license at mohiva dot com)
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
package modules

import com.google.inject.{ AbstractModule, Provides }
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.{ OAuth2Info, OAuth1Info }
import com.mohiva.play.silhouette.persistence.daos._
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import net.codingwell.scalaguice.ScalaModule
import play.api.Configuration
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi

/**
 * Provides Guice bindings for the persistence module.
 */
class PersistenceModule extends AbstractModule with ScalaModule {

  override def configure() = {
  }

  /*
     * implement passwork
     */
  @Provides
  def providePasswordInfoDAO(reactiveMongoApi: ReactiveMongoApi, config: Configuration): DelegableAuthInfoDAO[PasswordInfo] = {
    implicit lazy val format = Json.format[PasswordInfo]
    new MongoAuthInfoDAO[PasswordInfo](reactiveMongoApi, config)
  }

  /*
   * implement oaut2 for facebook user
   */
  @Provides
  def provideOAuth1InfoDAO(reactiveMongoApi: ReactiveMongoApi, config: Configuration): DelegableAuthInfoDAO[OAuth2Info] = {
    implicit lazy val format = Json.format[OAuth2Info]
    new MongoAuthInfoDAO[OAuth2Info](reactiveMongoApi, config)
  }

  /**
   * Provides the auth info repository.
   *
   * @param passwordInfoDAO The implementation of the delegable password auth info DAO.
   * @return The auth info repository instance.
   */
  @Provides
  def provideAuthInfoRepository(
    passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo],
    oauth2InfoDAO: DelegableAuthInfoDAO[OAuth2Info]
  ): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoDAO, oauth2InfoDAO)
  }

}
