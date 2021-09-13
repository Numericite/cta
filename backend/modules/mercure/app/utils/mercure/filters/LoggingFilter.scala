package utils.mercure.filters

import javax.inject.Inject
import akka.util.ByteString
import play.api.Logger
import play.api.libs.streams.Accumulator
import play.api.mvc._
import scala.concurrent.ExecutionContext
/**
 * Created by madalien on 10/06/16.
 */

class LoggingFilter @Inject() (implicit ec: ExecutionContext) extends EssentialFilter {

  val logger = Logger("MR." + this.getClass.getSimpleName)
  def apply(nextFilter: EssentialAction) = new EssentialAction {
    def apply(requestHeader: RequestHeader) = {

      val startTime = System.currentTimeMillis

      val accumulator: Accumulator[ByteString, Result] = nextFilter(requestHeader)

      accumulator.map { result =>

        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime

        logger.debug(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
        result.withHeaders("Request-Time" -> requestTime.toString)

      }
    }
  }
}
