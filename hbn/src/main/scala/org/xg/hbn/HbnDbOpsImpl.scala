package org.xg.hbn

import java.io.File
import java.time.ZonedDateTime
import java.util
import java.util.Date

import javax.persistence.TemporalType
import org.hibernate.{Session, SessionFactory}
import org.xg.auth.AuthHelpers
import org.xg.busiLogic.OrderStatusLogics
import org.xg.dbModels.TDbOps
import org.xg.dbModels._
import org.xg.gnl.DataUtils
import org.xg.hbn.ent.{Customer, Product}
import org.xg.hbn.utils.HbnUtils

import scala.reflect.ClassTag

object HbnDbOpsImpl {

  import Helpers._

  import collection.JavaConverters._
  import org.xg.hbn.ent._

  import DataUtils._

  private def queryAndConvert[TFrom : ClassTag, TTo : ClassTag](sess:Session, className:String, converter:TFrom => TTo):Array[TTo] = {
    val ql = s"Select x from $className x"
    val q = sess.createQuery(ql)
    val t = q.getResultList.asScala.map(_.asInstanceOf[TFrom])
    //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
    val res = t.map(converter).toArray
    res
  }
  private def queryWhereAndConvert[TFrom : ClassTag, TTo : ClassTag](
                                                                      sess:Session,
                                                                      className:String,
                                                                      whereClause:String,
                                                                      converter:TFrom => TTo
                                                                    ):Array[TTo] = {
    val ql = s"Select x from $className x where $whereClause"
    val q = sess.createQuery(ql)
    val t = q.getResultList.asScala.map(_.asInstanceOf[TFrom])
    //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
    val res = t.map(converter).toArray
    res
  }

  import org.xg.dbModels.TDbOps._
  private val Unfiltered:OrderFilter = _ => true
  private class OpsImpl(sessFactory:SessionFactory) extends TDbOps {
    override def addNewCustomer(
                                 uid: String,
                                 name: String,
                                 pass: String,
                                 idCardNo: String,
                                 mobile: String,
                                 postalAddr: String,
                                 ref_uid: String,
                                 bday: String
                               ): String = {
      runInTransaction(
        sessFactory,
        { sess =>
          val passHash = AuthHelpers.sha512(pass)
          val customer = new Customer(
            uid, name, passHash,
            idCardNo, mobile, postalAddr, bday, ref_uid
          )
          sess.save(customer)
          customer.getUid
        }
      )
    }

