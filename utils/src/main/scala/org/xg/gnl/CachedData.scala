package org.xg.gnl

import java.util.function.Supplier

import CachedData._
class CachedData[T >: AnyRef](
                     val retriever:CachedDataRetriever[T]
                   ) {
  //private val _lock:AnyRef = new Object
  private var _data:T = _
  def getData:T = {
    if (_data == null) {
      synchronized {
        if (_data == null)
          _data = retriever()
      }
    }
    _data
  }
  def update():Unit = {
    synchronized {
      _data = retriever()
    }
  }

}
object CachedData {
  type CachedDataRetriever[T] = () => T

  def createJ[T >: AnyRef](
    supplier:Supplier[T]
  ):CachedData[T] = {
    new CachedData(() => supplier.get())
  }
}

