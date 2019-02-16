package org.xg.db

import java.nio.charset.StandardCharsets
import java.sql.{Connection, DriverManager}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.xg.db.impl.DbOpsImpl

object ConnectorTests extends App {

  def tryConnect(connStr:String):Connection = {
    try {
      val conn = DriverManager.getConnection(connStr)
      conn
    }
    catch {
      case t:Throwable => {
        t.printStackTrace()
        throw t
      }
    }
  }

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
      val id = res.getString("id")
      val bday = res.getString("bday")
      println(s"$id\t$prd\t$bday")
    }
  }


  def tryInsertCustomer(conn:Connection):Unit = {
    val ops = DbOpsImpl.jdbcImpl(conn)
    val dt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

    val user = "added_customer4"
    val success = ops.addNewCustomer(
      user,
      "王丽",
      "323102003434123",
      "18623234322",
      "大街",
      dt
    )

    if (success) {
      println(s"user [$user] inserted")
    }
    else println(s"Failed to insert user [$user]")
  }

  val conn = tryConnect("jdbc:mysql://localhost/xgproj?useUnicode=true&characterEncoding=UTF-8&user=root&password=cPEaKeXnzq8fuRBD87csHdaL")

  println(s"Connected: ${conn.getCatalog}")
  //traceAllProducts(conn)

  tryInsertCustomer(conn)

  traceAllCustomers(conn)

  conn.close()

}
