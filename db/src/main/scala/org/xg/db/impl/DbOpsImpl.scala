package org.xg.db.impl

import java.sql.{Connection, ResultSet, Statement}
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.util.Base64

import org.joda.time.DateTime
import org.xg.auth.AuthHelpers
import org.xg.db.api.TDbOps
import org.xg.db.model.MCustomer
import org.xg.gnl.DataUtils

import scala.collection.mutable.ListBuffer

object DbOpsImpl {

  private class JDBCDbOps(conn:Connection) extends TDbOps {
    private val _conn = conn

    import collection.mutable

    override def placeOrder(uid: String, productId: Int, qty: Double): Long = {
      try {
        val time = DataUtils.utcTimeNow
        val timeStr = DataUtils.zonedDateTime2Str(time)
        val sttm = _conn.prepareStatement(
          "INSERT INTO orders(customer_id, product_id, qty, creation_time)" +
            s" VALUES ('$uid', $productId, $qty, '$timeStr')",
          Statement.RETURN_GENERATED_KEYS
        )
        val affectedRows = sttm.executeUpdate()
        if (affectedRows == 0)
          throw new RuntimeException("Failed to create order")
        val genKeys = sttm.getGeneratedKeys

        if (genKeys.next()) {
          val res = genKeys.getLong(1)
          res
        }
        else
          throw new RuntimeException("No id obtained!")
      }
      catch {
        case t:Throwable => {
          t.printStackTrace()
          throw new RuntimeException("Error placing order", t)
        }
      }

    }

    override def ordersOf(uid: String): String = {
      try {
        val sttm = _conn.prepareStatement(
          s"SELECT * FROM orders WHERE customer_id = '$uid'"
        )
        val res = sttm.executeQuery()
        val orders = ListBuffer[String]()
        println("getting results ...")
        while (res.next()) {
          // res.getString("name") //
          val orderId = res.getBigDecimal("id")
          val prodId = res.getString("product_id")
          val createTime = res.getTimestamp("creation_time")
          val zdt = DataUtils.timestamp2Zone(createTime)
          val qty = res.getFloat("qty")
          orders += s"$orderId\tproduct_id: $prodId\t$zdt\t$qty"
          println(s"\t$orders")
        }
        orders.mkString("\n")
      }
      catch {
        case t:Throwable => {
          //t.printStackTrace()
          throw new RuntimeException("Error in getUserPassMap", t)
        }
      }
    }

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

    override def allCustomers:Array[MCustomer] = {
      val stm = conn.prepareStatement(
        "SELECT * from customers"
      )

      val res = stm.executeQuery()
      val customers = ListBuffer[MCustomer]()
      while (res.next()) {
        // res.getString("name") //
        val uid = res.getString("uid")
        val name = res.getString("name")
        val idCardNo = res.getString("idcard_no")
        val mobile = res.getString("mobile")
        val postalAddr = res.getString("postal_addr")
        val bday = res.getString("bday")
        val refId = res.getString("ref_uid")
        customers += MCustomer(
          uid, name,
          idCardNo, mobile,
          postalAddr,
          bday, refId
        )
//        val passHash = res.getBytes("pass_hash")
//        val hashStr = AuthHelpers.hash2Str(passHash)
//        val hashBase64 = Base64.getEncoder.encodeToString(passHash)
      }
      customers.toArray
    }

    override def allProducts: String = {
      val stm = conn.prepareStatement(
        "SELECT * from products"
      )

      val res = stm.executeQuery()
      val productTrs = ListBuffer[String]()
      while (res.next()) {
        // res.getString("name") //
        val prd = res.getString("name")
        val id = res.getString("id")
        val price0 = res.getFloat("price0")
        productTrs += s"$id\t$prd\t$price0"
      }
      productTrs.mkString("\n")

    }
  }

  def jdbcImpl(conn:Connection):TDbOps = new JDBCDbOps(conn)

}
