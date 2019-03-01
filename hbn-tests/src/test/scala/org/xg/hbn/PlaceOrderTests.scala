package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object PlaceOrderTests extends App {

  import HbnDbOpsImpl._
  import TestUtils._

  val uid1 = "customer1"
  val uid2 = "customer2"
  val uid3 = "customer3"

  val uid1Orders = placeOrderSchedule(
    Iterable(
      (uid1, 1, 2.0),
      (uid1, 2, 4.0),
      (uid1, 3, 6.0),
      (uid1, 4, 8.0)
    )
  )

  val uid2Orders = placeOrderSchedule(
    Iterable(
      (uid2, 1, 2.0),
      (uid2, 2, 4.0),
      (uid2, 3, 6.0),
      (uid2, 4, 8.0)
    )
  )

  val uid3Orders = placeOrderSchedule(
    Iterable(
      (uid3, 1, 3.0),
      (uid3, 2, 6.0),
      (uid3, 3, 9.0),
      (uid3, 4, 12.0)
    )
  )

//  runSchedulesAndWait(
//    Seq(uid1Orders, uid2Orders, uid3Orders),
//    60
//  )

  val orders1 = hbnOps.ordersOf(uid1)
  println(orders1.size)
  val orders2 = hbnOps.ordersOf(uid2)
  println(orders2.size)
  val orders3 = hbnOps.ordersOf(uid3)
  println(orders3.size)

  val histories = hbnOps.testAllOrderHistory
  println(histories.length)

  HbnUtils.shutdown()

}
