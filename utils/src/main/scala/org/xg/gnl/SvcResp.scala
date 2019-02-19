package org.xg.gnl

case class SvcResp(success:Boolean, errorMsg:Option[String], exception:Option[Throwable])

object SvcResp {
  val SUCCESS:SvcResp = SvcResp(true, None, None)

  def error(errorMsg:String):SvcResp = SvcResp(false, Option(errorMsg), None)
  def exception(exception: Throwable):SvcResp = SvcResp(false, None, Option(exception))
}
