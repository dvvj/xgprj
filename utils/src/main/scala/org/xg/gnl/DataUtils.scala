package org.xg.gnl

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

object DataUtils {

  val UTC:ZoneId = ZoneId.of("UTC")
  def utcTimeNow:ZonedDateTime = ZonedDateTime.now(UTC)

  private val EmptyDateString = ""
  def zonedDateTime2Str(zdt:ZonedDateTime):String = {
    if (zdt == null) null
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

  def zonedDateTime2Ms(zdt:ZonedDateTime):Long = {
    zdt.toInstant.toEpochMilli
  }

  def timestampNow:Timestamp = Timestamp.valueOf(utcTimeNow.toLocalDateTime)

  def noneIfNull(s:String):Option[String] = if (s == null) None else Option(s)

  def utcTimeFromStr(s:String):ZonedDateTime = {
    LocalDateTime.parse(s).atZone(UTC)
  }
  def utcTimeFromStrOpt(s:String):Option[ZonedDateTime] = {
    if (s == null || s.isEmpty)
      None
    else
      Option(utcTimeFromStr(s))
  }
}
