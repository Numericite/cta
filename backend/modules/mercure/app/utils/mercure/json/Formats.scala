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
package utils.mercure.json

import com.mohiva.play.silhouette.api.util.PasswordInfo
import play.api.libs.json._

/**
 * Implicit formats for Silhouette classes.
 */
object Formats {

  /**
   * Converts a [[com.mohiva.play.silhouette.api.util.PasswordInfo]] instance to JSON and vice versa.
   */

  import reactivemongo.play.json.BSONFormats._
  implicit val passwordInfoFormat = Json.format[PasswordInfo]

}
