package org.xg.hbn

import org.xg.hbn.HbnDbOpsImpl.testHbnOps
import org.xg.user.UserType

object CustomerProfilesTest extends App {

  val uid = UserType.Customer.genUid("o1a1p1_customer1")
  val res = testHbnOps.getCustomerProfiles(uid)

  println(res.length)

}
