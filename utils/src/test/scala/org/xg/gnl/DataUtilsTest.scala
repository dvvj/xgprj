package org.xg.gnl

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test

class DataUtilsTest extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val zonedDateTime2StrTestData = Table(
    ("zdt", "expRes"),
    (
      ZonedDateTime.of(
        LocalDateTime.of(2001, 2, 20, 15, 24, 35),
        ZoneId.systemDefault()
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
}
