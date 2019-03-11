package org.xg.busiLogic

import java.time.LocalDateTime

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import org.xg.dbModels.MPricePlanMap

class PricePlanLogicsTest extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val prof1 = "prof1"
  private val prof1Plan1 = MPricePlanMap(prof1, "plan1", "2019-03-01T16:00:28", None)
  private val prof1Plan2 = MPricePlanMap(prof1, "plan2", "2019-03-04T16:00:28", Option("2019-03-08T16:00:28")) // expired
  private val prof2 = "prof2"
  private val prof2Plan1 = MPricePlanMap(prof2, "plan1", "2019-03-04T16:00:28", None) // overwritten
  private val prof2Plan2 = MPricePlanMap(prof2, "plan2", "2019-03-06T16:00:28", None)
  private val prof3 = "prof3"
  private val prof3Plan1 = MPricePlanMap(prof3, "plan1", "2019-03-04T16:00:28", Option("2019-03-08T16:00:28")) // expired
  private val prof4 = "prof4"
  private val prof4Plan1 = MPricePlanMap(prof4, "plan1", "2019-03-01T16:00:28", None)
//  private val prof5 = "prof5" // time at CET
//  private val prof5Plan1 = MPricePlanMap(prof5, "plan1", "2019-03-01T16:00:28", Option(zoned))

  private val testData = Table(
    ("testPlans", "expActivePlanMap"),
    (
      Array(prof1Plan1, prof1Plan2),
      Map(
        prof1 -> prof1Plan1
      )
    ),
    (
      Array(prof1Plan1, prof1Plan2, prof2Plan1, prof2Plan2),
      Map(
        prof1 -> prof1Plan1,
        prof2 -> prof2Plan2
      )
    ),
    (
      Array(prof1Plan1, prof1Plan2, prof2Plan1, prof2Plan2, prof3Plan1),
      Map(
        prof1 -> prof1Plan1,
        prof2 -> prof2Plan2
      )
    ),
    (
      Array(prof1Plan1, prof1Plan2, prof2Plan1, prof2Plan2, prof3Plan1, prof4Plan1),
      Map(
        prof1 -> prof1Plan1,
        prof2 -> prof2Plan2,
        prof4 -> prof4Plan1
      )
    )
  )

  @Test
  def testActivePlan():Unit = {
    import PricePlanLogics._
    forAll (testData) { (testPlans, expActivePlanMap) =>
      val resMap = activePricePlans(testPlans)
      resMap shouldBe expActivePlanMap
    }
  }
}
