package org.xg.dbUtils

object InsertCustomerProfileUtil {

  private val insertStatementTemplate =
    "INSERT INTO customer_profiles (prof_id, customer_id, detailed_info, version)" +
      "  VALUES (%s);"

  private val Ver = "1.00"
  import TestDataSet._
  import TestDataSet.PricePlans._
  private val profileData = Array(
    Array(
      profId111, customerId1111,
      "{  \"productIds\": [1, 2], \"pricePlanId\": \"%s\", \"creationTime\": \"2018-08-12 18:30:44\"}".format(PrPlanIdFixed09_P111),
      Ver
    ),
    Array(
      profId111, customerId1112,
      "{  \"productIds\": [1, 2], \"pricePlanId\": \"%s\",  \"creationTime\": \"2018-08-11 18:30:44\"}".format(PrPlanIdFixed09_P111),
      Ver
    ),
    Array(
      profId111, customerId1114,
      "{  \"productIds\": [1], \"pricePlanId\": \"%s\",  \"creationTime\": \"2018-08-14 18:30:44\"}".format(PrPlanIdFixed09_P111),
      Ver
    ),
    Array(
      profId112, customerId1111,
      "{  \"productIds\": [3],  \"pricePlanId\": \"%s\",  \"creationTime\": \"2018-08-12 13:30:44\"}".format(PrPlanIdFixed095),
      Ver
    ),
    Array(
      profId112, customerId1121,
      "{  \"productIds\": [3, 4],  \"pricePlanId\": \"%s\",  \"creationTime\": \"2018-07-12 13:30:44\"}".format(PrPlanIdFixed095),
      Ver
    ),
  )

  def main(args:Array[String]):Unit = {
    val sttms = profileData.map { d =>
      val vs = d.mkString("'", "','", "'")
      insertStatementTemplate.format(vs)
    }

    println(sttms.mkString("\n"))
  }

}
