package org.xg.hbn

import org.xg.hbn.HbnDbOpsImpl.testHbnOps
import org.xg.user.UserType

object CancelOrderTests extends App {

  val uid1 = UserType.Customer.genUid("o1a1p1_customer1")
  val unpaidOrders = testHbnOps.ordersOf_Unpaid(uid1)
  println(unpaidOrders.length)

  var cancelledOrders = testHbnOps.ordersOf_Cancelled(uid1)
  println(cancelledOrders.length)

  unpaidOrders.foreach { o => testHbnOps.cancelOrder(o.id) }

  cancelledOrders = testHbnOps.ordersOf_Cancelled(uid1)
  println(cancelledOrders.length)

}
