package org.xg.srcTests

import org.xg.auth.SvcHelpers
import org.xg.busiLogic.OrderStatusLogics
import org.xg.dbModels.MOrder
import org.xg.gnl.GlobalCfg
import org.xg.user.UserType

object CancelOrderTests extends App {

  val cfg = GlobalCfg.localTestCfg

  val (uid, pass) = UserType.Customer.genUid("o1a1p1_customer1") -> "123"
  val resp = SvcHelpers.authReq(cfg.authCustomerURL, uid, pass)

  if (resp.success)
    println(s"Success! Token: ${resp.token}")
  else
    println(s"Failed! Message: ${resp.msg}")

  val allOrders = SvcHelpers.getDecArray(
    cfg.currOrdersURL,
    resp.token,
    MOrder.fromJsons
  )
  val unpaidOrders = allOrders.filter(OrderStatusLogics.isUnpaid)

  unpaidOrders.foreach( o => SvcHelpers.post(cfg.cancelOrderURL, resp.token, o.id.toString) )

}
