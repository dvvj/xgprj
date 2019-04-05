package org.xg.gnl

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test

class CachedDataTests extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val testData = Table(
    ("retriever", "expected"),
    (
      () => Map(1 -> "1", 2 -> "2"),
      Map(1 -> "1", 2 -> "2")
    )
  )

  @Test
  def testGetData():Unit = {
    forAll (testData) { (retriever, expected) =>
      val c = new CachedData(retriever)
      val d = c.getData
      d shouldBe expected
    }
  }
}
