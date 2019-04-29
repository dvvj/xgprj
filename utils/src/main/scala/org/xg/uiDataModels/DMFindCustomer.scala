package org.xg.uiDataModels

import org.xg.dbModels.{MCustomer, MCustomerProfile}
import org.xg.uiDataModels.DataLoaders.TDataLoader

class DMFindCustomer(
  val customer:MCustomer,
  val profiles:Array[MCustomerProfile]
) {

}

object DMFindCustomer {

}