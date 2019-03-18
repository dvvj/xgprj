package org.xg.dbUtils

import org.xg.auth.AuthHelpers

object InsertProfOrgUtil extends App {

  val testProfOrgs = Array(
    Array("prof_org1", "医药公司1", "医药公司1 info", "org_no1", "13792929133", "2018-03-04T16:00:28.179") -> "123",
    Array("prof_org2", "医药公司2", "医药公司2 info", "org_no2", "1375555555", "2019-08-04T16:00:28.179") -> "456",
  )

  val insertStatementTemplate =
    "INSERT INTO prof_orgs (org_id, name, info, org_no, phone, join_date, pass_hash)" +
      "  VALUES (%s);"

  val insertStatements = testProfOrgs.map { p =>
    val (line, pass) = p
    val passHash = AuthHelpers.sha512(pass)
    val hashStr = AuthHelpers.hash2Str(passHash)
    val params = line.mkString("'", "','", "'") + s",X'$hashStr'"
    insertStatementTemplate.format(params)
  }

  println(insertStatements.mkString("\n"))

}
