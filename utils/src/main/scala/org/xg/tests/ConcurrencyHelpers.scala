package org.xg.tests

import scala.concurrent.Future

object ConcurrencyHelpers {

  type Task0 = () => Unit
  type Schedule0 = Iterable[Task0]

  import scala.concurrent.ExecutionContext.Implicits.global

  def runSchedule(s0:Schedule0):Future[Unit] = {
    Future {
      s0.foreach { t0 =>
        t0()
      }
    }
  }
}
