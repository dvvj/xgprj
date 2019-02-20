package org.xg.log

object Logging {
  def debug(msg:String):Unit = {
    println(msg)
  }
  def debug(fmt:String, p1:Any):Unit = {
    println(fmt.format(p1))
  }
  def debug(fmt:String, p1:Any, p2:Any):Unit = {
    println(fmt.format(p1, p2)) //todo: generalize
  }
}
