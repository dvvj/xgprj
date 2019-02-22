package org.xg.gnl

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

object DataUtils {

  def utcTimeNow:ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"))

  def zonedDateTime2Str(zdt:ZonedDateTime):String = {
    zdt.toLocalDateTime.format(
      DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )
  }

}
