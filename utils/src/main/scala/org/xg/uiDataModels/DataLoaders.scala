package org.xg.uiDataModels

import java.util.concurrent.TimeoutException

import org.xg.auth.SvcHelpers
import org.xg.dbModels._
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

  def customerDataLoader(serverCfg:GlobalCfg, currToken:String, statusStrMap:Map[Int, String]): TDataLoader[TDMCustomer] =
    new TDataLoader[TDMCustomer] {
    override val cfg: GlobalCfg = serverCfg
    override val dataRetrievers: Seq[DataRetriever] = Seq(
      () => SvcHelpers.getDecArray(serverCfg.customerProfilesURL, currToken, MCustomerProfile.fromJsons),
      () => SvcHelpers.getDecArray(serverCfg.currOrdersURL, currToken, MOrder.fromJsons),
      () => SvcHelpers.getDecArray(serverCfg.allProductsURL, currToken, MProduct.fromJsons),
      () => SvcHelpers.getDecArray(serverCfg.allPricePlansURL, currToken, MPricePlan.fromJsons),
      () => SvcHelpers.getDecArray(serverCfg.customerProfsURL, currToken, MMedProf.fromJsons)
      //,() => SvcHelpers.getPricePlan4UserJ(serverCfg.pricePlanURL, currToken)
    )

    override protected def construct(rawData: Array[AnyRef]): TDMCustomer = {
      DMCustomer.create(
        rawData(0).asInstanceOf[Array[MCustomerProfile]],
        rawData(1).asInstanceOf[Array[MOrder]],
        rawData(2).asInstanceOf[Array[MProduct]],
        rawData(3).asInstanceOf[Array[MPricePlan]],
        rawData(4).asInstanceOf[Array[MMedProf]],
        //rawData(3).asInstanceOf[TPricePlan],
        statusStrMap
      )
    }
  }

  def findCustomerDataLoader(serverCfg:GlobalCfg, currToken:String, customerId:String): TDataLoader[DMFindCustomer] =
    new TDataLoader[DMFindCustomer] {
      override val cfg: GlobalCfg = serverCfg

      override protected def dataRetrievers: Seq[DataRetriever] = Seq(
        () => {
          val j = SvcHelpers.post(
            serverCfg.customerByIdURL,
            currToken,
            customerId
          )
          if (j.isEmpty) null
          else MCustomer.fromJson(j)
        },
        () => SvcHelpers.postDecArray(serverCfg.existingCustomerProfileURL, currToken, customerId, MCustomerProfile.fromJsons)
      )

      override protected def construct(rawData: Array[AnyRef]): DMFindCustomer = {
        val customer:MCustomer = if (rawData(0) != null) rawData(0).asInstanceOf[MCustomer] else null
        new DMFindCustomer(
          rawData(0).asInstanceOf[MCustomer],
          rawData(1).asInstanceOf[Array[MCustomerProfile]]
        )
      }
    }

  import collection.JavaConverters._
  def customerDataLoaderJ(
                           serverCfg:GlobalCfg,
                           currToken:String,
                           statusStrMap:java.util.Map[Integer, String]
                         ): TDataLoader[TDMCustomer] =
    customerDataLoader(serverCfg, currToken, statusStrMap.asScala.map(p => p._1.toInt -> p._2).toMap)

}
