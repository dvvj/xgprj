package org.xg.dbUtils

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

  def genCustomers(o:Int, a:Int, p:Int, rawData:Array[(Array[String], String)]):Array[(Array[String], String)] = {
    val pfx = orgAgentProfPfx(o, a, p)

    rawData.indices.map { idx =>
      val customerIdx = idx+1
      val cid = customerId(pfx, customerIdx)
      val (arr, pass) = rawData(idx)
      arr(0) = pfx2Surname(pfx) + arr(0)
      (Array(cid) ++ arr) -> pass
    }.toArray
  }

  val o1a1p1Customers =
    genCustomers(1, 1, 1,
      Array(
        Array(
          "晓东", "3102030222313322", "13892929133", "邮寄地址1", "1983-02-05"
        ) -> "123",
        Array(
          "晓", "31020555555555555", "1385555555", "邮寄地址2", "1983-02-05"
        ) -> "456",
        Array(
          "东", "3102036666666666", "1386666666", "邮寄地址3", "1983-12-03"
        ) -> "abcdef",
        Array(
          "丽", "3102033333333333", "13833333333", "邮寄地址4", "1983-12-03"
        ) -> "acf",
        Array(
          "丽丽", "3102033333333334", "13833333334", "邮寄地址5", "1983-12-04"
        ) -> "acf",
        Array(
          "晓丽", "3102033333333334", "13833333334", "邮寄地址6", "1983-12-04"
        ) -> "acf"
      )
    )

  def main(args:Array[String]):Unit = {
    InsertCustomersUtil.insertCustomers(o1a1p1Customers, _ => profId(1, 1, 1))
  }

}
