package org.xg.pay.rewardPlan

import org.xg.json.CommonUtils
import org.xg.pay.rewardPlan.v1.{RwPlFixedRate, RwPlProdBasedRates}


object RewardPlanSettings {
  object VTag extends Enumeration {
    type VTag = Value

    val FixedRate, ProductBasedRates = Value
  }

  import VTag._

  type RewardPlanDecoder = String => TRewardPlan

  private val FixedRateDecoder:RewardPlanDecoder = j => {
    CommonUtils._fromJson[RwPlFixedRate](j)
  }
  private val ProductBasedRatesDecoder:RewardPlanDecoder = j => {
    CommonUtils._fromJson[RwPlProdBasedRates](j)
  }

  private val planDecoderMap:Map[VTag, RewardPlanDecoder] = Map(
    FixedRate -> FixedRateDecoder,
    ProductBasedRates -> ProductBasedRatesDecoder
  )

  def decodePlan(vtag:VTag, planJson:String):TRewardPlan = {
    planDecoderMap(vtag)(planJson)
  }

}
