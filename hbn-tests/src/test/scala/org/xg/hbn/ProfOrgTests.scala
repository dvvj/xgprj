package org.xg.hbn

object ProfOrgTests extends App {

  import HbnDbOpsImpl._

  val poMap = testHbnOps.profOrgAgentMap

  println(poMap)

  val orgId1 = "prof_org_agent1"
  val org1Profs = testHbnOps.profsOf(orgId1)

  println(org1Profs)
}
