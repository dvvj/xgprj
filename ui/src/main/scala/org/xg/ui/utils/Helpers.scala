package org.xg.ui.utils

import javafx.application.Platform
import javafx.concurrent.Task
import org.xg.db.model.MProduct
import org.xg.ui.model.Product

object Helpers {
  def convProducts(mps:Array[MProduct]):Array[Product] = {
    mps.map(Product.fromMProduct)
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
}
