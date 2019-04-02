package org.xg.auth

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test

class AuthorityBasicImplTest extends TestNGSuite with Matchers with TableDrivenPropertyChecks {

  private val userPassMap1 = Map(
    "usr1" -> AuthHelpers.sha512("1234"),
    "usr2" -> AuthHelpers.sha512("abcdefg")
  )
  import AuthorityBasicImpl._
  private val authSuccessToken = TokenNotUsed

  private val testData = Table(
    ( "userPassMap", "uid", "pass", "expAuthResult" ),
    ( userPassMap1, "usr1", "123", false ),
    ( userPassMap1, "usr1", "1234", true),
    ( userPassMap1, "usr2", "abcdefg", true ),
    ( userPassMap1, null, "abcdefg", false ),
    ( userPassMap1, null, "", false ),
    ( null, "usr1", "1234", false ),
    ( null, null, "1234", false )
  )

  @Test
  def basicAuthorizerTest:Unit = {
    println("[Test] -- in basicAuthorizerTest")
    forAll (testData) { (userPassMap, uid, pass, expResult) =>
      val authorizer = instance(userPassMap)
      println(s"\tchecking user: [$uid], pass: [$pass] ...")

      val passHash = AuthHelpers.sha512(pass)
      val passHashStr = AuthHelpers.hash2Str(passHash)
      val authRes = authorizer.authenticate(uid, passHashStr)
      authRes.result shouldBe expResult
    }
  }

}
