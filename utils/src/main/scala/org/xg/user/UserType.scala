package org.xg.user

import UserData._
object UserType extends Enumeration {
  type UserType = Value

  case class Val(code:Int, uidPfx:Option[String]) extends super.Val {
    def genUid(idPart:String):String = uidPfx.get + idPart.trim
  }

  implicit def val2UTValue(x:Value):Val = x.asInstanceOf[Val]
  val Customer = Val(0, Option(uidPrefix("c")))
  val MedProf = Val(1, Option(uidPrefix("mp")))
  val MedProfOrgAgent = Val(2, Option(uidPrefix("amp")))
  val MedProfOrg = Val(3, Option(uidPrefix("omp")))
  val Agency = Val(4, Option(uidPrefix("a")))
  val NotUsed = Val(-1, None)

}
