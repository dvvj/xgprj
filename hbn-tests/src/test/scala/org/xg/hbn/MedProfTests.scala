package org.xg.hbn

import org.xg.hbn.utils.HbnUtils

object MedProfTests extends App {

  import HbnDbOpsImpl._


  hbnOps.addNewMedProf(
    "medprof2", "微信", "456", "232312l2222", "322233211"
  )

  val m = hbnOps.getMedProfPassMap
  println(m.size)

  HbnUtils.shutdown()

}
