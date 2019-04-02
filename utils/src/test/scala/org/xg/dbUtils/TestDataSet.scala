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
    (1, 1, 1) -> (6 -> "朱"),
    (1, 1, 2) -> (2 -> "秦"),
    (1, 1, 3) -> (2 -> "尤"),
    (1, 1, 4) -> (0 -> "许"),
    (1, 2, 1) -> (2 -> "何"),
    (1, 2, 2) -> (1 -> "吕"),
    (1, 3, 1) -> (2 -> "施"),
    (1, 3, 2) -> (2 -> "孔"),
    (1, 3, 3) -> (1 -> "曹"),
    (2, 1, 1) -> (2 -> "严"),
    (2, 1, 2) -> (2 -> "华"),
    (2, 1, 3) -> (2 -> "金"),
    (2, 2, 1) -> (2 -> "魏"),
    (3, 1, 1) -> (0 -> "陶"),
    (3, 1, 2) -> (0 -> "姜")
  )

  val MedProfBase =  IndexedSeq(
    IndexedSeq("卫东", "3302030222313322", "13792929133") -> "123",
    IndexedSeq("鑫", "33020555555555555", "1375555555") -> "456",
    IndexedSeq("伟", "33020555555555556", "1375555556") -> "456",
    IndexedSeq("伟达", "3302036666666666", "1376666666") -> "abcdef"
  )

  private def insertCustomerData(coord:(Int, Int, Int), count:Int):Unit = {
    val (o, a, p) = coord

    val data = CustomerBaseData.take(count)
    val genData = genCustomers(
      o, a, p, data
    )
    InsertCustomersUtil.insertCustomers(genData, _ => profId(o, a, p))
  }

  private def insertProfData(coord:(Int, Int, Int), surname:String):Unit = {
    val (o, a, p) = coord
    val data = MedProfBase(p-1)
    val pid = profId(o, a, p)
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
      val (count, surname) = p._2
      insertProfData(p._1, surname)
      insertCustomerData(p._1, count)
    }
//    InsertCustomersUtil.insertCustomers(o1a1p1Customers, _ => profId(1, 1, 1))
//    println()
//    InsertCustomersUtil.insertCustomers(o1a1p2Customers, _ => profId(1, 1, 2))
//    println()
//    InsertCustomersUtil.insertCustomers(o1a1p3Customers, _ => profId(1, 1, 3))
  }

}
