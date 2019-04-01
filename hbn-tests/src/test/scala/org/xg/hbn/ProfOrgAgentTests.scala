package org.xg.hbn

import org.xg.user.UserType

object ProfOrgAgentTests extends App {

  import HbnDbOpsImpl._

  val poMap = testHbnOps.profOrgAgentMap

  println(poMap)

  val orgAgentId1 = UserType.MedProfOrgAgent.genUid("agent1")
  val org1Profs = testHbnOps.profsOf(orgAgentId1)

  println(org1Profs)
}
