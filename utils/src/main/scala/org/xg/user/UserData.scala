package org.xg.user

import org.xg.user.UserType.UserType

object UserData {
  private val UnderScoreUnicode:String = "＿"
  def uidPrefix(ut:String):String = s"$ut$UnderScoreUnicode"

  private def userTypeMap:Map[Int, UserType] = UserType.values.map(v => v.code -> v).toMap
  //def typeFromCode(userTypeCode:Int):UserType = userTypeMap(userTypeCode)

  def genUid(userTypeCode:Int, idPart:String):String = {
    if (userTypeMap.contains(userTypeCode))
      userTypeMap(userTypeCode).genUid(idPart)
    else
      throw new IllegalArgumentException(s"type code $userTypeCode")
  }

}
