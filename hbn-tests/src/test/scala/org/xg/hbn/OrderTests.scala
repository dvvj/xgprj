package org.xg.hbn

import org.xg.hbn.PlaceOrderTests.uid1
import org.xg.hbn.utils.HbnUtils

object OrderTests extends App {

  import HbnDbOpsImpl._
  import TestUtils._

  val uid1 = "customer1"

  val t1 = testHbnOps.allCustomers
  println(t1.length)

  val orders1 = testHbnOps.ordersOf(uid1)
  println(orders1.length)

  var orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 6)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 5)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 30)
  println(orders2.length)

  orders2 = testHbnOps.ordersOf_CreationTimeWithin(uid1, 18)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_CreatedThisMonth(uid1)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_CreatedLastMonth(uid1)
  println(orders2.length)
  orders2 = testHbnOps.ordersOf_Unpaid(uid1)
  println(orders2.length)

//  orders1.foreach { o =>
//    testHbnOps.updateOrder(
//      o.id,
//      2000
//    )
//  }

  var orders = testHbnOps.ordersOfCustomers(
    Array("customer1", "customer2")
  )
  println(orders.length)

  orders2 = testHbnOps.ordersOfCustomers_Unpaid(
    Array("customer1", "customer2")
  )
  println(orders2.length)


  HbnUtils.shutdownTest()
}