    override def getCustomerProfiles(uid: String): Array[MCustomerProfile] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryWhereAndConvert(
            sess,
            classOf[CustomerProfile].getName,
            s"x.customerId = '$uid'",
            convertCustomerProfile
          )
        }
      )
    }

    override def addRewardPlan(rwPlan:MRewardPlan):String = {
      runInTransaction(
        sessFactory,
        { sess =>
          val plan = new RewardPlan(
            rwPlan.id,
            rwPlan.info,
            rwPlan.defi,
            rwPlan.vtag.toString,
            rwPlan.creator
          )
          sess.save(plan)
          plan.getId
        }
      )
    }

    override def addPricePlan(prPlan: MPricePlan): String = {
      runInTransaction(
        sessFactory,
        { sess =>
          val plan = new PricePlan(
            prPlan.id,
            prPlan.info,
            prPlan.defi,
            prPlan.vtag.toString,
            prPlan.creator
          )
          sess.save(plan)
          plan.getId
        }
      )
    }

    override def allProfOrgAgents: Array[MProfOrgAgent] = {
      runInTransaction(
        sessFactory,
        { sess =>
          //          val param = "customerList"
          queryAndConvert(sess, classOf[ProfOrgAgent].getName, convertProfOrg)
        }
      )
    }

    override def allRewardPlanMaps: Array[MRewardPlanMap] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryAndConvert(sess, classOf[RewardPlanMap].getName, convertRewardPlanMap)
        }
      )
    }

    override def allRewardPlans: Array[MRewardPlan] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryAndConvert(sess, classOf[RewardPlan].getName, convertRewardPlan)
        }
      )
    }

    override def allSvcAudit:Array[MSvcAudit] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryAndConvert(sess, classOf[SvcAudit].getName, convertSvcAudit)
        }
      )
    }

    override def orderStatsOfOrg(orgId: String): Array[MOrgOrderStat] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryWhereAndConvert(
            sess,
            classOf[OrgOrderStat].getName,
            s"x.orgId = '$orgId'",
            convertOrgOrderStat
          )
        }
      )
    }

    override def allPricePlanMaps: Array[MPricePlanMap] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryAndConvert(sess, classOf[PricePlanMap].getName, convertPricePlanMap)
        }
      )
    }

    override def addPricePlanMap(ppm: MPricePlanMap):Boolean = {
      runInTransaction(
        sessFactory,
        { sess =>
          val p = revConvertPricePlanMap(ppm)
          sess.save(p)
          true
        }
      )
    }


    override def addRewardPlanMap(rpm: MRewardPlanMap):Boolean = {
      runInTransaction(
        sessFactory,
        { sess =>
          val p = revConvertRewardPlanMap(rpm)
          sess.save(p)
          true
        }
      )
    }

    override def pricePlansByUid(uid: String): Array[MPricePlanMap] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select ppm from ${classOf[PricePlanMap].getName} ppm where uid = '$uid'"
          val q = sess.createQuery(ql)
          val t = q.getResultList.asScala.map(_.asInstanceOf[PricePlanMap])
          //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
          val res = t.toArray.map(convertPricePlanMap)
          res
        }
      )
    }

    override def allPricePlans: Array[MPricePlan] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select pp from ${classOf[PricePlan].getName} pp"
          val q = sess.createQuery(ql)
          val t = q.getResultList.asScala.map(_.asInstanceOf[PricePlan])
          //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
          val res = t.toArray.map(convertPricePlan)
          res
        }
      )
    }

    override def customersRefedBy(refUid: String): Array[MCustomer] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select c from ${classOf[Customer].getName} c where c.refUid = '$refUid'"

          val q = sess.createQuery(ql)
          val t = q.getResultList.asScala.map(_.asInstanceOf[Customer])
          //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
          val res = t.toArray.map(convertCustomer)
          res
        }
      )
    }

    override def allMedProfs: Array[MMedProf] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select mp from ${classOf[MedProf].getName} mp"

          val q = sess.createQuery(ql)
          val t = q.getResultList.asScala.map(_.asInstanceOf[MedProf])
          //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
          val res = t.toArray.map(convertMedProf)
          res
        }
      )
    }

    override def addNewMedProf(profId: String, name: String, pass: String, idCardNo: String, mobile: String, orgAgentId:String): String = {
      runInTransaction(
        sessFactory,
        { sess =>
          val passHash = AuthHelpers.sha512(pass)
          val medProf = new MedProf(
            profId, name, passHash,
            idCardNo, mobile,
            orgAgentId
          )
          sess.save(medProf)
          profId
        }
      )
    }

    override def testAllOrderHistory: Array[MOrderHistory] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val orderQuery = s"Select oh from ${classOf[OrderHistory].getName} oh"
          val orderHistories = sess.createQuery(orderQuery).getResultList
            .asScala.map(_.asInstanceOf[OrderHistory])
          orderHistories.map(convertOrderHistory).toArray
        }
      )
    }

