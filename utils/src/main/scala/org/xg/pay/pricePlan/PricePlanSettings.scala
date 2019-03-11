package org.xg.pay.pricePlan

import org.xg.json.CommonUtils
import org.xg.pay.pricePlan.v1.{PrPlFixedRate, PrPlProdBasedRates}

object PricePlanSettings {

  object VTag extends Enumeration {
    type VTag = Value

    val FixedRate, ProductBasedRates = Value
  }

  import VTag._

  type PricePlanDecoder = String => TPricePlan

  private val FixedRateDecoder:PricePlanDecoder = j => {
    CommonUtils._fromJson[PrPlFixedRate](j)
  }
  private val ProductBasedRatesDecoder:PricePlanDecoder = j => {
    CommonUtils._fromJson[PrPlProdBasedRates](j)
  }

  private val planDecoderMap:Map[VTag, PricePlanDecoder] = Map(
    FixedRate -> FixedRateDecoder,
    ProductBasedRates -> ProductBasedRatesDecoder
  )

  def decodePlan(vtag:VTag, planJson:String):TPricePlan = {
    planDecoderMap(vtag)(planJson)
  }
}
