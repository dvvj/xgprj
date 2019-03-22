package org.xg.dbUtils

import org.xg.auth.AuthHelpers
//import org.xg.dbUtils.InsertCustomersUtil.testRefUid

object InsertMedProfsUtil extends App {
  val testProfs = Array(
    Array("prof1", "李卫东", "3302030222313322", "13792929133", "prof_org1") -> "123",
    Array("prof2", "张鑫", "33020555555555555", "1375555555", "prof_org1") -> "456",
    Array("prof3", "陈伟达", "3302036666666666", "1376666666", "prof_org2") -> "abcdef",
  )

  val insertStatementTemplate =
    "INSERT INTO med_profs (prof_id, name, idcard_no, mobile, org_id, pass_hash)" +
      "  VALUES (%s);"

  val insertStatements = testProfs.map { p =>
    val (line, pass) = p
    val passHash = AuthHelpers.sha512(pass)
    val hashStr = AuthHelpers.hash2Str(passHash)
    val params = line.mkString("'", "','", "'") + s",X'$hashStr'"
    insertStatementTemplate.format(params)
  }

  println(insertStatements.mkString("\n"))

}
