package org.xg.hbn

import java.sql.Timestamp
import java.time.ZonedDateTime

import org.xg.gnl.DataUtils
import org.xg.hbn.HbnDbOpsImpl.hbnOps
import org.xg.hbn.NewCustomerTests.{schedule1, schedule2, schedule3, schedule4}
import org.xg.tests.ConcurrencyHelpers.{Schedule0, Task0}

import scala.concurrent.{Await, Future}
import scala.util.Random

object TestUtils {
  private def customerInfo(i:Int): Array[String] = {
    val zdt = DataUtils.utcTimeNow.plusDays(i)
    val dtStr = DataUtils.dateStr(zdt)
    Array(
      s"_customer$i",
      s"_name$i",
      s"_pass$i",
      s"_idCardNo$i",
      s"_mobile$i",
      s"_postalAddr$i",
      s"_ref_uid$i",
      dtStr
    )
  }

  private def addNewCustomer(i:Int): Unit = {
    val params:Array[String] = customerInfo(i)
    hbnOps.addNewCustomer(
      params(0),
      params(1),
      params(2),
      params(3),
      params(4),
      params(5),
      params(6),
      params(7)
    )
  }

  private def idx2Task0(idx:Int):Task0 = () => {
    val seed = DataUtils.timestampNow.getTime
    val rand = new Random(seed).nextInt(2000)
    Thread.sleep(rand)
    addNewCustomer(idx)
  }

  def newCustomerSchedule(idxRange:Range):Schedule0 = idxRange.map(idx2Task0)

  def runSchedulesAndWait(schedules:Seq[Schedule0], waitSecs:Int):Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    import org.xg.tests.ConcurrencyHelpers._
    val f = Future.sequence(schedules.map(runSchedule))
    Await.result(f, waitSecs seconds)
  }
}
