package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object HbnDbOpsImplTests extends App {


  import HbnDbOpsImpl._

  hbnOps.getUserPassMap

  val prods = hbnOps.allProducts
  println(prods.length)
  val customers = hbnOps.allCustomers
  println(customers.length)
  val uid = "customer1"
  val ordersOfC1 = hbnOps.ordersOf(uid)
  println(ordersOfC1.length)

//  val newOrderId = opsInstance.placeOrder(
//    uid,
//    1,
//    2.5
//  )
//  println(s"New order id: $newOrderId")
//  val t = opsInstance.ordersOf(uid)
//  println(t.length)
  HbnUtils.shutdown()
}
