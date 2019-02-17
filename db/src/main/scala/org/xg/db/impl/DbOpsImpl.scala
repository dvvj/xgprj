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

    import collection.mutable
    override def getUserPassMap: Map[String, Array[Byte]] = {
      try {
        val sttm = _conn.prepareStatement(
          "SELECT uid, pass_hash FROM customers"
        )
        val res = sttm.executeQuery()
        val userPassMap = mutable.Map[String, Array[Byte]]()
        while (res.next()) {
          // res.getString("name") //
          val uid = res.getString("uid")
          val passHash = res.getBytes("pass_hash")
          userPassMap += uid -> passHash
        }
        userPassMap.toMap

      }
      catch {
        case t:Throwable => {
          //t.printStackTrace()
          throw new RuntimeException("Error in getUserPassMap", t)
        }
      }

    }

    override def addNewCustomer(
                                 uid: String, name: String, pass:String,
                                 idCardNo: String, mobile: String, postalAddr: String, bday: String,
                                 ref_uid: String
                               ): Boolean = {
      //val dtStr = bday.format(DateTimeFormatter.ISO_DATE)
      try {
        val passHash = AuthHelpers.hash2Str(
          AuthHelpers.sha512(pass)
        )
        val sttm = _conn.prepareStatement(
          "INSERT INTO customers(uid, name, pass_hash, idcard_no, mobile, postal_addr, ref_uid, bday)" +
            s" VALUES ('$uid', '$name', X'$passHash', '$idCardNo', '$mobile', '$postalAddr', '$ref_uid', '$bday')"
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

    override def allCustomers:String = {
      val stm = conn.prepareStatement(
        "SELECT * from customers"
      )

      val res = stm.executeQuery()
      val customerTrs = ListBuffer[String]()
      while (res.next()) {
        // res.getString("name") //
        val prd = res.getString("name")
        val id = res.getString("uid")
        val passHash = res.getBytes("pass_hash")
        val hashStr = AuthHelpers.hash2Str(passHash)
        customerTrs += s"$id\t$prd\t$hashStr"
      }
      customerTrs.mkString("\n")
    }
  }

  def jdbcImpl(conn:Connection):TDbOps = new JDBCDbOps(conn)

}
