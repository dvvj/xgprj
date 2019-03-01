package org.xg.hbn

import org.hibernate.Session
import org.xg.db.model._
import org.xg.gnl.DataUtils
import org.xg.hbn.utils.HbnUtils

object Helpers {

  def runInTransaction(action: Session => Unit):Unit = {
    val sess = HbnUtils.sessFactory.getCurrentSession
    val tr = sess.beginTransaction()
    try {
      action(sess)
      tr.commit()
    }
    catch {
      case t:Throwable => {
        tr.rollback()
        //        t.printStackTrace()
        throw t
      }
    }
  }

  def runInTransaction[T](f: Session => T):T = {
    val sess = HbnUtils.sessFactory.getCurrentSession
    val tr = sess.beginTransaction()
    try {
      val res = f(sess)
      tr.commit()
      res
    }
    catch {
      case t:Throwable => {
        tr.rollback()
        //        t.printStackTrace()
        throw t
      }
    }
  }

  import org.xg.hbn.ent._

  def convertProduct(prod:Product, asset:Asset):MProduct = {
    val assetItems = AssetCfg.assetsFromJsons(asset.getAssetJson)
    MProduct(prod.getId, prod.getName, prod.getPrice0, prod.getDetailedInfo, prod.getKeywords, AssetCfg(prod.getId, assetItems))
  }

  def convertCustomer(c:Customer):MCustomer = {
    MCustomer(c.getUid, c.getName, c.getIdCardNo, c.getMobile, c.getPostalAddr, c.getBday, c.getRefUid)
  }

  import DataUtils._
  def convertOrder(o:Order):MOrder = {
    MOrder(
      o.getId, o.getCustomerId, o.getProductId, o.getQty,
      zonedDateTime2Str(o.getCreationTime),
      zonedDateTime2Str(o.getProcTime1),
      zonedDateTime2Str(o.getProcTime2),
      zonedDateTime2Str(o.getProcTime3)
    )
  }

  def convertOrderHistory(oh:OrderHistory):MOrderHistory = {
    MOrderHistory(
      oh.getOrderId, zonedDateTime2Str(oh.getUpdateTime), oh.getOldQty
    )
  }

  def loggingTodo(msg:String):Unit = {
    println(msg)
  }
}
