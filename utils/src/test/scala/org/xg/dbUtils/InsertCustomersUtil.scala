package org.xg.dbUtils

import org.xg.auth.AuthHelpers

object InsertCustomersUtil extends App {

  //INSERT INTO customers (id, name, idcard_no, mobile, postal_addr, bday) VALUES ('uid=customer1', '张晓东', '3102030222313322', '13892929133', '邮寄地址1','1983-02-05');

//  val testRefUid = "TestRef-4ae8f129bf1c"

  val testCustomers = Array(
    Array("customer1", "张晓东", "3102030222313322", "13892929133", "邮寄地址1", "1983-02-05", "prof1") -> "123",
    Array("customer2", "张晓", "31020555555555555", "1385555555", "邮寄地址2", "1983-02-05", "prof1") -> "456",
    Array("customer3", "晓东", "3102036666666666", "1386666666", "邮寄地址3", "1983-12-03", "prof1") -> "abcdef",
    Array("customer4", "王丽", "3102033333333333", "13833333333", "邮寄地址4", "1983-12-03", "prof2") -> "acf"
  )

  val insertStatementTemplate =
    "INSERT INTO customers (uid, name, idcard_no, mobile, postal_addr, bday, ref_uid, pass_hash)" +
      "  VALUES (%s);"

  val insertStatements = testCustomers.map { p =>
    val (line, pass) = p
    val passHash = AuthHelpers.sha512(pass)
    val hashStr = AuthHelpers.hash2Str(passHash)
    val params = line.mkString("'", "','", "'") + s",X'$hashStr'"
    insertStatementTemplate.format(params)
  }

  println(insertStatements.mkString("\n"))
}
