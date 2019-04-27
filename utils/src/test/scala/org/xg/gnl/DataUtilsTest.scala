package org.xg.gnl

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test

class DataUtilsTest extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val ldt = LocalDateTime.of(2001, 2, 20, 15, 24, 35)
  private val zonedDateTime2StrTestData = Table(
    ("zdt", "expRes"),
    (
      ZonedDateTime.of(
        ldt,
        DataUtils.UTC
      ),
      "2001-02-20T15:24:35"
    )
  )

  @Test
  def zonedDateTime2StrTest():Unit = {
    forAll(zonedDateTime2StrTestData) { (zdt, expRes) =>
      val res = DataUtils.zonedDateTime2Str(zdt)
      res shouldBe expRes
    }
  }

  private val timestamp2ZoneTestData = Table(
    ("ts", "expZdt"),
    (
      Timestamp.valueOf(ldt),
      ZonedDateTime.of(ldt, DataUtils.UTC)
    )
  )

  @Test
  def timestamp2ZoneTest():Unit = {
    forAll(timestamp2ZoneTestData) { (ts, expZdt) =>
      val res = DataUtils.timestamp2Zone(ts)
      res shouldBe expZdt
    }
  }

  private val dateStrTestData = Table(
    ("zdt", "expRes"),
    (
      ZonedDateTime.of(
        ldt,
        DataUtils.UTC
      ),
      "2001-02-20"
    )
  )
  @Test
  def dateStrTest():Unit = {
    forAll(dateStrTestData) { (zdt, expStr) =>
      val res = DataUtils.dateStr(zdt)
      res shouldBe expStr
    }
  }

  private val maskStrTestData = Table(
    ( "str", "startC", "endC", "exp" ),
    ( "abc", 1, 0, "a**"),
    ( "abc", 1, 1, "a*c"),
    ( "abc", 4, 0, "abc"),
    ( "abc", 0, 1, "**c"),
    ( "abc", 0, 4, "abc"),
    ( "abc", 2, 1, "abc"),
    ( "abc", 2, 2, "abc"),
    ( "abc", 1, 2, "abc")
  )
  @Test
  def maskStrTest():Unit = {
    forAll(maskStrTestData) { (str, startC, endC, exp) =>
      val res = DataUtils.maskStr(str, startC, endC)
      res shouldBe exp
    }
  }


  }
