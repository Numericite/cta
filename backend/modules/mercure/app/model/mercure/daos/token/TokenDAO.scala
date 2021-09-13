package model.mercure.daos.token

import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 09/06/16.
 */

trait TokenDAO {

  def insert(doc: Token): Future[WriteResult]
  def remove(selector: BSONDocument): Future[WriteResult]
  def findById(id: String): Future[Option[Token]]
  def update(selector: BSONDocument, docModifier: Token, upsertIn: Boolean = false): Future[UpdateWriteResult]
}
