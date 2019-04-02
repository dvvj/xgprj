package org.xg.dbUtils

import org.xg.dbUtils.InsertMedProfOrgUtil.{orgId1, orgId2}
import org.xg.user.UserType._

object TestDataSet {

  val customerInId = "customer"
  val profInId = "prof"
  val agentInId = "agent"
  val proforgInId = "proforg"

  def orgAgentProfPfx(o:Int, a:Int, p:Int):String = "o%da%dp%d_".format(o, a, p)
  def customerId(pfx:String, c:Int):String = Customer.genUid(pfx + s"$customerInId$c")
  def customerId(o:Int, a:Int, p:Int, c:Int):String = Customer.genUid(orgAgentProfPfx(o, a, p) + s"$customerInId$c")

  def orgAgentPfx(o:Int, a:Int):String = "o%da%d_".format(o, a)
  def profId(o:Int, a:Int, p:Int):String = MedProf.genUid(orgAgentPfx(o, a) + s"$profInId$p")

  def orgPfx(o:Int):String = "o%d_".format(o)
  def agentId(o:Int, a:Int):String = MedProfOrgAgent.genUid(orgPfx(o) + s"$agentInId$a")

  def proforgId(o:Int):String = MedProfOrg.genUid(s"$proforgInId$o")

  val pfx2Surname:Map[String, String] = Map(
    orgAgentProfPfx(1, 1, 1) -> "张",
    orgAgentProfPfx(1, 1, 2) -> "王",
    orgAgentProfPfx(1, 1, 3) -> "赵",
    orgAgentProfPfx(1, 1, 4) -> "钱",
    orgAgentProfPfx(1, 2, 1) -> "孙",
    orgAgentProfPfx(1, 2, 2) -> "李",
    orgAgentProfPfx(1, 3, 1) -> "周",
    orgAgentProfPfx(1, 3, 2) -> "吴",
    orgAgentProfPfx(1, 3, 3) -> "郑",
    orgAgentProfPfx(2, 1, 1) -> "冯",
    orgAgentProfPfx(2, 1, 2) -> "陈",
    orgAgentProfPfx(2, 1, 3) -> "沈",
    orgAgentProfPfx(2, 2, 1) -> "韩",
    orgAgentProfPfx(3, 1, 1) -> "杨",
    orgAgentProfPfx(3, 1, 2) -> "蒋"
  )

  def genCustomers(o:Int, a:Int, p:Int, rawData:IndexedSeq[(IndexedSeq[String], String)]):IndexedSeq[(IndexedSeq[String], String)] = {
    val pfx = orgAgentProfPfx(o, a, p)

    rawData.indices.map { idx =>
      val customerIdx = idx+1
      val cid = customerId(pfx, customerIdx)
      val (arr, pass) = rawData(idx)
      val surnamedAdded = pfx2Surname(pfx) + arr(0)
      val sfx = s"$o$a$p$idx"
      val idCardNo = arr(1) + sfx
      val mobile = arr(2) + sfx
      val postAddr = arr(3) + sfx
      (IndexedSeq(cid) ++ IndexedSeq(surnamedAdded, idCardNo, mobile, postAddr, arr.last)) -> pass
    }
  }

  private val CustomerBaseData = IndexedSeq(
    IndexedSeq(
      "晓东", "3102030222313322", "13892929133", "邮寄地址1", "1983-02-05"
    ) -> "123",
    IndexedSeq(
      "晓", "31020555555555555", "1385555555", "邮寄地址2", "1983-02-05"
    ) -> "456",
    IndexedSeq(
      "东", "3102036666666666", "1386666666", "邮寄地址3", "1983-12-03"
    ) -> "abcdef",
    IndexedSeq(
      "丽", "3102033333333333", "13833333333", "邮寄地址4", "1983-12-03"
    ) -> "acf",
    IndexedSeq(
      "丽丽", "3102033333333334", "13833333334", "邮寄地址5", "1983-12-04"
    ) -> "acf",
    IndexedSeq(
      "晓丽", "3102033333333334", "13833333334", "邮寄地址6", "1983-12-04"
    ) -> "acf"
  )


  val allCustomers = Seq(
    (1, 1, 1) -> 6,
    (1, 1, 2) -> 2,
    (1, 1, 3) -> 2,
    (1, 1, 4) -> 0,
    (1, 2, 1) -> 2,
    (1, 2, 2) -> 1,
    (1, 3, 1) -> 2,
    (1, 3, 2) -> 2,
    (1, 3, 3) -> 1,
    (2, 1, 1) -> 2,
    (2, 1, 2) -> 2,
    (2, 1, 3) -> 2,
    (2, 2, 1) -> 2,
    (3, 1, 1) -> 0,
    (3, 1, 2) -> 0
  )

