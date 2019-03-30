package org.xg.snippet

object TestCurrentDir {
  def main(args:Array[String]):Unit = {
    println(
      System.getProperty("user.dir")
    )
  }
}
