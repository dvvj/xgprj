package org.xg.hbn

import org.xg.hbn.HbnDbOpsImpl.testHbnOps

object AuditTests extends App {


  //testHbnOps.svcAudit("test1", 10, "uid1")

  val audits = testHbnOps.allSvcAudit

  println(audits.mkString("\n\n"))

}
