package org.xg.auth

import java.sql.Timestamp
import java.time.ZonedDateTime
import java.util.{Date, Random}

import org.scalatest.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import org.xg.gnl.SvcResp

class SessionMgrTest extends TestNGSuite with Matchers with TableDrivenPropertyChecks {


  import SvcResp._
  private val addTestData = Table(
    ( "existingUids", "uids", "uidsToCheck" ),
    (
      List(
        "uid0"
      ),
      List(
        "uid1", "uid2"
      ),
      List(
        "uid0", "uid1", "uid2"
      )
    )
  )
  private val tokenNA = "123"

  @Test
  def testAdd():Unit = {
    forAll (addTestData) { (existing, uidsToAdd, uidsToCheck) =>
      val sessMgr = SessionMgr.create(1200)

      existing.foreach(sessMgr.addSession(_, tokenNA))

      uidsToAdd.foreach(sessMgr.addSession(_, tokenNA))

      uidsToCheck.foreach { uidc =>
        sessMgr.checkSession(uidc, tokenNA) shouldBe SUCCESS
      }

    }
  }

  private val expireTestData = Table(
    ( "timeout", "sleepTime" ),
    ( 15, 25 )
  )

  @Test(enabled = false)
  def testExpire():Unit = {
    forAll (expireTestData) { (timeout, sleepTime) =>

      val sessMgr = SessionMgr.create(timeout)

      val uid = "uid1"
      val r0 = sessMgr.addSession(uid, tokenNA)
      r0 shouldBe SUCCESS
      println(s"Uid [$uid] added.")
      val r1 = sessMgr.checkSession(uid, tokenNA)
      r1 shouldBe SUCCESS

      println(s"Sleeping $sleepTime seconds ...")
      Thread.sleep(sleepTime * 1000)

      val r2 = sessMgr.checkSession(uid, tokenNA)
      r2.success shouldBe false

      val allUsers = sessMgr.testAllUsers()
      allUsers.size shouldBe 0
    }
  }

  private class AddSessionRunnable(sessMgr:SessionMgr, uid:String) extends Runnable {
    override def run(): Unit = {
      // add random sleep time
      val ts = new Date().getTime
      val rs = new Random(ts)
      val s = math.abs(rs.nextInt()) % 500

      Thread.sleep(s)
      println(s"\tAdding session $uid ...")
      sessMgr.addSession(uid, tokenNA)
    }
  }

  @Test
  def concurrTest():Unit = {
    val iter = 10
    val concurrency = 10

    (0 until iter).foreach { it =>
      println(s"Iteration $it:")
      val sessionMgr = SessionMgr.create(100)
      val uids = (1 to concurrency).map(idx => s"uid$idx")
      uids.foreach { uid =>
        new Thread(
          new AddSessionRunnable(sessionMgr, uid)
        ).start()
      }
      Thread.sleep(2000)
      println(s"Checking iteration $it")
      uids.foreach { uid =>
        val resp = sessionMgr.checkSession(uid, tokenNA)
        resp shouldBe SUCCESS
      }
    }
  }

}
