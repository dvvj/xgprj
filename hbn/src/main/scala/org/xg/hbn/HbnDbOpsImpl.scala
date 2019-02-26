package org.xg.hbn

import java.util

import org.xg.auth.AuthHelpers
import org.xg.db.api.TDbOps
import org.xg.db.model.{MCustomer, MOrder, MProduct}
import org.xg.hbn.ent.{Customer, Product}

object HbnDbOpsImpl {

  import Helpers._

  import collection.JavaConverters._
  import org.xg.hbn.ent._

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
                               ): Boolean = ???
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

    override def placeOrder(uid: String, productId: Int, qty: Double): Long = ???

    override def allCustomers: Array[MCustomer] = {
      runInTransaction { sess =>
        val ql = s"Select c from ${classOf[Customer].getName} c"

        val q = sess.createQuery(ql)
        val t = q.getResultList.asScala.map(_.asInstanceOf[Customer])
        t.foreach { c => println(AuthHelpers.hash2Str(c.getPassHash)) }
        val res = t.toArray.map(convertCustomer)
        res
      }
    }


    override def allProducts: Array[MProduct] =     {
      runInTransaction { sess =>
        val ql = s"Select p from ${classOf[Product].getName} p"

        val q = sess.createQuery(ql)
        val res = q.getResultList.asScala
          .toArray.map(c => convertProduct(c.asInstanceOf[Product]))
        res
      }
    }

    override def getUserPassMap: Map[String, Array[Byte]] = ???

    override def getUserPassMapJ: util.Map[String, Array[Byte]] = ???

    override def ordersOf(uid: String): Array[MOrder] = ???
  }

  val opsInstance:TDbOps = new OpsImpl
}
