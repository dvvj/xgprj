package org.xg.hbn

import org.hibernate.{Session, SessionFactory}
import org.xg.dbModels._
import org.xg.gnl.DataUtils
import org.xg.hbn.utils.HbnUtils

object Helpers {

  def runInTransaction(sessFactory:SessionFactory, action: Session => Unit):Unit = {
    val sess = sessFactory.getCurrentSession
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

  def runInTransaction[T](sessFactory:SessionFactory, f: Session => T):T = {
    val sess = sessFactory.getCurrentSession
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

  def testRunInTransaction[T](f: Session => T):T = {
    runInTransaction(HbnUtils.testSessFactory, f)
  }

  import org.xg.hbn.ent._

  def convertProduct(prod:Product, asset:Asset):MProduct = {
    val assetItems = AssetCfg.assetsFromJsons(asset.getAssetJson)
    MProduct(prod.getId, prod.getName, prod.getPrice0, prod.getDetailedInfo, prod.getKeywords, AssetCfg(prod.getId, assetItems))
  }

  def convertCustomer(c:Customer):MCustomer = {
    MCustomer(c.getUid, c.getName, c.getIdCardNo, c.getMobile, c.getPostalAddr, c.getBday, c.getRefUid)
  }

  def convertPricePlan(pp:PricePlan):MPricePlan = {
    MPricePlan(pp.getId, pp.getInfo, pp.getDefi, pp.getVtag)
  }

  def convertMedProf(mp:MedProf):MMedProf = {
    MMedProf(mp.getProfId, mp.getName,mp.getIdCardNo, mp.getMobile)
  }

  import DataUtils._
  def convertOrder(o:Order):MOrder = {
    MOrder.createJ(
      o.getId, o.getCustomerId, o.getProductId, o.getQty,
      zonedDateTime2Str(o.getCreationTime),
      zonedDateTime2Str(o.getPayTime),
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
