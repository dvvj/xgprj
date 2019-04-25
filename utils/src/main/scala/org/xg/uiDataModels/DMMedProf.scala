package org.xg.uiDataModels

import javafx.collections.ObservableList
import org.xg.uiModels.{Customer, CustomerOrder, PricePlanOption, UIProduct}

trait TDMMedProf {
  def getCustomers:ObservableList[Customer]
  def getProducts:ObservableList[UIProduct]
  def getCustomerOrders:ObservableList[CustomerOrder]
  def getPricePlanOptions:ObservableList[PricePlanOption]
  def getOrderData:Array[CustomerOrder]
}

object DMMedProf {

}
