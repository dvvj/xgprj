package org.xg.db

import java.nio.charset.StandardCharsets
import java.sql.{Connection, DriverManager}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.xg.auth.AuthHelpers
import org.xg.db.impl.{DbOpsImpl, Utils}
import org.xg.gnl.GlobalCfg

object ConnectorTests extends App {



  def traceAllProducts(conn:Connection):Unit = {
    val stm = conn.prepareStatement(
      "SELECT * from products"
    )

    val res = stm.executeQuery()
    while (res.next()) {
      // res.getString("name") //
      val prd = res.getString("name")
      val id = res.getInt("id")
      val price0 = res.getFloat("price0")
      println(s"$id\t$prd\t$price0")
    }
  }

  def traceAllCustomers(conn:Connection):Unit = {
    val stm = conn.prepareStatement(
      "SELECT * from customers"
    )

    val res = stm.executeQuery()
    while (res.next()) {
      // res.getString("name") //
      val prd = res.getString("name")
      val id = res.getString("uid")
      val passHash = res.getBytes("pass_hash")
      val hashStr = AuthHelpers.hash2Str(passHash)
      println(s"$id\t$prd\t$hashStr")
    }
  }


  def tryInsertCustomer(conn:Connection):Unit = {
    val ops = DbOpsImpl.jdbcImpl(conn)
    val dt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

    val user = "added_customer4"
    val customerId = ops.addNewCustomer(
      user,
      "王丽",
      "dkfd",
      "323102003434123",
      "18623234322",
      "大街",
      "test_ref",
      dt
    )

    if (customerId != null) {
      println(s"user [$user] inserted")
    }
    else println(s"Failed to insert user [$user]")
  }

  val conn = Utils.tryConnect(GlobalCfg.localTestCfg.infoDbConnStr)

  println(s"Connected: ${conn.getCatalog}")
  //traceAllProducts(conn)

//  tryInsertCustomer(conn)

  traceAllCustomers(conn)

  conn.close()

}
