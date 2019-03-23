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
      1L, customer3, 1, 1.0, 1499.99,
      "2019-02-11 19:30:44"
    ),
    MOrder.createJ(
      2L, customer1, 2, 3.0, 399.99,
      "2019-02-12 19:30:44", "2019-02-12 19:35:44"
    ),
    MOrder.createJ(
      3L, customer1, 3, 2.0, 19.99,
      "2019-03-11 19:30:44", "2019-03-11 19:35:44"
    ),
    MOrder.createJ(
      4L, customer1, 3, 4.0, 29.99,
      "2019-03-12 19:20:44"
    ),
    MOrder.createJ(
      5L, customer1, 3, 4.0, 29.99,
      "2019-03-12 19:30:44", "2019-03-12 19:35:44"
    ),
    MOrder.createJ(
      6L, customer2, 3, 10.0, 79.99,
      "2019-03-12 19:30:44", "2019-03-12 19:35:44"
    ),
    MOrder.createJ(
      7L, customer2, 1, 2.0, 2499.99,
      "2019-03-02 19:30:44", "2019-03-02 19:35:44"
    ),
    MOrder.createJ(
      8L, customer5, 3, 1.0, 4.99,
      "2019-03-11 19:30:44"
    ),
    MOrder.createJ(
      9L, customer4, 1, 10.0, 9999.99,
      "2019-02-11 19:30:44"
    )
  )
  val insertStatementTemplate1 =
    "INSERT INTO orders (id, customer_id, product_id, qty, actual_cost, creation_time, pay_time, proc_time1, proc_time2, proc_time3)" +
      "  VALUES (%s);"
  val insertStatementTemplate2 =
    "INSERT INTO org_order_stats (org_id, agent_id, order_id, product_id, qty, actual_cost, creation_time, status)" +
      "  VALUES (%s);"

  val NULL = "NULL"
  def main(args:Array[String]):Unit = {
    val insertStatements = testOrders.flatMap { mo =>
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
      val profId = customer2ProfMap(mo.uid)
      val profOrgId = prof2OrgMap(profId)
      params2 += s"'$profOrgId'"
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