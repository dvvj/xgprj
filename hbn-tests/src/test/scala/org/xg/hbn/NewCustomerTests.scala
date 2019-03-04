package org.xg.hbn

import org.xg.gnl.DataUtils
import org.xg.hbn.utils.HbnUtils

import scala.concurrent.{Await, Future}
import scala.util.Random

object NewCustomerTests extends App {

  import HbnDbOpsImpl._

//  val c = 10
//
//  (0 until c).foreach { idx =>
//    addNewCustomer(idx)
//  }

  import org.xg.tests.ConcurrencyHelpers._
  import TestUtils._


  val schedule1 = newCustomerSchedule(
    Range(100, 200, 4)
  )
  val schedule2 = newCustomerSchedule(
    Range(101, 200, 4)
  )
  val schedule3 = newCustomerSchedule(
    Range(102, 200, 4)
  )
  val schedule4 = newCustomerSchedule(
    Range(103, 200, 4)
  )

  runSchedulesAndWait(
    Seq(schedule1, schedule2, schedule3, schedule4),
    600
  )

  val allCustomers = testHbnOps.allCustomers
  println(s"All customer #: ${allCustomers.length}")

  HbnUtils.shutdownTest()
}
