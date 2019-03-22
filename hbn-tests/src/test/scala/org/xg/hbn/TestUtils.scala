package org.xg.hbn

import java.sql.Timestamp
import java.time.ZonedDateTime

import org.xg.gnl.DataUtils
import org.xg.hbn.HbnDbOpsImpl.testHbnOps
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
    testHbnOps.addNewCustomer(
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

  private def randSleep(rng:Int):Unit = {
    val seed = DataUtils.timestampNow.getTime
    val r = new Random(seed).nextInt(rng)
    Thread.sleep(r)
  }
  private def idx2Task0(idx:Int):Task0 = () => {
    randSleep(2000)
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

  val profId1 = "prof1"
  val profId2 = "prof2"
  val profId3 = "prof3"

  val profOrgId1 = "prof_org1"
  val profOrgId2 = "prof_org2"

  val prof2OrgMap:Map[String, String] = Map(
    profId1 -> profOrgId1,
    profId2 -> profOrgId1,
    profId3 -> profOrgId2
  )

  val customer1 = "customer1"
  val customer2 = "customer2"
  val customer3 = "customer3"
  val customer4 = "customer4"

  val customer2ProfMap = Map(
    customer1 -> profId1,
    customer2 -> profId1,
    customer3 -> profId1,
    customer4 -> profId2
  )

//  private def orderData2Task0(d:Order4Test):Task0 = () => {
//    randSleep(1000)
//    val profId =
//    testHbnOps.placeOrder(d.uid, d.prodId, d.qty, d.actualPrice)
//  }
//
//  case class Order4Test(
//                       uid:String,
//                       prodId:Int,
//                       qty:Double,
//                       actualPrice:Double
//                       )
//  def placeOrderSchedule(
//    orderData:Iterable[Order4Test]
//  ):Schedule0 = {
//    orderData.map(orderData2Task0)
//  }
//
//  type TestSchedule_InitSetup = () => Unit
//  type TestSchedule_Run = () => Unit
//  type TestSchedule_Verification = () => Unit
//  def testSchedules(
//                   initSetup:TestSchedule_InitSetup,
//                   runSchedule:TestSchedule_Run,
//                   verify:TestSchedule_Verification
//                   ):Unit = {
//    initSetup()
//
//    runSchedule()
//
//    verify()
//  }
}
