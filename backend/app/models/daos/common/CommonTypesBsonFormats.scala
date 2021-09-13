package models.daos.common

import java.util.UUID

import org.joda.time.DateTime
import reactivemongo.bson.{ BSONHandler, BSONLong, BSONReader, BSONString, BSONWriter }

/**
 * Created by Clément Lelong on 2/6/17.
 */
object CommonTypesBsonFormats {
  /*

  implicit def UUIDReader[V](implicit vr: BSONReader[BSONString, UUID]): BSONReader[BSONString, UUID] = new BSONReader[BSONString, UUID] {
    def read(bson: BSONString): UUID = {
      UUID.fromString(bson.value)
    }
  }

  implicit def UUIDWriter[V](implicit vw: BSONWriter[UUID, BSONString]): BSONWriter[UUID, BSONString] = new BSONWriter[UUID, BSONString] {
    def write(uUID: UUID): BSONString = {
      BSONString(uUID.toString)
    }
  }
*/

  implicit object UuidBsonHandler extends BSONHandler[BSONString, UUID] {
    def read(bson: BSONString): UUID = UUID.fromString(bson.value)
    def write(uUID: java.util.UUID): BSONString = BSONString(uUID.toString)
  }

  implicit object DateTimeBsonHandler extends BSONHandler[BSONLong, DateTime] {
    def read(bson: BSONLong): DateTime = new DateTime(bson.value)
    def write(date: DateTime): BSONLong = BSONLong(date.getMillis)
  }

}

