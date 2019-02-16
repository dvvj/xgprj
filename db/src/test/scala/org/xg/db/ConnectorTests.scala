package org.xg.db

import java.nio.charset.StandardCharsets
import java.sql.{Connection, DriverManager}

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

  val conn = tryConnect("jdbc:mysql://localhost/xgproj?useUnicode=true&characterEncoding=UTF-8&user=root&password=cPEaKeXnzq8fuRBD87csHdaL")

  println(s"Connected: ${conn.getCatalog}")
  traceAllProducts(conn)

  conn.close()

}
