package org.xg.uiDataModels

import javafx.collections.{FXCollections, ObservableList}
import org.xg.dbModels.{MCustomerProfile, MOrder, MProduct}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.Order

class CustomerDM(
  profiles:Array[MCustomerProfile],
  orders:Array[MOrder],
  products:Array[MProduct],
  pricePlan: TPricePlan,
  statusStrMap:Map[Int, String]
) {

  private var product2PricePlan:Map[Int, String] = {
    profiles.flatMap(pf => pf.getDetail.productIds.map(_ -> pf.getDetail.pricePlanId)).toMap
  }

  import DataTransformers._
  private val productMap = getProductMapJ(products, pricePlan)
  import collection.JavaConverters._
  private def createOrders(raw:Array[MOrder]):ObservableList[Order] = {
    val ords = raw.map(mo => Order.fromMOrder(mo, productMap.asJava))
    ords.foreach(o => o.setStatusStr(statusStrMap(o.getStatus)))
    FXCollections.observableArrayList(ords.toSeq.asJava)
  }

  private var _orders:ObservableList[Order] = createOrders(orders)
  def getOrders:ObservableList[Order] = _orders

  def updateOrders(newOrders:Array[MOrder]):Unit = {
    _orders = createOrders(newOrders)
  }

  import org.xg.uiModels.{Product => UiProduct}
//  private def createProducts(raw:Array[MProduct]):ObservableList[Product] = {
//    val prods = raw.map(mp => UiProduct.fromMProduct())
//    FXCollections.observableArrayList(raw.toSeq.asJava)
//  }
//
//  private var _accessibleProducts:ObservableList[Product] = createProducts(products)

//
//  private var _products:ObservableList[Product] = createProducts(products)

}
