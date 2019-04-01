package org.xg.user

object UserType extends Enumeration {
  type UserType = Value

  case class Val(code:Int) extends super.Val

  implicit def val2UTValue(x:Value):Val = x.asInstanceOf[Val]
  val Customer = Val(0)
  val Professional = Val(1)
  val MedProfOrgAgent = Val(2)
  val MedProfOrg = Val(3)
  val Agency = Val(4)
  val NotUsed = Val(-1)

}
