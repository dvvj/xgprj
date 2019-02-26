package org.xg.hbn

import org.hibernate.Session
import org.hibernate.query.Query
import org.xg.hbn.ent.Product
import org.xg.hbn.utils.HbnUtils

object QueryTests extends App {

  import Helpers._

  runInTransaction { sess =>
    val newProduction = new Product(
      5, "prod6", 189.99, "detailed 5", "kw5,kw6"
    )
    sess.persist(newProduction)
  }

  runInTransaction { sess =>
    val ql = s"Select p from ${classOf[Product].getName} p order by p.price0"

    val q = sess.createQuery(ql)
    val res = q.getResultList

    println(res.size())
  }


  HbnUtils.sessFactory.close()
}
