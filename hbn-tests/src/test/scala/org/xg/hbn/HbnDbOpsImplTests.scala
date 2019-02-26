package org.xg.hbn

object HbnDbOpsImplTests extends App {


  import HbnDbOpsImpl._

  val prods = opsInstance.allProducts
  println(prods.length)
  val customers = opsInstance.allCustomers
  println(customers.length)
}
