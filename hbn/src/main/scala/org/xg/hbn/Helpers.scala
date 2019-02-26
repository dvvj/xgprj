package org.xg.hbn

import org.hibernate.Session
import org.xg.db.model.{MCustomer, MProduct}
import org.xg.hbn.utils.HbnUtils

object Helpers {

  def runInTransaction(action: Session => Unit):Unit = {
    try {
      val sess = HbnUtils.sessFactory.getCurrentSession
      val tr = sess.beginTransaction()
      action(sess)
      tr.commit()
    }
    catch {
      case t:Throwable => {
        //        t.printStackTrace()
        throw t
      }
    }
  }

  def runInTransaction[T](f: Session => T):T = {
    try {
      val sess = HbnUtils.sessFactory.getCurrentSession
      val tr = sess.beginTransaction()
      val res = f(sess)
      tr.commit()
      res
    }
    catch {
      case t:Throwable => {
        //        t.printStackTrace()
        throw t
      }
    }
  }

  import org.xg.hbn.ent._

  def convertProduct(prod:Product):MProduct = {
    MProduct(prod.getId, prod.getName, prod.getPrice0, prod.getDetailedInfo, prod.getKeywords)
  }

  def convertCustomer(c:Customer):MCustomer = {
    MCustomer(c.getUid, c.getName, c.getIdCardNo, c.getMobile, c.getPostalAddr, c.getBday, c.getRefUid)
  }
}
