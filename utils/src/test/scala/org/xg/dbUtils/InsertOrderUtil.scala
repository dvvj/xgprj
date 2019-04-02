package org.xg.dbUtils

import org.xg.dbModels.MOrder

import scala.collection.mutable.ListBuffer

object InsertOrderUtil {

  private val Status_Created = 1
  private val Status_Paid = 2
  private val Status_Cancelled = 3

  import InsertCustomersUtil._
  import InsertMedProfsUtil._

  val testOrders = Array(
    MOrder.createJ(
      1L, customerId2, 1, 2.0, 2499.99,
      "2018-10-02 19:30:44"
    ),
    MOrder.createJ(
      2L, customerId2, 1, 2.0, 2499.99,
      "2018-10-12 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      3L, customerId2, 1, 2.0, 2499.99,
      "2018-10-22 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      4L, customerId2, 1, 2.0, 2499.99,
      "2018-10-23 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      5L, customerId2, 1, 2.0, 2499.99,
      "2018-11-02 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      6L, customerId2, 1, 2.0, 2499.99,
      "2018-11-12 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      7L, customerId2, 1, 2.0, 2499.99,
      "2019-01-02 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      8L, customerId2, 1, 2.0, 2499.99,
      "2019-01-03 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      9L, customerId2, 1, 2.0, 2499.99,
      "2019-01-04 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      10L, customerId3, 1, 1.0, 1499.99,
      "2019-02-11 19:30:44"
    ),
    MOrder.createJ(
      11L, customerId1, 2, 3.0, 399.99,
      "2019-02-12 19:30:44", "2019-02-12 19:35:44"
    ),
    MOrder.createJ(
      12L, customerId1, 3, 2.0, 19.99,
      "2019-03-11 19:30:44", "2019-03-11 19:35:44"
    ),
    MOrder.createJ(
      13L, customerId1, 3, 2.0, 19.99,
      "2019-03-11 20:30:44", "2019-03-11 19:35:44"
    ),
    MOrder.createJ(
      14L, customerId1, 3, 4.0, 29.99,
      "2019-03-12 19:20:44"
    ),
    MOrder.createJ(
      15L, customerId1, 3, 4.0, 29.99,
      "2019-03-12 19:30:44", "2019-03-12 19:35:44"
    ),
    MOrder.createJ(
      16L, customerId2, 3, 10.0, 79.99,
      "2019-03-12 19:30:44", "2019-03-12 19:35:44"
    ),
    MOrder.createJ(
      17L, customerId2, 1, 2.0, 2499.99,
      "2019-03-02 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      18L, customerId5, 3, 1.0, 4.99,
      "2019-03-11 19:30:44"
    ),
    MOrder.createJ(
      19L, customerId4, 1, 10.0, 9999.99,
      "2019-02-11 19:30:44"
    )
  )
  val insertStatementTemplate1 =
    "INSERT INTO orders (id, customer_id, product_id, qty, actual_cost, creation_time, pay_time, proc_time1, proc_time2, proc_time3)" +
      "  VALUES (%s);"
  val insertStatementTemplate2 =
    "INSERT INTO org_agent_order_stats (org_agent_id, prof_id, order_id, product_id, qty, actual_cost, creation_time, status)" +
      "  VALUES (%s);"

  val NULL = "NULL"
  def main(args:Array[String]):Unit = {
    insertOrders(testOrders, customer2ProfMap, prof2OrgAgentMap)
  }

  def insertOrders(
                    orders:Array[MOrder],
                    customer2ProfIdMap:Map[String, String],
                    prof2OrgAgentIdMap:Map[String, String]
                  ):Unit = {
    val insertStatements = orders.flatMap { mo =>
      val params = ListBuffer[String]()

      params += mo.id.toString
      params += s"'${mo.uid}'"
      params += mo.productId.toString
      params += mo.qty.toString
      params += mo.actualCost.toString
      params += s"'${mo.creationTimeS}'"
      val payTime = if (mo.payTime.nonEmpty) s"'${mo.payTime.get}'" else NULL
      params += payTime
      params += NULL
      params += NULL
      params += NULL
      val statement1 = insertStatementTemplate1.format(params.mkString(","))

      val params2 = ListBuffer[String]()
      val profId = customer2ProfIdMap(mo.uid)
      val profOrgAgentId = prof2OrgAgentIdMap(profId)
      params2 += s"'$profOrgAgentId'"
      params2 += s"'$profId'"
      params2 += mo.id.toString
      params2 += mo.productId.toString
      params2 += mo.qty.toString
      params2 += mo.actualCost.toString
      params2 += s"'${mo.creationTimeS}'"
      val status = if (mo.payTime.nonEmpty) Status_Paid else Status_Created
      params2 += status.toString
      val statement2 = insertStatementTemplate2.format(params2.mkString(","))

      Array(statement1, statement2)
    }

    println(insertStatements.mkString("\n"))
  }

  // VALUES ('customer3', 1, 1.0, 1499.99, '2018-02-11 19:30:44', NULL, NULL, NULL, NULL);
}
