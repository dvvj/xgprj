package org.xg.uiDataModels

import javafx.collections.{FXCollections, ObservableList}
import org.xg.dbModels.{MOrder, MProduct}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.Order

class CustomerDM(
                orders:Array[MOrder],
                products:Array[MProduct],
                pricePlan: TPricePlan,
                statusStrMap:Map[Int, String]
                ) {

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

  private def createProducts(raw:Array[Product]):ObservableList[Product] = {
    FXCollections.observableArrayList(raw.toSeq.asJava)
  }

//
//  private var _products:ObservableList[Product] = createProducts(products)

}
