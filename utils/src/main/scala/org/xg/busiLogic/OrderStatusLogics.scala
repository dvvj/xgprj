package org.xg.busiLogic

import org.xg.dbModels.MOrder

object OrderStatusLogics {
  val Status_Cancelled:Int = -1
  val Status_CreatedNotPaid:Int = 1
  val Status_Paid:Int = 2
  val Status_Locked:Int = 3

  def status(morder: MOrder):Int = {
    if (morder.procTime1S.nonEmpty)
      Status_Locked
    if (morder.procTime2S.nonEmpty)
      Status_Cancelled
    else if (morder.payTime.nonEmpty)
      Status_Paid
    else
      Status_CreatedNotPaid
  }

  def isCancelled(mo:MOrder):Boolean = status(mo) == Status_Cancelled
  def isUnpaid(mo:MOrder):Boolean = status(mo) == Status_CreatedNotPaid
  def orderCanBePaid(morder:MOrder):Boolean = status(morder) == Status_CreatedNotPaid
  def orderCanBeCancelled(morder:MOrder):Boolean = status(morder) == Status_CreatedNotPaid
}
