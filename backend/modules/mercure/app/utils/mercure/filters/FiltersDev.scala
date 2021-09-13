package utils.mercure.filters

/**
 * Created by madalien on 18/05/16.
 */
import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.cors.CORSFilter
import play.filters.gzip.GzipFilter

class FiltersDev @Inject() (
    gzip: GzipFilter,
    log: LoggingFilter,
    corsFilter: CORSFilter
) extends HttpFilters {
  def filters = Seq(corsFilter, log, gzip)
}

