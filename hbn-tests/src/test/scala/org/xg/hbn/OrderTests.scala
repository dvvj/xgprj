package org.xg.hbn

import org.xg.hbn.PlaceOrderTests.uid1
import org.xg.hbn.utils.HbnUtils

object OrderTests extends App {

  import HbnDbOpsImpl._
  import TestUtils._

  val uid1 = "customer1"

  val orders1 = testHbnOps.ordersOf(uid1)

  var orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 6)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 5)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 30)
  println(orders2.length)

//  orders1.foreach { o =>
//    testHbnOps.updateOrder(
//      o.id,
//      2000
//    )
//  }

  val orders = testHbnOps.ordersOfCustomers(
    Array("customer1", "customer2")
  )
  println(orders.length)

  HbnUtils.shutdownTest()
}
