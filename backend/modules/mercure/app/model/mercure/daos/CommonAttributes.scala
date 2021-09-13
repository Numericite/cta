package model.mercure.daos

import reactivemongo.api.commands.{ UpdateWriteResult, WriteResult }
import reactivemongo.bson.BSONDocument

import scala.concurrent.Future

/**
 * Created by madalien on 23/05/16.
 */
trait CommonAttributes {

  def update(selector: BSONDocument, modifier: BSONDocument, upsertIn: Boolean = false): Future[UpdateWriteResult]
  def remove(selector: BSONDocument): Future[WriteResult]
}
