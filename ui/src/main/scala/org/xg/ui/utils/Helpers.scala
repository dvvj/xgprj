package org.xg.ui.utils

import javafx.application.Platform
import javafx.concurrent.Task
import org.xg.db.model.{MOrder, MProduct}
import org.xg.ui.model.{Order, Product}

object Helpers {
  def convProducts(mps:Array[MProduct]):Array[Product] = {
    mps.map(Product.fromMProduct)
  }

  def convOrders(morders:Array[MOrder], prodMap: java.util.Map[Integer, Product]):Array[Order] = {
    morders.map(mo => Order.fromMOrder(mo, prodMap))
  }

  def statusTaskJ[T](action:() => T, successMsg:String, uiUpdater:T => Unit):Task[T] = new Task[T]() {
    override def call(): T = {
      val res = action()
      updateMessage(successMsg)
      if (uiUpdater != null) {
        val runnable = new Runnable {
          override def run(): Unit = {
            uiUpdater(res)
          }
        }
        Platform.runLater(runnable)
      }

      res
    }
  }

  import collection.JavaConverters._
  def productMapFromJ(products:Array[Product]):java.util.Map[Integer, Product] = {
    products.map(p => p.getId -> p).toMap.asJava
  }
}
