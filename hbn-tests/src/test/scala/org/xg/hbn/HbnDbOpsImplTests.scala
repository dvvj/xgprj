package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object HbnDbOpsImplTests extends App {


  import HbnDbOpsImpl._

  opsInstance.getUserPassMap

  val prods = opsInstance.allProducts
  println(prods.length)
  val customers = opsInstance.allCustomers
  println(customers.length)
  val uid = "customer1"
  val ordersOfC1 = opsInstance.ordersOf(uid)
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