//    override def updateOrder(orderId: Long, newQty:Double): Boolean = {
//      val updateTime = DataUtils.utcTimeNow
//      runInTransaction(
//        sessFactory,
//        { sess =>
//          val orderQuery = s"Select o from ${classOf[Order].getName} o where o.id = $orderId"
//          val order = sess.createQuery(orderQuery).getResultList.get(0).asInstanceOf[Order]
//          val morder = convertOrder(order)
//          if (morder.canBeModified) {
//            val histEntry = new OrderHistory(order.getId, DataUtils.utcTimeNow, order.getQty)
//            order.setQty(newQty)
//            sess.update(order)
//            sess.save(histEntry)
//            true
//          }
//          else {
//            loggingTodo("Order locked (cannot be modified anymore)!")
//            false
//          }
//        }
//      )
//    }

    override def lockOrders: Array[MOrder] = {
      val currTime = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>
          val orderQuery = s"Select o from ${classOf[Order].getName} o where o.procTime1 is null"
          val orders = sess.createQuery(orderQuery).getResultList
            .asScala.map(_.asInstanceOf[Order])
          orders.foreach { o =>
            o.setProcTime1(currTime)
            sess.update(o)
          }
          orders.map(convertOrder).toArray
        }
      )
    }

    override def saveAlipayNotifyRawAndPayOrder(notifyRaw:String, orderId:Long, tradeDt:String): Boolean = {
      val payTime = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>

          val orderQuery = s"Select o from ${classOf[Order].getName} o where o.id = $orderId"
          val order = sess.createQuery(orderQuery).getResultList.get(0).asInstanceOf[Order]
          val morder = convertOrder(order)
          if (morder.canBePaid) {
            order.setPayTime(payTime)
            sess.update(order)
            val orgOrderStatQuery = s"Select o from ${classOf[OrgAgentOrderStat].getName} o where o.orderId = $orderId"
            val orderStat = sess.createQuery(orgOrderStatQuery).getResultList.get(0).asInstanceOf[OrgAgentOrderStat]
            orderStat.setStatus(OrderStatusLogics.Status_Paid)
            sess.update(orderStat)

            val alipayNotifyRaw = new AlipayNotifyRaw(
              orderId, tradeDt, notifyRaw
            )
            sess.save(alipayNotifyRaw)

            true
          }
          else {
            val msg = s"Order cannot be paid: $morder"
            loggingTodo(msg)
            throw new IllegalStateException(msg)
          }
        }
      )
    }

    override def saveAlipayNotifyRawButDoNotPay(notifyRaw:String):Boolean = {
      runInTransaction(
        sessFactory,
        { sess =>

          val alipayNotifyRaw = new AlipayNotifyRaw(
            null, null, notifyRaw
          )
          sess.save(alipayNotifyRaw)
          true
        }
      )
    }

    override def cancelOrder(orderId: Long): Boolean = {
      val cancelTime = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>
          val orderQuery = s"Select o from ${classOf[Order].getName} o where o.id = $orderId"
          val order = sess.createQuery(orderQuery).getResultList.get(0).asInstanceOf[Order]
          val morder = convertOrder(order)
          if (morder.canBeCancelled) {
            order.setProcTime2(cancelTime)
            sess.update(order)
            val orgOrderStatQuery = s"Select o from ${classOf[OrgAgentOrderStat].getName} o where o.orderId = $orderId"
            val orderStat = sess.createQuery(orgOrderStatQuery).getResultList.get(0).asInstanceOf[OrgAgentOrderStat]
            orderStat.setStatus(OrderStatusLogics.Status_Paid)
            sess.update(orderStat)
            true
          }
          else {
            val orderStatus = OrderStatusLogics.status(morder)
            loggingTodo("Order cannot be cancelled, status: " + orderStatus)
            false
          }
        }
      )
    }

    override def placeOrder(uid: String, refUid:String, orgAgentId:String, productId: Int, qty: Double, actualCost:Double): Long = {
      val creationTime = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>
          val order = new Order(
            uid,
            productId,
            qty,
            actualCost,
            creationTime
          )
          val orderId:java.lang.Long = sess.save(order).asInstanceOf[java.lang.Long]

          val orgOrderStat = new OrgAgentOrderStat(
            orgAgentId,
            refUid,
            orderId,
            order.getProductId,
            order.getQty,
            order.getActualCost,
            order.getCreationTime,
            OrderStatusLogics.Status_CreatedNotPaid
          )

          sess.save(orgOrderStat)

          orderId.asInstanceOf[Long]
        }
      )
    }

    override def svcAuditEx(ops: String, status: Int, duration: Int, uid: String, extra: String): OpResp = {
      val ts = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>
          val audit = new SvcAudit(
            ops,
            ts,
            status,
            duration,
            uid,
            extra
          )

          val auditId = sess.save(audit)
          println(s"Audit added: $auditId")

          OpResp.Success
        }
      )
    }

    override def allCustomers: Array[MCustomer] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select c from ${classOf[Customer].getName} c"

          val q = sess.createQuery(ql)
          val t = q.getResultList.asScala.map(_.asInstanceOf[Customer])
  //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
          val res = t.toArray.map(convertCustomer)
          res
        }
      )
    }


    override def allProducts: Array[MProduct] =     {
      runInTransaction(
        sessFactory, { sess =>
          val queryProducts = s"Select p from ${classOf[Product].getName} p"
          val prods = sess.createQuery(queryProducts).getResultList.asScala
          val queryAssets = s"Select a from ${classOf[Asset].getName} a"
          val assets = sess.createQuery(queryAssets).getResultList.asScala
          val prodMap = prods.map(_.asInstanceOf[Product]).map(p => p.getId -> p).toMap
          val res = assets.map(_.asInstanceOf[Asset]).map { asset =>
            val prod = prodMap(asset.getProductId)
            convertProduct(prod, asset)
          }

          res.toArray
        }
      )
    }

    private def getUidPassMap(uidField:String, className:String):Map[String, Array[Byte]] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select c.$uidField, c.passHash from $className c"

          val q = sess.createQuery(ql)
          val t = q.getResultList.asScala.map { r =>
            val objArr = r.asInstanceOf[Array[AnyRef]]
            val uid = objArr(0).asInstanceOf[String]
            val passHash = objArr(1).asInstanceOf[Array[Byte]]
            uid -> passHash
          }
          t.toMap
        }
      )
    }

    override def getUserPassMap: Map[String, Array[Byte]] = {
      getUidPassMap("uid", classOf[Customer].getName)
    }

    override def getMedProfPassMap: Map[String, Array[Byte]] = {
      getUidPassMap("profId", classOf[MedProf].getName)
//      runInTransaction(
//        sessFactory,
//        { sess =>
//          val ql = s"Select mp.profId, mp.passHash from ${classOf[MedProf].getName} mp"
//
//          val q = sess.createQuery(ql)
//          val t = q.getResultList.asScala.map { r =>
//            val objArr = r.asInstanceOf[Array[AnyRef]]
//            val uid = objArr(0).asInstanceOf[String]
//            val passHash = objArr(1).asInstanceOf[Array[Byte]]
//            uid -> passHash
//          }
//          t.toMap
//        }
//      )
    }

    override def getProfOrgAgentPassMap: Map[String, Array[Byte]] = {
      getUidPassMap("orgAgentId", classOf[ProfOrgAgent].getName)
    }

    override def getMedProfOrgPassMap: Map[String, Array[Byte]] = {
      getUidPassMap("orgId", classOf[MedProfOrg].getName)
    }

    override def ordersOf(uid: String): Array[MOrder] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select o from ${classOf[Order].getName} o where o.customerId = '$uid'"
          val q = sess.createQuery(ql)
          val res = q.getResultList.asScala
            .toArray.map(c => convertOrder(c.asInstanceOf[Order]))
          res
        }
      )
    }

