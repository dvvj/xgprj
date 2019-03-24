package org.xg.hbn

import java.io.File
import java.time.ZonedDateTime
import java.util
import java.util.Date

import javax.persistence.TemporalType
import org.hibernate.{Session, SessionFactory}
import org.xg.auth.AuthHelpers
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

    override def allProfOrgs: Array[MProfOrg] = {
      runInTransaction(
        sessFactory,
        { sess =>
          //          val param = "customerList"
          queryAndConvert(sess, classOf[ProfOrg].getName, convertProfOrg)
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

    override def allPricePlanMaps: Array[MPricePlanMap] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryAndConvert(sess, classOf[PricePlanMap].getName, convertPricePlanMap)
//          val ql = s"Select ppm from ${classOf[PricePlanMap].getName} ppm"
//          val q = sess.createQuery(ql)
//          val t = q.getResultList.asScala.map(_.asInstanceOf[PricePlanMap])
//          //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
//          val res = t.toArray.map(convertPricePlanMap)
//          res
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

    override def addNewMedProf(profId: String, name: String, pass: String, idCardNo: String, mobile: String, orgId:String): String = {
      runInTransaction(
        sessFactory,
        { sess =>
          val passHash = AuthHelpers.sha512(pass)
          val medProf = new MedProf(
            profId, name, passHash,
            idCardNo, mobile,
            orgId
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

    override def updateOrder(orderId: Long, newQty:Double): Boolean = {
      val updateTime = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>
          val orderQuery = s"Select o from ${classOf[Order].getName} o where o.id = $orderId"
          val order = sess.createQuery(orderQuery).getResultList.get(0).asInstanceOf[Order]
          val morder = convertOrder(order)
          if (morder.canBeModified) {
            val histEntry = new OrderHistory(order.getId, DataUtils.utcTimeNow, order.getQty)
            order.setQty(newQty)
            sess.update(order)
            sess.save(histEntry)
            true
          }
          else {
            loggingTodo("Order locked (cannot be modified anymore)!")
            false
          }
        }
      )
    }

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

    override def payOrder(orderId: Long): Boolean = {
      val payTime = DataUtils.utcTimeNow
      runInTransaction(
        sessFactory,
        { sess =>
          val orderQuery = s"Select o from ${classOf[Order].getName} o where o.id = $orderId"
          val order = sess.createQuery(orderQuery).getResultList.get(0).asInstanceOf[Order]
          val morder = convertOrder(order)
          if (morder.canBePayed) {
            order.setPayTime(payTime)
            sess.update(order)
            val orgOrderStatQuery = s"Select o from ${classOf[OrgOrderStat].getName} o where o.orderId = $orderId"
            val orderStat = sess.createQuery(orgOrderStatQuery).getResultList.get(0).asInstanceOf[OrgOrderStat]
            orderStat.setStatus(MOrgOrderStat.Status_Paid)
            sess.update(orderStat)
            true
          }
          else {
            loggingTodo("Order locked (cannot be modified anymore)!")
            false
          }
        }
      )
    }

    override def placeOrder(uid: String, refUid:String, orgId:String, productId: Int, qty: Double, actualCost:Double): Long = {
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

          val orgOrderStat = new OrgOrderStat(
            orgId,
            refUid,
            orderId,
            order.getProductId,
            order.getQty,
            order.getActualCost,
            order.getCreationTime,
            MOrgOrderStat.Status_Created
          )

          sess.save(orgOrderStat)

          orderId.asInstanceOf[Long]
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

    override def getProfOrgPassMap: Map[String, Array[Byte]] = {
      getUidPassMap("orgId", classOf[ProfOrg].getName)
//      runInTransaction(
//        sessFactory,
//        { sess =>
//          val ql = s"Select mp.orgId, mp.passHash from ${classOf[ProfOrg].getName} mp"
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

    override def ordersOf_Unpaid(uid: String): Array[MOrder] = {
      runInTransaction(
        sessFactory,
        { sess =>
          val ql = s"Select o from ${classOf[Order].getName} o where o.customerId = '$uid' and o.payTime is null "
          val q = sess.createQuery(ql)
          val res = q.getResultList.asScala
            .toArray.map(c => convertOrder(c.asInstanceOf[Order]))
          res
        }
      )

    }

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

    override def profsOf(profOrgId: String): Array[MMedProf] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryWhereAndConvert(sess,
            classOf[MedProf].getName,
            s"x.orgId = '$profOrgId'",
            convertMedProf
          )
        }
      )
    }

    override def getOrderStat4Org(orgId: String): Array[MOrgOrderStat] = {
      runInTransaction(
        sessFactory,
        { sess =>
          queryWhereAndConvert(sess,
            classOf[OrgOrderStat].getName,
            s"x.orgId = '$orgId'",
            convertOrgOrderStat
          )
        }
      )

    }
  }

  def hbnOps(cfgFile:String):TDbOps = new OpsImpl(
    HbnUtils.getSessFactory(cfgFile)
  )

  val testHbnOps:TDbOps = new OpsImpl(HbnUtils.testSessFactory)
}
