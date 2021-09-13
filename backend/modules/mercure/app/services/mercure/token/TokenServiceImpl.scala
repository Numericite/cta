package services.mercure.token

import com.google.inject.Inject
import model.mercure.daos.token.{ Token, TokenDAO }
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 09/06/16.
 */
class TokenServiceImpl @Inject() (val tokenDAO: TokenDAO) extends TokenService {

  def create(entity: String, tokenDoc: Token): Future[WriteResult] = tokenDAO.update(BSONDocument("entity" -> entity), tokenDoc, true)

  def retrieve(id: String): Future[Option[Token]] = tokenDAO.findById(id)

  def consume(id: String): Future[WriteResult] = tokenDAO.remove(BSONDocument("id" -> id))
}
