package org.xg.gnl

import java.sql.Timestamp
import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

object DataUtils {

  val UTC:ZoneId = ZoneId.of("UTC")
  def utcTimeNow:ZonedDateTime = ZonedDateTime.now(UTC)

  def zonedDateTime2Str(zdt:ZonedDateTime):String = {
    zdt.toLocalDateTime.format(
      DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )
  }

  def timestamp2Zone(ts:Timestamp):ZonedDateTime = {
    ts.toLocalDateTime.atZone(UTC)
  }
}
