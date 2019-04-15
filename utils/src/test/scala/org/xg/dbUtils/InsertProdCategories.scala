package org.xg.dbUtils

import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.xg.dbModels.IJStr

object InsertProdCategories {

  def main(args:Array[String]):Unit = {
    val rs = getClass.getResourceAsStream("/prod_categories.txt")
    val lines = IOUtils.readLines(rs, StandardCharsets.UTF_8)
    import collection.JavaConverters._
    lines.asScala.filter(!_.trim.isEmpty).map { l =>
      val parts = l.split("\\|\\|\\|")
      val categoryId = parts(0).trim.toInt
      val name = parts(1).trim
      val json = parts(2).trim
      val m = IJStr.mapFromJsons(json)
      println(m.byJ("zhs"))
    }
    rs.close()
  }

}
