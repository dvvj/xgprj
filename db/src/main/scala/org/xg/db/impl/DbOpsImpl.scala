package org.xg.db.impl

import java.sql.Connection
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZonedDateTime}

import org.joda.time.DateTime
import org.xg.db.api.TDbOps

object DbOpsImpl {

  private class JDBCDbOps(conn:Connection) extends TDbOps {
    private val _conn = conn

    override def addNewCustomer(uid: String, name: String, idCardNo: String, mobile: String, postalAddr: String, bday: String): Boolean = {
      //val dtStr = bday.format(DateTimeFormatter.ISO_DATE)
      try {
        val sttm = _conn.prepareStatement(
          "INSERT INTO customers(id, name, idcard_no, mobile, postal_addr, bday)" +
            s" VALUES ('$uid', '$name', '$idCardNo', '$mobile', '$postalAddr', '$bday')"
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
  }

  def jdbcImpl(conn:Connection):TDbOps = new JDBCDbOps(conn)

}
