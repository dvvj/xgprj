package org.xg.dbUtils

import org.xg.dbModels.{MOrder, MPricePlan}
import org.xg.dbUtils.InsertCustomersUtil._
import org.xg.dbUtils.InsertMedProfOrgUtil.{orgId1, orgId2}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.pricePlan.v1.{PrPlFixedRate, PrPlProdBasedRates}
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.pay.rewardPlan.v1.{RwPlFixedRate, RwPlProdBasedRates}
import org.xg.user.UserType._

import scala.collection.mutable.ListBuffer

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

  val customerId2ProfIdMap:Map[String, String] = allCustomers.flatMap { pp =>
      val (coord, count) = pp
      val (o, a, p) = coord
      val pid = profId(o, a, p)
      (1 to count).map { customerIdx =>
        val cid = customerId(o, a, p, customerIdx)
        cid -> pid
      }
    }.toMap

  val profId2AgentIdMap:Map[String, String] = allCustomers.map { pp =>
      val (coord, count) = pp
      val (o, a, p) = coord
      val aid = agentId(o, a)
      val pid = profId(o, a, p)
      pid -> aid
    }.toMap

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

  private[dbUtils] def insertOrgAgentData():Unit = {
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

  private[dbUtils] def insertCustomerData(coord:(Int, Int, Int), count:Int):Unit = {
    val (o, a, p) = coord

    val data = CustomerBaseData.take(count)
    val genData = genCustomers(
      o, a, p, data
    )
    InsertCustomersUtil.insertCustomers(genData, _ => profId(o, a, p))
  }

  private[dbUtils] def insertProfData(coord:(Int, Int, Int)):Unit = {
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

  def genMonthlyOrders(startOrderId:Long, customerId:String, prodId:Int, qty:Double, actualCost:Double,
                       creationTimeFmt:String, payTimeFmt:String,
                       beginYearMonth:(Int, Int), endYearMonth:(Int, Int)):Array[MOrder] = {
    var orderId:Long = startOrderId

    val stepFunc:(Int, Int) => (Int, Int) = (year, month) => {
      if (month >= 12)
        (year+1, 1)
      else
        (year, month + 1)
    }

    var currYearMonth = beginYearMonth
    val res = ListBuffer[MOrder]()
    while (currYearMonth != endYearMonth) {
      val (year, month) = currYearMonth
      res += MOrder.createJ(
        orderId, customerId, prodId, qty, actualCost,
        creationTimeFmt.format(year, month),
        payTimeFmt.format(year, month)
        //"%d-%02d-02 19:30:44"
      )
      currYearMonth = stepFunc(year, month)
      orderId += 1
    }
    res.toArray
  }

  val customerId1111 = customerId(1, 1, 1, 1)
  val customerId1121 = customerId(1, 1, 2, 1)
  private[dbUtils] val orderData =
    genMonthlyOrders(
      1000L, customerId1111, 1, 2.0, 2399.99,
      "%d-%02d-02 19:30:44", "%d-%02d-02 19:50:44",
      2018 -> 1, 2019 -> 4
    ) ++ genMonthlyOrders(
      1100L, customerId1121, 1, 2.0, 2399.99,
      "%d-%02d-12 18:30:44", "%d-%02d-12 18:50:44",
      2018 -> 6, 2019 -> 4
    ) ++ Array(
      MOrder.createJ(
        10000L, customerId1111, 2, 2.0, 149.99,
        "2019-03-02 19:00:44"
        //"%d-%02d-02 19:30:44"
      ),
      MOrder.createJ(
        10001L, customerId1111, 2, 2.0, 149.99,
        "2019-03-02 19:20:44"
        //"%d-%02d-02 19:30:44"
      ),
    )

  val profId111 = profId(1, 1, 1)
  val profId112 = profId(1, 1, 2)
  val profId121 = profId(1, 2, 1)
  val agentId11 = agentId(1, 1)
  val agentId12 = agentId(1, 2)
  val agentId21 = agentId(2, 1)

  object RewardPlans {
    val RwPlanIdFixed010:String = "RwFixed-0.1"
    val RwPlanIdFixed010_A11:String = "RwFixed-0.1_A11"
    val RwPlanIdFixed020:String = "RwFixed-0.2"
    val RwPlanIdFixed030:String = "RwFixed-0.3"
    val RwPlanIdModifier1_5:String = "RwModifier-1.5"
    val RwPlanIdProdBasedBasic:String = "RwProdBased-Basic"
    val RwPlanIdProdBasedAdvanced:String = "RwProdBased-Advanced"
    import org.xg.pay.rewardPlan.RewardPlanSettings.VTag._
    import org.xg.dbModels.MRewardPlan._

    val planData = Array[(String, String, VTag, TRewardPlan, String)](
      (RwPlanIdFixed010, "所有商品10%", FixedRate, RwPlFixedRate(0.1), builtInCreator),
      (RwPlanIdFixed010_A11, "所有商品10%", FixedRate, RwPlFixedRate(0.1), agentId11),
      (RwPlanIdFixed020, "所有商品20%", FixedRate, RwPlFixedRate(0.2), builtInCreator),
      (RwPlanIdFixed030, "所有商品30%", FixedRate, RwPlFixedRate(0.3), builtInCreator),
      (RwPlanIdModifier1_5, "Fixed Rate x 1.5, used only in combination with other plans", FixedRate, RwPlFixedRate(1.5), builtInCreator),
      (RwPlanIdProdBasedBasic, "【商品1】20%，【商品2】30%，其余10%", ProductBasedRates,
        RwPlProdBasedRates(0.1, Map(1 -> 0.2, 2 -> 0.3)), builtInCreator),
      (RwPlanIdProdBasedAdvanced, "【商品1】30%，【商品2】40%，其余20%", ProductBasedRates,
        RwPlProdBasedRates(0.2, Map(1 -> 0.3, 2 -> 0.4)), builtInCreator),
    )

    val planMapData = Array(
      (agentId11, RwPlanIdFixed020, 0),
      (agentId12, RwPlanIdFixed030, 0),
      (profId111, RwPlanIdFixed010, -7),
      (profId112, RwPlanIdFixed020, 0),
      (profId121, s"$RwPlanIdProdBasedBasic,$RwPlanIdModifier1_5", 0),
      (agentId21, RwPlanIdProdBasedAdvanced, 0)
    )
  }

  object PricePlans {
    import org.xg.pay.pricePlan.PricePlanSettings.VTag._
    import MPricePlan._

    val PrPlanIdFixed09:String = "PrFixed-0.9"
    val PrPlanIdFixed09_P111:String = "PrFixed-0.9_P111"
    val PrPlanIdFixed095:String = "PrFixed-0.95"
    val PrPlanIdProdBasedBasic:String = "PrProdBased-Basic"
    val PrPlanIdProdBasedBasic_P111:String = "PrProdBased-Basic_P111"
    val PrPlanIdProdBasedAdvanced:String = "PrProdBased-Advanced"
    val planData = Array[(String, String, VTag, TPricePlan, String)](
      (PrPlanIdFixed09_P111, "所有商品9折", FixedRate, PrPlFixedRate(0.9), profId111),
      (PrPlanIdFixed09, "所有商品9折", FixedRate, PrPlFixedRate(0.9), builtInCreator),
      (PrPlanIdFixed095, "所有商品95折", FixedRate, PrPlFixedRate(0.95), builtInCreator),
      (PrPlanIdProdBasedBasic, "【商品1】9折，【商品2】85折，其余95折", ProductBasedRates,
        PrPlProdBasedRates(0.95, Map(1 -> 0.9, 2 -> 0.85)), builtInCreator),
      (PrPlanIdProdBasedBasic_P111, "【商品1】9折，【商品2】85折，其余95折", ProductBasedRates,
        PrPlProdBasedRates(0.95, Map(1 -> 0.9, 2 -> 0.85)), profId111),
      (PrPlanIdProdBasedAdvanced, "【商品1】8折，【商品2】85折，其余9折", ProductBasedRates,
        PrPlProdBasedRates(0.9, Map(1 -> 0.8, 2 -> 0.85)), builtInCreator),
    )


  }

}
