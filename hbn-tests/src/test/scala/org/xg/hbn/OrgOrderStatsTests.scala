package org.xg.hbn

import org.xg.hbn.HbnDbOpsImpl.testHbnOps
import org.xg.hbn.TestUtils.prof2OrgAgentMap

object OrgOrderStatsTests extends App {
  val uid1 = "customer1"
  import TestUtils._
  val profId = customer2ProfMap(uid1)
  val orgAgentId = prof2OrgAgentMap(profId)
//  testHbnOps.placeOrder(
//    uid1, profId, orgId, 1, 1.0, 1400.0
//  )

  val stats = testHbnOps.getOrderStat4OrgAgent(orgAgentId)

  println(stats)
}
