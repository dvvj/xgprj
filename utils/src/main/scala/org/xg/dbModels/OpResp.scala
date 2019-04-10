package org.xg.dbModels

case class OpResp(success:Boolean, errMsg:Option[String]) {
  def errMsgJ:String = errMsg.orNull
}

object OpResp {
  val Success:OpResp = OpResp(true, None)
  def failed(errMsg:String):OpResp = OpResp(false, Option(errMsg))
}
