package org.xg.dbUtils

import org.xg.auth.AuthHelpers
import org.xg.dbUtils.InsertProfOrgAgentUtil.insertStatements

object InsertMedProfOrgUtil {

  import org.xg.user.UserType._
  val orgId1 = MedProfOrg.genUid("org1")
  val orgId2 = MedProfOrg.genUid("org2")

  def main(args:Array[String]):Unit = {
    val testProfOrgs = Array(
      Array(orgId1, "医药公司1", "医药公司1 info", "13792929133", "2018-03-04T16:00:28.179") -> "123",
      Array(orgId2, "医药公司2", "医药公司2 info", "1375555555", "2019-08-04T16:00:28.179") -> "456",
    )
    val insertStatementTemplate =
      "INSERT INTO med_prof_orgs (org_id, name, info, phone, join_date, pass_hash)" +
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
}
