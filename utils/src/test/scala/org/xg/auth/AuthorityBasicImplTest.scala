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
  private val authSuccessToken = Option(TokenNotUsed)

  private val testData = Table(
    ( "userPassMap", "uid", "pass", "expAuthResult" ),
    ( userPassMap1, "usr1", "123", None ),
    ( userPassMap1, "usr1", "1234", authSuccessToken ),
    ( userPassMap1, "usr2", "abcdefg", authSuccessToken ),
    ( userPassMap1, null, "abcdefg", None ),
    ( null, "usr1", "1234", None ),
    ( null, null, "1234", None )
  )

  @Test
  def basicAuthorizerTest:Unit = {
    println("[Test] -- in basicAuthorizerTest")
    forAll (testData) { (userPassMap, uid, pass, expResult) =>
      val authorizer = instance(userPassMap)
      println(s"\tchecking user: [$uid], pass: [$pass] ...")

      val authRes = authorizer.authenticate(uid, pass, UserType.Customer)
      authRes shouldBe expResult
    }
  }

}
