package org.xg.db.impl

import java.sql.Connection
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZonedDateTime}

import org.joda.time.DateTime
import org.xg.auth.AuthHelpers
import org.xg.db.api.TDbOps

import scala.collection.mutable.ListBuffer

object DbOpsImpl {

  private class JDBCDbOps(conn:Connection) extends TDbOps {
    private val _conn = conn

    override def addNewCustomer(uid: String, name: String, pass:String, idCardNo: String, mobile: String, postalAddr: String, bday: String): Boolean = {
      //val dtStr = bday.format(DateTimeFormatter.ISO_DATE)
      try {
        val passHash = AuthHelpers.hash2Str(
          AuthHelpers.sha512(pass)
        )
        val sttm = _conn.prepareStatement(
          "INSERT INTO customers(id, name, pass_hash, idcard_no, mobile, postal_addr, bday)" +
            s" VALUES ('$uid', '$name', X'$passHash', '$idCardNo', '$mobile', '$postalAddr', '$bday')"
        )
        val res = sttm.executeUpdate()
        if (res != 1)
          println(s"Return value: $res")
        true
      }
      catch {
        case t:Throwable => {
          t.printStackTrace()
          false
        }
      }
    }

    override def allCustomers(conn:Connection):String = {
      val stm = conn.prepareStatement(
        "SELECT * from customers"
      )

      val res = stm.executeQuery()
      val customerTrs = ListBuffer[String]()
      while (res.next()) {
        // res.getString("name") //
        val prd = res.getString("name")
        val id = res.getString("id")
        val passHash = res.getBytes("pass_hash")
        val hashStr = AuthHelpers.hash2Str(passHash)
        customerTrs += s"$id\t$prd\t$hashStr"
      }
      customerTrs.mkString("\n")
    }
  }

  def jdbcImpl(conn:Connection):TDbOps = new JDBCDbOps(conn)

}
