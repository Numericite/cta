package services.mercure.token

import model.mercure.daos.token.Token
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
 * Created by madalien on 09/06/16.
 */

trait TokenService {

  /**
   * Creates a new token.
   *
   * The new token will be persisted so that later it can be retrieved by its ID.
   *
   * @param token The token to create.
   * @return The created token or None if the token couldn't be created.
   */
  def create(entity: String, token: Token): Future[WriteResult]

  /**
   * Retrieves an available token.
   *
   * @param id Tokenhe token ID.
   * @return Tokenhe retrieved token or None if no token could be retrieved for the given ID.
   */
  def retrieve(id: String): Future[Option[Token]]

  /**
   * Consumes a token.
   *
   * Tokenhis method makes the token unavailable for further use.
   * It's up to the implementation how to do that. For example, the token can be deleted,
   * updated as "consumed", moved to another table, etc.
   *
   * Consumed tokens can't be retrieved.
   *
   * @param id Tokenhe ID of the token to consume.
   */
  def consume(id: String): Future[WriteResult]
}
