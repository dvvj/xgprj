package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object MedProfTests extends App {

  import HbnDbOpsImpl._

  private def testRefId():Unit = {
    val profId = "profxxx"
    testHbnOps.addNewMedProf(
      profId, "微信", "456", "232312l2222", "322233211", "prof_org_agent2"
    )

    val m = testHbnOps.getMedProfPassMap
    println(m.size)

    val i = 10
    val params = Array(
      s"_customer$i",
      s"_name$i",
      s"_pass$i",
      s"_idCardNo$i",
      s"_mobile$i",
      s"_postalAddr$i",
      profId,
      "1991-10-02"
    )
    testHbnOps.addNewCustomer(
      params(0),
      params(1),
      params(2),
      params(3),
      params(4),
      params(5),
      params(6),
      params(7)
    )

    val customers = testHbnOps.customersOf(profId)
    println(customers.length)
  }

  private def showAllMedProfs():Unit = {
    val medProfs = testHbnOps.allMedProfs
    println(medProfs.mkString("\n"))
  }

  private def testCustomersRefedBy(refUid:String):Unit = {
    val medProfs = testHbnOps.customersRefedBy(refUid)
    println(s"Customers refed by [$refUid]:\n${medProfs.mkString("\n")}")
  }

  showAllMedProfs()
  testCustomersRefedBy("prof1")
  testCustomersRefedBy("prof2")

  HbnUtils.shutdownTest()

}
