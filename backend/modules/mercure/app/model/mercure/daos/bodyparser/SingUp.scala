package model.mercure.daos.bodyparser

import play.api.libs.json.Json

/**
 * Created by Mamadou Coulibaly on 25/11/15.
 */
case class SignUp(
  pseudo: String,
  device_id: String,
  orientation: Int,
  session_span: Long,
  geoloc: List[Double],
  gender: Int,
  avatarURL: Option[String]
)

object SignUpFormat {

  /**
   * Converts the [SignUp] object to Json and vice versa.
   */
  import play.api.libs.json.Json
  implicit val SignUpFormat = Json.format[SignUp]

}
