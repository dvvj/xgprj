package org.xg.pay.rewordPlan

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import org.xg.pay.pricePlan.v1.{PrPlFixedRate, PrPlProdBasedRates}
import org.xg.pay.rewardPlan.v1.{RwPlFixedRate, RwPlProdBasedRates}

class V1RewardPlanTests extends TestNGSuite with Matchers with TableDrivenPropertyChecks {
  private val fixedPlan = RwPlFixedRate(0.01)
  private val prodBasedPlan = RwPlProdBasedRates(
    0.01,
    Map(
      1 -> 0.02,
      2 -> 0.03
    )
  )

  private val ProductId_NA = -1
  private val testData = Table(
    ("plan", "prodId", "price0", "expPrice"),
    ( fixedPlan, ProductId_NA, 100.0, 1.0 ),
    ( prodBasedPlan, 1, 100.0, 2.0 ),
    ( prodBasedPlan, 2, 100.0, 3.0 ),
    ( prodBasedPlan, 3, 100.0, 1.0 )
  )

  import org.xg.TestUtils._
  @Test
  def testPlans():Unit = {
    forAll (testData) { (plan, prodId, price0, expPrice) =>
      val actPrice = plan.reward(prodId, price0)
      checkDoubleEquals(actPrice, expPrice) shouldBe true
    }
  }
}
