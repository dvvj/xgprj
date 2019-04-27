package org.xg.gnl

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

object DataUtils {

  val UTC:ZoneId = ZoneId.of("UTC")
  def utcTimeNow:ZonedDateTime = ZonedDateTime.now(UTC)
  def utcTimeNowStr:String = zonedDateTime2Str(utcTimeNow)

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

  def roundMoney(price:Double):Double = math.round(price * 100) / 100.0

  def chartMaxY(valueMax:Double, roundTo:Int):Double = ((valueMax-1).toInt/roundTo + 1) * roundTo

  def maskStr(str:String, startC:Int, endC:Int):String = {
    if (startC + endC >= str.length)
      str
    else {
      val p1Len = math.min(startC, str.length)
      val p1 = str.substring(0, p1Len)
      val p2Len = math.min(endC, str.length)
      val p2 = str.substring(str.length-p2Len, str.length)

      val pm = (0 until str.length-p1Len-p2Len).map(_ => '*').mkString
      p1 + pm + p2
    }
  }

  def maskStrStart(str:String, startC:Int):String = maskStr(str, startC, 0)
  def maskStrEnd(str:String, endC:Int):String = maskStr(str, 0, endC)
  def maskStrAll(str:String):String = maskStr(str, 0, 0)
}
