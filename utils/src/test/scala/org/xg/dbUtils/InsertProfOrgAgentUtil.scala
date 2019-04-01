package org.xg.dbUtils

import org.xg.auth.AuthHelpers

object InsertProfOrgAgentUtil extends App {

  import InsertMedProfsUtil._

  val testProfOrgs = Array(
    Array(profOrgAgentId1, "医药公司业务员1", "医药公司业务员1 info", "13792929133", "2018-03-04T16:00:28.179") -> "123",
    Array(profOrgAgentId2, "医药公司业务员2", "医药公司业务员2 info", "1375555555", "2019-08-04T16:00:28.179") -> "456",
  )

  val insertStatementTemplate =
    "INSERT INTO prof_org_agents (agent_id, name, info, phone, join_date, pass_hash)" +
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
