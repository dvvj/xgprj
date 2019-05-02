package org.xg.uiDataModels

import javafx.collections.ObservableList
import org.xg.dbModels.{MCustomer, MPricePlan}
import org.xg.uiModels._

trait TDMMedProf {
  def getCustomers:ObservableList[Customer]
  def getProducts:ObservableList[UIProduct]
  def getCustomerOrders:ObservableList[CustomerOrder]
  def getPricePlanOptions:ObservableList[PricePlanOption]
  def getPricePlans:ObservableList[PricePlan]
  def getOrderData:Array[CustomerOrder]

  def calcTotalReward:Double

  def setCustomersJ(customers:Array[MCustomer], pricePlans4Customers:java.util.Map[String, MPricePlan]):Unit
}

object DMMedProf {
//  private class MedProfDM extends TDMMedProf {
//
//  }
}
