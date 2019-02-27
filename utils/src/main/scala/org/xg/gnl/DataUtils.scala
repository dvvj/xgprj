package org.xg.gnl

import java.sql.Timestamp
import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

object DataUtils {

  val UTC:ZoneId = ZoneId.of("UTC")
  def utcTimeNow:ZonedDateTime = ZonedDateTime.now(UTC)

  private val EmptyDateString = ""
  def zonedDateTime2Str(zdt:ZonedDateTime):String = {
    if (zdt == null) EmptyDateString
    else
      zdt.toLocalDateTime.format(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
      )
  }
  def dateStr(zdt:ZonedDateTime):String = {
    if (zdt == null) EmptyDateString
    else
      zdt.toLocalDateTime.format(
        DateTimeFormatter.ISO_DATE
      )
  }
  def timestamp2Zone(ts:Timestamp):ZonedDateTime = {
    ts.toLocalDateTime.atZone(UTC)
  }

  def timestampNow:Timestamp = Timestamp.valueOf(utcTimeNow.toLocalDateTime)
}