//    override def ordersOf_Unpaid(uid: String): Array[MOrder] = {
//      runInTransaction(
//        sessFactory,
//        { sess =>
//          val ql = s"Select o from ${classOf[Order].getName} o where o.customerId = '$uid' and o.payTime is null "
//          val q = sess.createQuery(ql)
//          val res = q.getResultList.asScala
//            .toArray.map(c => convertOrder(c.asInstanceOf[Order]))
//          res
//        }
//      )
//
//    }

    override def ordersOf_CreationTimeWithin(uid: String, days: Int): Array[MOrder] = {
      val zdtNow = DataUtils.utcTimeNow
      val creationDate0 = Date.from(zdtNow.minusDays(days).toInstant)
      val paramCreationDate = "creationDate"
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select o from ${classOf[Order].getName} o where o.customerId = '$uid' and o.creationTime >= :$paramCreationDate"
          val q = sess.createQuery(ql)
          q.setParameter(paramCreationDate, creationDate0, TemporalType.DATE)
          val res = q.getResultList.asScala
            .toArray.map(c => convertOrder(c.asInstanceOf[Order]))
          res
        }
      )
      //val ZonedDateTime.from(creationDate0.toInstant)

    }


    override def ordersOfCustomers(customerIds: Array[String]): Array[MOrder] = {
      runInTransaction(
        sessFactory,
        { sess =>
          //          val param = "customerList"
          val paramVal = customerIds.mkString("'", "','", "'")
          val q = sess.createQuery(s"Select x from ${classOf[Order].getName} x where x.customerId in ($paramVal)")
          val t = q.getResultList.asScala.map(_.asInstanceOf[Order])
          t.map(convertOrder).toArray
        }
      )
    }

    override def ordersOfCustomers_Unpaid(customerIds: Array[String]): Array[MOrder] = {
      runInTransaction(
        sessFactory,
        { sess =>
          //          val param = "customerList"
          val paramVal = customerIds.mkString("'", "','", "'")
          val q = sess.createQuery(s"Select x from ${classOf[Order].getName} x where x.customerId in ($paramVal) and x.payTime is null")
          val t = q.getResultList.asScala.map(_.asInstanceOf[Order])
          t.map(convertOrder).toArray
        }
      )
    }

    override def ordersOfCustomers_CreationTimeWithin(customerIds: Array[String], days: Int): Array[MOrder] = {
      val zdtNow = DataUtils.utcTimeNow
      val creationDate0 = Date.from(zdtNow.minusDays(days).toInstant)
      val paramCreationDate = "creationDate"
      runInTransaction(
        sessFactory,
        { sess =>
          //          val param = "customerList"
          val paramVal = customerIds.mkString("'", "','", "'")
          val q = sess.createQuery(s"Select x from ${classOf[Order].getName} x where x.customerId in ($paramVal) and o.creationTime >= :$paramCreationDate")
          q.setParameter(paramCreationDate, creationDate0, TemporalType.DATE)
          val t = q.getResultList.asScala.map(_.asInstanceOf[Order])
          t.map(convertOrder).toArray
        }
      )

    }


    override def customersOf(profId: String): Array[MCustomer] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select c from ${classOf[Customer].getName} c where c.refUid = '$profId'"
          val q = sess.createQuery(ql)
          val res = q.getResultList.asScala
            .toArray.map(c => convertCustomer(c.asInstanceOf[Customer]))
          res
        }
      )
    }

    override def profsOf(orgAgentId: String): Array[MMedProf] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryWhereAndConvert(sess,
            classOf[MedProf].getName,
            s"x.orgAgentId = '$orgAgentId'",
            convertMedProf
          )
        }
      )
    }

    override def getOrderStat4OrgAgent(orgAgentId: String): Array[MOrgAgentOrderStat] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryWhereAndConvert(sess,
            classOf[OrgAgentOrderStat].getName,
            s"x.orgAgentId = '$orgAgentId'",
            convertOrgOrderStat
          )
        }
      )

    }

    override def updateCustomerPass(customerId:String, oldPassHash:Array[Byte], newPassHash:Array[Byte]):OpResp = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select c from ${classOf[Customer].getName} c where c.uid = '$customerId'"
          val q = sess.createQuery(ql)
          val res = q.getResultList
          if (res.size() != 1)
            throw new IllegalStateException(s"Found ${res.size} customer with id [$customerId], (expecting exactly one)!")
          else {
            val c = res.get(0).asInstanceOf[Customer]
            if (oldPassHash.sameElements(c.getPassHash)) {
              c.setPassHash(newPassHash)
              sess.update(c)
              OpResp.Success
            }
            else {
              OpResp.failed("Old pass not match")
            }
          }
        }
      )
    }
  }

  def hbnOps(cfgFile:String):TDbOps = new OpsImpl(
    HbnUtils.getSessFactory(cfgFile)
  )

  val testHbnOps:TDbOps = new OpsImpl(HbnUtils.testSessFactory)
}
