package org.xg.uiDataModels

import javafx.collections.{FXCollections, ObservableList}
import org.xg.dbModels.{MCustomerProfile, MOrder, MPricePlan, MProduct}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.Order

class CustomerDM(
  profiles:Array[MCustomerProfile],
  orders:Array[MOrder],
  products:Array[MProduct],
  pricePlans:Array[MPricePlan],
//  pricePlan: TPricePlan,
  statusStrMap:Map[Int, String]
) {

  private def checkNoProductOverlapInProfiles():Unit = {
    // make sure no overlapping of products in profiles
    import collection.mutable
    val productIds = mutable.Set[Int]()
    profiles.foreach { prf =>
      assert(!prf.getDetail.productIds.exists(productIds.contains))
      productIds ++= prf.getDetail.productIds
    }
  }

  private val product2PricePlan:Map[Integer, TPricePlan] = {
    checkNoProductOverlapInProfiles()
    val pricePlanMap = pricePlans.map(pp => pp.id -> pp.getPlan).toMap
    profiles.flatMap(pf => pf.getDetail.productIds.map { pid =>
      val prodIdJ = new Integer(pid)
      val plan = pricePlanMap(pf.getDetail.pricePlanId)
      prodIdJ -> plan
    }).toMap
  }

  import DataTransformers._
  private val productMap = getProductMapJ(products, product2PricePlan)
  import collection.JavaConverters._
  private def createOrders(raw:Array[MOrder]):ObservableList[Order] = {
    val ords = raw.map(mo => Order.fromMOrder(mo, productMap.asJava))
    ords.foreach(o => o.setStatusStr(statusStrMap(o.getStatus)))
    FXCollections.observableArrayList(ords.toSeq.asJava)
  }

  private val _orders:ObservableList[Order] = createOrders(orders)
  def getOrders:ObservableList[Order] = _orders

//  def updateOrders(newOrders:Array[MOrder]):Unit = {
//    _orders = createOrders(newOrders)
//  }

  import org.xg.uiModels.{UIProduct => UiProduct}
//  private def createProducts(raw:Array[MProduct]):ObservableList[UIProduct] = {
//    val prods = raw.map(mp => UiProduct.fromMProduct())
//    FXCollections.observableArrayList(raw.toSeq.asJava)
//  }
//
//  private var _accessibleProducts:ObservableList[UIProduct] = createProducts(products)

//
//  private var _products:ObservableList[UIProduct] = createProducts(products)

}
