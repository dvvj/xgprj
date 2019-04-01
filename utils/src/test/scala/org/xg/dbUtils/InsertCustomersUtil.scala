package org.xg.dbUtils

import org.xg.auth.AuthHelpers
import org.xg.user.UserType._

object InsertCustomersUtil {

  //INSERT INTO customers (id, name, idcard_no, mobile, postal_addr, bday) VALUES ('uid=customer1', '张晓东', '3102030222313322', '13892929133', '邮寄地址1','1983-02-05');

//  val testRefUid = "TestRef-4ae8f129bf1c"

  private[dbUtils] val customerId1 = Customer.genUid("customer1")
  private[dbUtils] val customerId2 = Customer.genUid("customer2")
  private[dbUtils] val customerId3 = Customer.genUid("customer3")
  private[dbUtils] val customerId4 = Customer.genUid("customer4")
  private[dbUtils] val customerId5 = Customer.genUid("customer5")

  private[dbUtils] val customer2ProfMap = Map(
    customerId1 -> InsertMedProfsUtil.profId1,
    customerId2 -> InsertMedProfsUtil.profId1,
    customerId3 -> InsertMedProfsUtil.profId1,
    customerId4 -> InsertMedProfsUtil.profId2,
    customerId5 -> InsertMedProfsUtil.profId3
  )

  val testCustomers = Array(
    Array(customerId1, "张晓东", "3102030222313322", "13892929133", "邮寄地址1", "1983-02-05") -> "123",
    Array(customerId2, "张晓", "31020555555555555", "1385555555", "邮寄地址2", "1983-02-05") -> "456",
    Array(customerId3, "晓东", "3102036666666666", "1386666666", "邮寄地址3", "1983-12-03") -> "abcdef",
    Array(customerId4, "王丽", "3102033333333333", "13833333333", "邮寄地址4", "1983-12-03") -> "acf",
    Array(customerId5, "王丽丽", "3102033333333334", "13833333334", "邮寄地址5", "1983-12-04") -> "acf"
  )

  def main(args:Array[String]):Unit = {
    val insertStatementTemplate =
      "INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)" +
        "  VALUES (%s);"

    val insertStatements = testCustomers.map { p =>
      val (line, pass) = p
      val passHash = AuthHelpers.sha512(pass)
      val hashStr = AuthHelpers.hash2Str(passHash)
      val customerId = line(0)
      val lineWithProf = line ++ Array(customer2ProfMap(customerId))
      val params = lineWithProf.mkString("'", "','", "'") + s",X'$hashStr'"
      insertStatementTemplate.format(params)
    }

    println(insertStatements.mkString("\n"))
  }


}
