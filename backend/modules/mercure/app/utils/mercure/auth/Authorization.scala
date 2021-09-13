package utils.mercure.auth

/**
 * Created by madalien on 18/05/16.
 */

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.{ JWTAuthenticator }
import model.mercure.daos.user.User
import play.api.mvc.Request
import play.api.i18n.Messages
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Only allows those users that have at least a role of the selected.
 * Master role is always allowed.
 * Ex: WithRole("high", "sales") => only users with roles "high" OR "sales" (or "master") are allowed.
 */
case class WithRole(anyOf: String*) extends Authorization[User, JWTAuthenticator] {
  override def isAuthorized[A](user: User, authenticator: JWTAuthenticator)(implicit r: Request[A]) = Future.successful {
    WithRole.isAuthorized(user, anyOf: _*)
  }
}
object WithRole {
  def isAuthorized(user: User, anyOf: String*): Boolean =
    anyOf.intersect(user.roles).size > 0 || user.roles.contains("Administrator")
}

/**
 * Only allows those users that have every of the selected roles.
 * Master role is always allowed.
 * Ex: Restrict("high", "sales") => only users with roles "high" AND "sales" (or "master") are allowed.
 */
case class WithRoles(allOf: String*) extends Authorization[User, JWTAuthenticator] {
  override def isAuthorized[A](user: User, authenticator: JWTAuthenticator)(implicit r: Request[A]) = Future.successful {
    WithRoles.isAuthorized(user, allOf: _*)
  }
}

object WithRoles {
  def isAuthorized(user: User, allOf: String*): Boolean =
    allOf.intersect(user.roles).size == allOf.size || user.roles.contains("Administrator")
}

case class IsOwner(allOf: String) extends Authorization[User, JWTAuthenticator] {
  override def isAuthorized[A](user: User, authenticator: JWTAuthenticator)(implicit r: Request[A]) = Future.successful {
    IsOwner.isAuthorized(user, allOf)
  }
}

object IsOwner {
  def isAuthorized(user: User, allOf: String): Boolean =
    allOf.intersect(user.email).size == allOf.size || allOf.intersect(user.userID.toString).size == allOf.size || user.roles.contains("Administrator")
}

