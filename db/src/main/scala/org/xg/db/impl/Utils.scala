package org.xg.db.impl

import java.sql.{Connection, DriverManager}

object Utils {

  def tryConnect(connStr:String):Connection = {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver")
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
}
