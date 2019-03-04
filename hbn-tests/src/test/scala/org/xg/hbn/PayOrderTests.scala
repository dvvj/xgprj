package org.xg.hbn

object PayOrderTests extends App {

  import HbnDbOpsImpl._

  val uid = "customer1"

  val orders = testHbnOps.ordersOf(uid)

  orders.foreach { o =>
    testHbnOps.setOrderPayTime(o.id)
  }

  val orders2 = testHbnOps.ordersOf(uid)

  println(orders2)

}
