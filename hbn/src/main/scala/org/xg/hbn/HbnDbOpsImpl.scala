package org.xg.hbn

import java.util

import org.xg.auth.AuthHelpers
import org.xg.db.api.TDbOps
import org.xg.db.model._
import org.xg.gnl.DataUtils
import org.xg.hbn.ent.{Customer, Product}

object HbnDbOpsImpl {

  import Helpers._

  import collection.JavaConverters._
  import org.xg.hbn.ent._

  import DataUtils._

  private class OpsImpl extends TDbOps {
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
      runInTransaction { sess =>
        val passHash = AuthHelpers.sha512(pass)
        val customer = new Customer(
          uid, name, passHash,
          idCardNo, mobile, postalAddr, bday, ref_uid
        )
        sess.save(customer)
        customer.getUid
      }
    }
//    {
//      runInTransaction { sess =>
//        val passHash = AuthHelpers.sha512(pass)
//        val customers = new Customer(
//          uid, name, passHash,
//          idCardNo, mobile,
//          postalAddr, bday, ref_uid
//        )
//      }
//      true
//    }

    override def allMedProfs: Array[MMedProf] = {
      runInTransaction { sess =>
        val ql = s"Select mp from ${classOf[MedProf].getName} mp"

        val q = sess.createQuery(ql)
        val t = q.getResultList.asScala.map(_.asInstanceOf[MedProf])
        //        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
        val res = t.toArray.map(convertMedProf)
        res
      }
    }

    override def addNewMedProf(profId: String, name: String, pass: String, idCardNo: String, mobile: String): String = {
      runInTransaction { sess =>
        val passHash = AuthHelpers.sha512(pass)
        val medProf = new MedProf(
          profId, name, passHash,
          idCardNo, mobile
        )
        sess.save(medProf)
        profId
      }
    }

    override def testAllOrderHistory: Array[MOrderHistory] = {
      runInTransaction { sess =>
        val orderQuery = s"Select oh from ${classOf[OrderHistory].getName} oh"
        val orderHistories = sess.createQuery(orderQuery).getResultList
          .asScala.map(_.asInstanceOf[OrderHistory])
        orderHistories.map(convertOrderHistory).toArray
      }
    }

    override def updateOrder(orderId: Long, newQty:Double): Boolean = {
      val updateTime = DataUtils.utcTimeNow
      runInTransaction { sess =>
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
    }

    override def lockOrders: Array[MOrder] = {
      val currTime = DataUtils.utcTimeNow
      runInTransaction { sess =>
        val orderQuery = s"Select o from ${classOf[Order].getName} o where o.procTime1 is null"
        val orders = sess.createQuery(orderQuery).getResultList
          .asScala.map(_.asInstanceOf[Order])
        orders.foreach { o =>
          o.setProcTime1(currTime)
          sess.update(o)
        }
        orders.map(convertOrder).toArray
      }

    }

    override def placeOrder(uid: String, productId: Int, qty: Double): Long = {
      val creationTime = DataUtils.utcTimeNow
      runInTransaction { sess =>
        val order = new Order(
          uid,
          productId,
          qty,
          creationTime
        )
        val orderId = sess.save(order)
        orderId.asInstanceOf[Long]
      }
    }

    override def allCustomers: Array[MCustomer] = {
      runInTransaction { sess =>
        val ql = s"Select c from ${classOf[Customer].getName} c"

        val q = sess.createQuery(ql)
        val t = q.getResultList.asScala.map(_.asInstanceOf[Customer])
//        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
        val res = t.toArray.map(convertCustomer)
        res
      }
    }


    override def allProducts: Array[MProduct] =     {
      runInTransaction { sess =>
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
    }

    override def getUserPassMap: Map[String, Array[Byte]] = {
      runInTransaction { sess =>
        val ql = s"Select c.uid, c.passHash from ${classOf[Customer].getName} c"

        val q = sess.createQuery(ql)
        val t = q.getResultList.asScala.map { r =>
          val objArr = r.asInstanceOf[Array[AnyRef]]
          val uid = objArr(0).asInstanceOf[String]
          val passHash = objArr(1).asInstanceOf[Array[Byte]]
          uid -> passHash
        }
        t.toMap
      }
    }

    override def getMedProfPassMap: Map[String, Array[Byte]] = {
      runInTransaction { sess =>
        val ql = s"Select mp.profId, mp.passHash from ${classOf[MedProf].getName} mp"

        val q = sess.createQuery(ql)
        val t = q.getResultList.asScala.map { r =>
          val objArr = r.asInstanceOf[Array[AnyRef]]
          val uid = objArr(0).asInstanceOf[String]
          val passHash = objArr(1).asInstanceOf[Array[Byte]]
          uid -> passHash
        }
        t.toMap
      }
    }

    override def ordersOf(uid: String): Array[MOrder] = {
      runInTransaction { sess =>
        val ql = s"Select o from ${classOf[Order].getName} o where o.customerId = '$uid'"
        val q = sess.createQuery(ql)
        val res = q.getResultList.asScala
          .toArray.map(c => convertOrder(c.asInstanceOf[Order]))
        res
      }
    }

    override def customersOf(profId: String): Array[MCustomer] = {
      runInTransaction { sess =>
        val ql = s"Select c from ${classOf[Customer].getName} c where c.refUid = '$profId'"
        val q = sess.createQuery(ql)
        val res = q.getResultList.asScala
          .toArray.map(c => convertCustomer(c.asInstanceOf[Customer]))
        res
      }
    }
  }

  val hbnOps:TDbOps = new OpsImpl
}
