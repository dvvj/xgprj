package org.xg.uiDataModels

import java.util.concurrent.TimeoutException

import org.xg.auth.SvcHelpers
import org.xg.dbModels.{MCustomerProfile, MOrder, MProduct}
import org.xg.gnl.GlobalCfg
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.Order

import scala.concurrent.{Await, Future}

object DataLoaders {

  type DataRetriever = () => AnyRef

  trait TDataLoader[T] {
    val cfg:GlobalCfg
    protected def dataRetrievers:Seq[DataRetriever]
    protected def load(timeout:Int):Array[AnyRef] = {
      import scala.concurrent.ExecutionContext.Implicits.global
      import scala.concurrent.duration._
      try {
        val fs = dataRetrievers.map(dr => Future { dr.apply() } )
        val futureAll = Future.sequence(fs)
        //        println("waiting ...")
        Await.result(futureAll, timeout millis).toArray
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
    protected def construct(rawData:Array[AnyRef]):T
    def loadAndConstruct(timeout:Int):T = {
      val rd = load(timeout)
      construct(rd)
    }
  }

  def customerDataLoader(serverCfg:GlobalCfg, currToken:String, statusStrMap:Map[Int, String]): TDataLoader[CustomerDM] = new TDataLoader[CustomerDM] {
    override val cfg: GlobalCfg = serverCfg
    override val dataRetrievers: Seq[DataRetriever] = Seq(
      () => SvcHelpers.getDecArray(serverCfg.customerProfilesURL, currToken, MCustomerProfile.fromJsons),
      () => SvcHelpers.getDecArray(serverCfg.currOrdersURL, currToken, MOrder.fromJsons),
      () => SvcHelpers.getDecArray(serverCfg.allProductsURL, currToken, MProduct.fromJsons),
      () => SvcHelpers.getPricePlan4UserJ(serverCfg.pricePlanURL, currToken)
    )

    override protected def construct(rawData: Array[AnyRef]): CustomerDM = {
      new CustomerDM(
        rawData(0).asInstanceOf[Array[MCustomerProfile]],
        rawData(1).asInstanceOf[Array[MOrder]],
        rawData(2).asInstanceOf[Array[MProduct]],
        rawData(3).asInstanceOf[TPricePlan],
        statusStrMap
      )
    }
  }

  import collection.JavaConverters._
  def customerDataLoaderJ(serverCfg:GlobalCfg, currToken:String, statusStrMap:java.util.Map[Integer, String]): TDataLoader[CustomerDM] =
    customerDataLoader(serverCfg, currToken, statusStrMap.asScala.map(p => p._1.toInt -> p._2).toMap)

}
