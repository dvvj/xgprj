package org.xg.ui.utils

import java.util.concurrent.TimeoutException

import javafx.application.Platform
import javafx.concurrent.Task
import org.xg.dbModels.{MCustomer, MOrder, MProduct}
import org.xg.pay.pricePlan.TPricePlan
import org.xg.ui.model.{Customer, Order, Product}

import scala.concurrent.{Await, Future}

object Helpers {
  def convProducts(mps:Array[MProduct], pricePlan:TPricePlan):Array[Product] = {
    mps.map(mp => Product.fromMProduct(mp, pricePlan))
  }

  def convCustomers(mcs:Array[MCustomer]):Array[Customer] = {
    mcs.map(Customer.fromMCustomer)
  }

  def convOrders(morders:Array[MOrder], prodMap: java.util.Map[Integer, Product]):Array[Order] = {
    morders.map(mo => Order.fromMOrder(mo, prodMap))
  }

  def uiTaskJ[T >: AnyRef](
    action:() => T,
    uiUpdater:T => Unit,
    timeoutMs:Int // ms
//    successMsg:String,
//    timeoutMsg:String,
//    unknownErrorMsg:String
  ):Task[T] = new Task[T]() {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    override def call(): T = {
      var res:T = null
      //var msg:String = null
      try {
        val f = Future {
          action()
        }
//        println("waiting ...")
        res = Await.result(f, timeoutMs millis)
//        println("done waiting")
        //msg = successMsg
      }
      catch {
        case to:TimeoutException => {
          //msg = timeoutMsg
          to.printStackTrace()
          // todo: log
          //throw new RuntimeException("error", to)
        }
        case t:Throwable => {
          //msg = unknownErrorMsg
          t.printStackTrace()
          // todo: log
          //throw new RuntimeException("error", t)
        }
      }

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
