package org.xg.ui.utils

import java.util.concurrent.TimeoutException
import java.util.function.Supplier

import javafx.application.Platform
import javafx.concurrent.Task
import org.xg.dbModels._
import org.xg.pay.pricePlan.TPricePlan
import org.xg.pay.rewardPlan.TRewardPlan
import org.xg.ui.model._
import org.xg.uiModels
import org.xg.uiModels._

import scala.concurrent.{Await, Future}

object Helpers {
  def convProducts(mps:Array[MProduct], pricePlan:TPricePlan):Array[uiModels.Product] = {
    mps.map(mp => Product.fromMProduct(mp, pricePlan))
  }

  def convCustomers(mcs:Array[MCustomer], planMap:java.util.Map[String, MPricePlan]):Array[Customer] = {
    mcs.map(c => Customer.fromMCustomer(c, planMap))
  }

  def convMedProfs(mcs:Array[MMedProf]):Array[MedProf] = {
    mcs.map(MedProf.fromMMedProf)
  }

  def convOrgAgentOrderStats(
                         os:Array[MOrgAgentOrderStat],
                         profMap:java.util.Map[String, MedProf],
                         rewardPlan: TRewardPlan
                       ):Array[OrgAgentOrderStat] = {
    os.map(o => OrgAgentOrderStat.fromM(o, Global.getProductMap, profMap, rewardPlan))
  }

  def convOrgOrderStats(
    os:Array[MOrgOrderStat],
    agentMap:java.util.Map[String, ProfOrgAgent],
    rewardPlanMap: java.util.Map[String, TRewardPlan]
  ):Array[OrgOrderStat] = {
    os.map(o => OrgOrderStat.fromM(o, Global.getProductMap, agentMap, rewardPlanMap.get(o.orgAgentId)))
  }


  def convOrders(morders:Array[MOrder], prodMap: java.util.Map[Integer, Product]):Array[Order] = {
    morders.map(mo => Order.fromMOrder(mo, prodMap))
  }

  def convCustomerOrders(
                          morders:Array[MOrder],
                          customerMap: java.util.Map[String, Customer],
                          prodMap: java.util.Map[Integer, Product],
                          rewardPlan:TRewardPlan
                        ):Array[CustomerOrder] = {
    morders.map(mo => CustomerOrder.fromMOrder(mo, customerMap, Global.getProductMap, rewardPlan))
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

  type TaskAction = () => Any
  def paraActions(
    suppliers:Array[Supplier[Any]],
    timeoutMs:Int // ms
  ):Array[Any] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._
    try {
      val fs = suppliers.map(supplier => Future { supplier.get() })
      val futureAll = Future.sequence(fs.toSeq)
      //        println("waiting ...")
      Await.result(futureAll, timeoutMs millis).toArray
      //        println("done waiting")
      //msg = successMsg
    }
    catch {
      case to:TimeoutException => {
        //msg = timeoutMsg
        to.printStackTrace()
        // todo: log
        throw new RuntimeException("Timeout", to)
      }
      case t:Throwable => {
        //msg = unknownErrorMsg
        t.printStackTrace()
        // todo: log
        throw new RuntimeException("Other error", t)
      }
    }
  }

  import collection.JavaConverters._
  def productMapFromJ(products:Array[Product]):java.util.Map[Integer, Product] = {
    products.map(p => p.getId -> p).toMap.asJava
  }

  private val countryCode2ResName = Map(
    "SV" -> "srcCountry.Sweden",
    "US" -> "srcCountry.US",
    "CA" -> "srcCountry.Canada",
    "BR" -> "srcCountry.Britain",
    "AU" -> "srcCountry.Australia",
    "NZ" -> "srcCountry.NewZealand",
  )

  def srcCountryResKey(countryCode:String):String = countryCode2ResName(countryCode)

  def calcReward(rewardPlan:TRewardPlan, prodId:Integer, prodPrice0:Double): Double = {
    if (rewardPlan != null)
      rewardPlan.reward(prodId, prodPrice0)
    else 0.0
  }

  def calcRewards(rewardPlan:TRewardPlan, orders: Array[CustomerOrder], prodMap:java.util.Map[Integer, Product]): Double = {
    orders.map(order => calcReward(rewardPlan, order.getOrder.getProdId, prodMap.get(order.getOrder.getProdId).getPrice0)).sum
  }

  def calcRewards(rewardPlan:TRewardPlan, orders: Array[OrgAgentOrderStat], prodMap:java.util.Map[Integer, Product]): Double = {
    orders.map(os => calcReward(rewardPlan, os.getProductId, prodMap.get(os.getProductId).getPrice0)).sum
  }

  def calcRewards(rewardPlans:java.util.Map[String, TRewardPlan], orders: Array[OrgOrderStat], prodMap:java.util.Map[Integer, Product]): Double = {
    orders.map(os => calcReward(rewardPlans.get(os.getAgentOrderStat.getOrgAgentId), os.getAgentOrderStat.getProductId, prodMap.get(os.getAgentOrderStat.getProductId).getPrice0)).sum
  }
}
