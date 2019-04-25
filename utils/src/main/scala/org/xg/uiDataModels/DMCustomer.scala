package org.xg.uiDataModels

import javafx.collections.{FXCollections, ObservableList}
import org.xg.dbModels._
import org.xg.pay.pricePlan.TPricePlan
import org.xg.uiModels.{CustomerProduct, Order, UIProduct}

trait TDMCustomer {
  def getProducts:ObservableList[CustomerProduct]

  def getOrders:ObservableList[Order]
}

object DMCustomer {
  private class CustomerDM (
    profiles:Array[MCustomerProfile],
    orders:Array[MOrder],
    products:Array[MProduct],
    pricePlans:Array[MPricePlan],
    medprofs:Array[MMedProf],
    statusStrMap:Map[Int, String]
  ) extends TDMCustomer {

    private val accessibleProducts = profiles.flatMap(_.getDetail.productIds).toSet
    private val referringProfs = profiles.map(_.profId).toSet

    private def checkNoProductOverlapInProfiles():Unit = {
      // make sure no overlapping of products in profiles
      import collection.mutable
      val productIds = mutable.Set[Int]()
      profiles.foreach { prf =>
        assert(!prf.getDetail.productIds.exists(productIds.contains))
        productIds ++= prf.getDetail.productIds
      }
    }

    private val medprofMap:Map[String, MMedProf] = medprofs
      .filter(mp => referringProfs.contains(mp.profId))
      .map(mp => mp.profId -> mp).toMap
    private val prod2Profs = profiles.flatMap(pr => pr.getDetail.productIds.map(_ -> medprofMap(pr.profId))).toMap

    private val product2PricePlan:Map[Integer, TPricePlan] = {
      checkNoProductOverlapInProfiles()
      val pricePlanMap = pricePlans.map(pp => pp.id -> pp.getPlan).toMap
      profiles.flatMap(pf => pf.getDetail.productIds.map { pid =>
        val prodIdJ = new Integer(pid)
        val plan = pricePlanMap(pf.getDetail.pricePlanId)
        prodIdJ -> plan
      }).toMap
    }
    import collection.JavaConverters._
    import DataTransformers._
    private val productMap:Map[Integer, CustomerProduct] = getProductMapJ(
      products.filter(p => accessibleProducts.contains(p.id)),
      product2PricePlan,
      prod2Profs,
    )
    private val _products:ObservableList[CustomerProduct] = {
      val seq = productMap.values
        .toSeq.sortBy(_.getProduct.getId)
      FXCollections.observableArrayList(seq.asJava)
    }
    def getProducts:ObservableList[CustomerProduct] = _products

    private def createOrders(raw:Array[MOrder]):ObservableList[Order] = {
      val ords = raw.map(mo => Order.fromCustomerOrder(mo, productMap.asJava))
      ords.foreach(o => o.setStatusStr(statusStrMap(o.getStatus)))
      FXCollections.observableArrayList(ords.toSeq.asJava)
    }

    private val _orders:ObservableList[Order] = createOrders(orders)
    def getOrders:ObservableList[Order] = _orders

    //  def updateOrders(newOrders:Array[MOrder]):Unit = {
    //    _orders = createOrders(newOrders)
    //  }

    import org.xg.uiModels.{UIProduct => UiProduct}
    //  private def createProducts(raw:Array[MProduct]):ObservableList[UIProduct] = {
    //    val prods = raw.map(mp => UiProduct.fromMProduct())
    //    FXCollections.observableArrayList(raw.toSeq.asJava)
    //  }
    //
    //  private var _accessibleProducts:ObservableList[UIProduct] = createProducts(products)

    //
    //  private var _products:ObservableList[UIProduct] = createProducts(products)

  }

  def create(
    profiles:Array[MCustomerProfile],
    orders:Array[MOrder],
    products:Array[MProduct],
    pricePlans:Array[MPricePlan],
    medprofs:Array[MMedProf],
    statusStrMap:Map[Int, String]
  ):TDMCustomer = new CustomerDM(profiles, orders, products, pricePlans, medprofs, statusStrMap)
}