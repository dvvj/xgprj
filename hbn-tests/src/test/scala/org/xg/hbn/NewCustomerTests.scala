package org.xg.hbn

import org.xg.gnl.DataUtils
import org.xg.hbn.utils.HbnUtils

import scala.concurrent.{Await, Future}
import scala.util.Random

object NewCustomerTests extends App {

  import HbnDbOpsImpl._

  def customerInfo(i:Int): Array[String] = {
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

  def addNewCustomer(i:Int): Unit = {
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

//  val c = 10
//
//  (0 until c).foreach { idx =>
//    addNewCustomer(idx)
//  }

  import org.xg.tests.ConcurrencyHelpers._

  def idx2Task0(idx:Int):Task0 = () => {
    val rand = new Random().nextInt(2000)
    Thread.sleep(rand)
    addNewCustomer(idx)
  }

  val idx1 = Range(10, 20, 2)
  val schedule1 = idx1.map(idx2Task0)
  val idx2 = Range(11, 20, 2)
  val schedule2 = idx2.map(idx2Task0)

  val f1 = runSchedule(schedule1)
  val f2 = runSchedule(schedule2)
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  val f = Future.sequence(Seq(f1, f2))
  Await.result(f, 60 seconds)

  val allCustomers = hbnOps.allCustomers
  println(s"All customer #: ${allCustomers.length}")

  HbnUtils.shutdown()
}
