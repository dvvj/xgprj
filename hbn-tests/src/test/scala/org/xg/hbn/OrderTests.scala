package org.xg.hbn

import org.xg.hbn.PlaceOrderTests.uid1
import org.xg.hbn.utils.HbnUtils

object OrderTests extends App {

  import HbnDbOpsImpl._
  import TestUtils._

  val uid1 = "customer1"

  val orders1 = testHbnOps.ordersOf(uid1)

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
