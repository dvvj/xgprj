package org.xg.hbn

object ProfOrgTests extends App {

  import HbnDbOpsImpl._

  val poMap = testHbnOps.profOrgAgentMap

  println(poMap)

  val orgAgentId1 = "prof_org_agent1"
  val org1Profs = testHbnOps.profsOf(orgAgentId1)

  println(org1Profs)
}
