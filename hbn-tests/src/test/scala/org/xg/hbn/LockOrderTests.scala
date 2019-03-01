package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object LockOrderTests extends App {

  import HbnDbOpsImpl._

  def updateOrders(uid:String, newQty:Double):Unit = {

    val orders1 = hbnOps.ordersOf(uid)

    orders1.foreach { o =>
      val res = hbnOps.updateOrder(
        o.id,
        newQty
      )
      println(s"update result: $res")
    }
  }

  updateOrders("customer1", 111)

  val orders = hbnOps.lockOrders

  updateOrders("customer1", 112)


  HbnUtils.shutdown()


}
