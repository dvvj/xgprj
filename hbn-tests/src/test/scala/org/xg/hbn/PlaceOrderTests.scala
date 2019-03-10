package org.xg.hbn

import org.xg.dbModels.MOrder
import org.xg.hbn.utils.HbnUtils

object PlaceOrderTests extends App {

  import HbnDbOpsImpl._
  import TestUtils._

  val uid1 = "customer1"
  val uid2 = "customer2"
  val uid3 = "customer3"

  val orderTestData = Seq(
    Map(
      uid1 -> Array(
        Order4Test(uid1, 1, 2.0),
        Order4Test(uid1, 2, 4.0),
        Order4Test(uid1, 3, 6.0),
        Order4Test(uid1, 4, 8.0)
      ),
      uid2 -> Array(
        Order4Test(uid2, 1, 2.0),
        Order4Test(uid2, 2, 4.0),
        Order4Test(uid2, 3, 6.0),
        Order4Test(uid2, 4, 8.0)
      ),
      uid3 -> Array(
        Order4Test(uid3, 1, 3.0),
        Order4Test(uid3, 2, 6.0),
        Order4Test(uid3, 3, 9.0),
        Order4Test(uid3, 4, 12.0)
      )
    ),
    Map(
      uid1 -> Array(
        Order4Test(uid1, 1, 12.0),
        Order4Test(uid1, 2, 14.0),
        Order4Test(uid1, 3, 16.0),
        Order4Test(uid1, 4, 18.0)
      ),
      uid2 -> Array(
        Order4Test(uid2, 1, 12.0),
        Order4Test(uid2, 2, 14.0),
        Order4Test(uid2, 3, 16.0),
        Order4Test(uid2, 4, 18.0)
      )
    ),
    Map(
      uid1 -> Array(
        Order4Test(uid1, 1, 22.0),
        Order4Test(uid1, 2, 24.0),
        Order4Test(uid1, 3, 26.0),
        Order4Test(uid1, 4, 28.0)
      ),
      uid3 -> Array(
        Order4Test(uid3, 1, 22.0),
        Order4Test(uid3, 2, 24.0),
        Order4Test(uid3, 3, 26.0),
        Order4Test(uid3, 4, 28.0)
      )
    )
  )

  def runTest(testData:Map[String, Array[Order4Test]]):Unit = {
    import collection.mutable

    val allUsers = testData.keySet.toSeq
    var expResultOrders = mutable.Map[String, Array[Order4Test]]()

    def ordersOf(users:Iterable[String]):Map[String, Array[Order4Test]] = {
      users.map { uid =>
        val orders = testHbnOps.ordersOf(uid)
        uid -> orders.map(o => Order4Test(o.uid, o.productId, o.qty))
      }.toMap
    }

    testSchedules(
      () => {
        expResultOrders = mutable.Map[String, Array[Order4Test]]()
        val existingOrders = ordersOf(allUsers)
        //expResultOrders ++= existingOrders
        allUsers.foreach { uid =>
          val existing = existingOrders.getOrElse(uid, Array())
          val added = testData.getOrElse(uid, Array())
          expResultOrders += uid -> (existing ++ added)
        }
      },
      () => {
        val s1 = placeOrderSchedule(testData(uid1))
        runSchedulesAndWait(
          allUsers.map(uid => placeOrderSchedule(testData(uid))),
          60
        )
      },
      () => {
        val currOrders = ordersOf(allUsers)
        if (checkOrders(currOrders, expResultOrders.toMap)) {
          println(s"Test passed")
        }
        else {
          println(s"Test Failed")
          throw new RuntimeException("Test failed!")
        }
      }
    )
  }


  def checkOrders(m1:Map[String, Array[Order4Test]], m2:Map[String, Array[Order4Test]]):Boolean = {
    val usrs1 = m1.keySet
    val usrs2 = m2.keySet
    if (usrs1 != usrs2) {
      println(s"Different user sets: [$usrs1] vs [$usrs2]")
      false
    }
    else {
      usrs1.forall { usr =>
        val orders1 = m1(usr).toSeq.sortBy(o => o.prodId -> o.qty)
        val orders2 = m2(usr).toSeq.sortBy(o => o.prodId -> o.qty)
        println(s"Checking orders for [$usr] (${orders1.size}-${orders2.size}) ...")
        if (orders1 != orders2) {
          println(s"Orders for [$usr] differ")
          false
        }
        else
          true
      }
    }
  }

  orderTestData.foreach { orderTestData =>
    runTest(orderTestData)
    Thread.sleep(500)
  }

//
//  val orders1 = hbnOps.ordersOf(uid1)
//  println(orders1.length)
//  val orders2 = hbnOps.ordersOf(uid2)
//  println(orders2.length)
//  val orders3 = hbnOps.ordersOf(uid3)
//  println(orders3.length)

  val histories = testHbnOps.testAllOrderHistory
  println(histories.length)

  HbnUtils.shutdownTest()

}
