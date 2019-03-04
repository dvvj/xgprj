package org.xg.hbn

object PayOrderTests extends App {

  import HbnDbOpsImpl._

  val uid = "customer1"

  val orders = hbnOps.ordersOf(uid)

  orders.foreach { o =>
    hbnOps.setOrderPayTime(o.id)
  }

  val orders2 = hbnOps.ordersOf(uid)

  println(orders2)

}
