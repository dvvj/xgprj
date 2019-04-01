package org.xg.dbUtils

import org.xg.user.UserType._
import org.xg.auth.AuthHelpers
//import org.xg.dbUtils.InsertCustomersUtil.testRefUid

object InsertMedProfsUtil {

  val profId1 = MedProf.genUid("prof1")
  val profId2 = MedProf.genUid("prof2")
  val profId3 = MedProf.genUid("prof3")

  val profOrgAgentId1 = MedProfOrgAgent.genUid("agent1")
  val profOrgAgentId2 = MedProfOrgAgent.genUid("agent2")

  val prof2OrgAgentMap:Map[String, String] = Map(
    profId1 -> profOrgAgentId1,
    profId2 -> profOrgAgentId1,
    profId3 -> profOrgAgentId2
  )

  val testProfs = IndexedSeq(
    IndexedSeq(profId1, "李卫东", "3302030222313322", "13792929133") -> "123",
    IndexedSeq(profId2, "张鑫", "33020555555555555", "1375555555") -> "456",
    IndexedSeq(profId3, "陈伟达", "3302036666666666", "1376666666") -> "abcdef",
  )

  def main(args:Array[String]):Unit = {

    insertMedProf(
      testProfs, prof2OrgAgentMap
    )
//    val insertStatementTemplate =
//      "INSERT INTO med_profs (prof_id, name, idcard_no, mobile, org_agent_id, pass_hash)" +
//        "  VALUES (%s);"
//
//    val insertStatements = testProfs.map { p =>
//      val (line, pass) = p
//      val passHash = AuthHelpers.sha512(pass)
//      val hashStr = AuthHelpers.hash2Str(passHash)
//      val profId = line(0)
//      val lineWithOrgAgentId = line ++ IndexedSeq(prof2OrgAgentMap(profId))
//      val params = lineWithOrgAgentId.mkString("'", "','", "'") + s",X'$hashStr'"
//      insertStatementTemplate.format(params)
//    }
//
//    println(insertStatements.mkString("\n"))
  }

  def insertMedProf(
                     rawData:IndexedSeq[(IndexedSeq[String],String)],
                     prof2OrgAgent:String => String
                   ):Unit = {
    val insertStatementTemplate =
      "INSERT INTO med_profs (prof_id, name, idcard_no, mobile, org_agent_id, pass_hash)" +
        "  VALUES (%s);"

    val insertStatements = rawData.map { p =>
      val (line, pass) = p
      val passHash = AuthHelpers.sha512(pass)
      val hashStr = AuthHelpers.hash2Str(passHash)
      val profId = line(0)
      val lineWithOrgAgentId = line ++ IndexedSeq(prof2OrgAgent(profId))
      val params = lineWithOrgAgentId.mkString("'", "','", "'") + s",X'$hashStr'"
      insertStatementTemplate.format(params)
    }

    println(insertStatements.mkString("\n"))
  }

}