  val MedProfBase =  IndexedSeq(
    IndexedSeq("卫东", "3302030222313322", "13792929133") -> "123",
    IndexedSeq("鑫", "33020555555555555", "1375555555") -> "456",
    IndexedSeq("伟", "33020555555555556", "1375555556") -> "456",
    IndexedSeq("伟达", "3302036666666666", "1376666666") -> "abcdef"
  )

  val OrgAgentBase = IndexedSeq(
    IndexedSeq("建华", "业务员1 info", "13792929133", "2018-03-04T16:00:28.179") -> "123",
    IndexedSeq("芳芳", "业务员2 info", "1375555555", "2019-08-04T16:00:28.179") -> "456",
    IndexedSeq("爱国", "业务员3 info", "13755555566", "2019-08-04T16:00:28.179") -> "456",
    IndexedSeq("娜", "业务员4 info", "13755555577", "2019-08-04T16:00:28.179") -> "456",
  )

  val agent2ProfSurnameMap = Map(
    (1, 1) -> "朱",
    (1, 2) -> "秦",
    (1, 3) -> "尤",
    (1, 4) -> "许",
    (2, 1) -> "何",
    (2, 2) -> "吕",
    (3, 1) -> "施"
  )

  val org2AgentData = IndexedSeq(
    1 -> (4, "孔"),
    2 -> (2, "曹"),
    3 -> (1, "严")
  )

  val ProfOrgBase = IndexedSeq(
    IndexedSeq("医药公司1", "医药公司1 info", "13792929133", "2018-03-04T16:00:28.179") -> "123",
    IndexedSeq("医药公司2", "医药公司2 info", "1375555555", "2019-08-04T16:00:28.179") -> "456",
    IndexedSeq("医药公司3", "医药公司3 info", "1375555533333", "2019-08-04T16:00:28.179") -> "456",
  )

  private def genOrgAgents(
    orgIdx:Int,
    surname:String,
    rawData:IndexedSeq[(IndexedSeq[String], String)]
  ):IndexedSeq[(IndexedSeq[String], String)] = {

    rawData.indices.map { idx =>
      val (arr, pass) = rawData(idx)
      val aidx = idx+1
      val orgId = proforgId(orgIdx)
      val aid = agentId(orgIdx, aidx)
      val name = surname + arr(0)
      val info = arr(1)
      val sfx = s"$orgIdx$aidx"
      val phone = arr(2) + sfx
      val joinDate = arr(3)
      IndexedSeq(aid, orgId, name, info, phone, joinDate) -> pass
    }
  }

  private def insertOrgAgentData():Unit = {
    org2AgentData.foreach { p =>
      val (orgIdx, agentData) = p
      val (agentCount, surname) = agentData
      val orgId = proforgId(orgIdx)

      val data = OrgAgentBase.take(agentCount)
      val genData = genOrgAgents(orgIdx, surname, data)
      InsertProfOrgAgentUtil.insertOrgAgents(genData)
    }

    val orgData = ProfOrgBase.indices.map { idx =>
      val (arr, pass) = ProfOrgBase(idx)
      val orgId = proforgId(idx+1)
      (IndexedSeq(orgId) ++ arr) -> pass
    }
    InsertMedProfOrgUtil.insertMedProfOrgs(orgData)
  }

  private def insertCustomerData(coord:(Int, Int, Int), count:Int):Unit = {
    val (o, a, p) = coord

    val data = CustomerBaseData.take(count)
    val genData = genCustomers(
      o, a, p, data
    )
    InsertCustomersUtil.insertCustomers(genData, _ => profId(o, a, p))
  }

  private def insertProfData(coord:(Int, Int, Int)):Unit = {
    val (o, a, p) = coord
    val data = MedProfBase(p-1)
    val pid = profId(o, a, p)
    val surname = agent2ProfSurnameMap(o -> a)
    val name = surname + data._1(0)

    val sfx = s"$o$a$p"
    val ex = data._1.tail.map(_ + sfx)
    val genData = (IndexedSeq(pid, name) ++ ex) -> data._2

    InsertMedProfsUtil.insertMedProf(
      IndexedSeq(genData), _ => agentId(o, a)
    )
  }

  def main(args:Array[String]):Unit = {
    allCustomers.foreach { p =>
      println()
      val count = p._2
      insertProfData(p._1)
      insertCustomerData(p._1, count)
    }

    insertOrgAgentData()
//    InsertCustomersUtil.insertCustomers(o1a1p1Customers, _ => profId(1, 1, 1))
//    println()
//    InsertCustomersUtil.insertCustomers(o1a1p2Customers, _ => profId(1, 1, 2))
//    println()
//    InsertCustomersUtil.insertCustomers(o1a1p3Customers, _ => profId(1, 1, 3))
  }

}
