package org.xg.pay.pricePlan

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import org.xg.pay.pricePlan.v1.{PrPlChained, PrPlFixedRate, PrPlProdBasedRates}

class V1PricePlanTests extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val fixedPlan = PrPlFixedRate(0.85)
  private val prodBasedPlan = PrPlProdBasedRates(
    0.9,
    Map(
      1 -> 0.8,
      2 -> 0.85
    )
  )
  private val chainedPlan = PrPlChained.create(
    List(prodBasedPlan, PrPlFixedRate(0.9))
  )

  private val ProductId_NA = -1
  private val testData = Table(
    ("plan", "prodId", "price0", "expPrice"),
    ( fixedPlan, ProductId_NA, 10.0, 8.5 ),
    ( prodBasedPlan, 1, 10.0, 8.0 ),
    ( prodBasedPlan, 2, 10.0, 8.5 ),
    ( prodBasedPlan, 3, 10.0, 9.0 ),
    ( chainedPlan, 1, 10.0, 7.2 ),
    ( chainedPlan, 3, 10.0, 8.1 )
  )

  import org.xg.TestUtils._
  @Test
  def testPrPls():Unit = {
    forAll(testData) { (plan, prodId, price0, expPrice) =>
      val actPrice = plan.adjust(prodId, price0)
      checkDoubleEquals(actPrice, expPrice) shouldBe true
    }
  }
}
