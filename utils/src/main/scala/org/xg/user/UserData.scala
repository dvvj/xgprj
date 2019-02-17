package org.xg.user

object UserData {

  object UserType extends Enumeration {
    type UserType = Value
    val Customer, Professional, PharmaCompany, Agency = Value
  }
}
