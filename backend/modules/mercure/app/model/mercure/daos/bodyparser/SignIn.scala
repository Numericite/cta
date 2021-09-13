package model.mercure.daos.bodyparser

/**
 * Created by madalien on 25/11/15.
 */

import play.api.libs.json.Json

case class SignIn(
  email: String,
  password: String
)

object SignIn {

  /**
   * Converts the [SignUp] object to Json and vice versa.
   */

  import play.api.libs.json.Json
  implicit val SignInFormat = Json.format[SignIn]

}
