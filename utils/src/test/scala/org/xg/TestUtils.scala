package org.xg

object TestUtils {

  def checkDoubleEquals(d1:Double, d2:Double):Boolean = {
    math.abs(d1 - d2) < 1e-8
  }
}
