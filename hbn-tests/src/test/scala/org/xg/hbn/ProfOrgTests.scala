package org.xg.hbn

object ProfOrgTests extends App {

  import HbnDbOpsImpl._

  val poMap = testHbnOps.profOrgMap

  println(poMap)

  val orgId1 = "prof_org1"
  val org1Profs = testHbnOps.profsOf(orgId1)

  println(org1Profs)
}
