package org.xg.auth

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import org.xg.user.UserData.UserType

class AuthorityBasicImplTest extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val userPassMap1 = Map(
    "usr1" -> AuthHelpers.sha512("1234"),
    "usr2" -> AuthHelpers.sha512("abcdefg")
  )
  import AuthorityBasicImpl._
  private val authSuccessToken = TokenNotUsed

  private val testData = Table(
    ( "userPassMap", "uid", "pass", "expAuthResult" ),
    ( userPassMap1, "usr1", "123", InvalidToken ),
    ( userPassMap1, "usr1", "1234", authSuccessToken ),
    ( userPassMap1, "usr2", "abcdefg", authSuccessToken ),
    ( userPassMap1, null, "abcdefg", InvalidToken ),
    ( null, "usr1", "1234", InvalidToken ),
    ( null, null, "1234", InvalidToken )
  )

  @Test
  def basicAuthorizerTest:Unit = {
//    println("[Test] -- in basicAuthorizerTest")
//    forAll (testData) { (userPassMap, uid, pass, expResult) =>
//      val authorizer = instance(userPassMap)
//      println(s"\tchecking user: [$uid], pass: [$pass] ...")
//
//      val passHash = AuthHelpers.sha512(pass)
//      val authRes = authorizer.authenticate(uid, passHash)
//      authRes shouldBe expResult
//    }
  }

}
