package org.xg.dbUtils

import org.xg.auth.AuthHelpers
//import org.xg.dbUtils.InsertCustomersUtil.testRefUid

object InsertMedProfsUtil {

  val profId1 = "prof1"
  val profId2 = "prof2"
  val profId3 = "prof3"

  val profOrgId1 = "prof_org1"
  val profOrgId2 = "prof_org2"

  val prof2OrgMap:Map[String, String] = Map(
    profId1 -> profOrgId1,
    profId2 -> profOrgId1,
    profId3 -> profOrgId2
  )

  val testProfs = Array(
    Array(profId1, "李卫东", "3302030222313322", "13792929133") -> "123",
    Array(profId2, "张鑫", "33020555555555555", "1375555555") -> "456",
    Array(profId3, "陈伟达", "3302036666666666", "1376666666") -> "abcdef",
  )

  def main(args:Array[String]):Unit = {
    val insertStatementTemplate =
      "INSERT INTO med_profs (prof_id, name, idcard_no, mobile, org_id, pass_hash)" +
        "  VALUES (%s);"

    val insertStatements = testProfs.map { p =>
      val (line, pass) = p
      val passHash = AuthHelpers.sha512(pass)
      val hashStr = AuthHelpers.hash2Str(passHash)
      val profId = line(0)
      val lineWithOrgId = line ++ Array(prof2OrgMap(profId))
      val params = lineWithOrgId.mkString("'", "','", "'") + s",X'$hashStr'"
      insertStatementTemplate.format(params)
    }

    println(insertStatements.mkString("\n"))
  }



}
