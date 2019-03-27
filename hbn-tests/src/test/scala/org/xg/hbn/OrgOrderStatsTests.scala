package org.xg.hbn

import org.xg.hbn.HbnDbOpsImpl.testHbnOps
import org.xg.hbn.TestUtils.prof2OrgMap

object OrgOrderStatsTests extends App {
  val uid1 = "customer1"
  import TestUtils._
  val profId = customer2ProfMap(uid1)
  val orgId = prof2OrgMap(profId)
//  testHbnOps.placeOrder(
//    uid1, profId, orgId, 1, 1.0, 1400.0
//  )

  val stats = testHbnOps.getOrderStat4Org(orgId)

  println(stats)
}
