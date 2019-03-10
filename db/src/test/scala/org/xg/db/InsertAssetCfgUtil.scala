package org.xg.db

object InsertAssetCfgUtil extends App {

  import org.xg.dbModels.AssetCfg._

  val testAssets = Array(
    asset(
      1,
      Array(
        Array("1.png", "cat1"),
        Array("2.png", "cat1"),
        Array("3.png", "cat2")
      ),
      Array(
        Array("1.pdf", "cat3"),
        Array("2.pdf", "cat1")
      )
    ),
    asset(
      2,
      Array(
        Array("1.png", "cat1")
      ),
      Array(
      )
    ),
    asset(
      3,
      Array(
        Array("1.png", "cat1")
      ),
      Array(
        Array("1.pdf", "cat1")
      )
    ),
    asset(
      4,
      Array(
        Array("1.png", "cat1")
      ),
      Array(
        Array("1.pdf", "cat1")
      )
    )
  )

  val insertStatementTemplate = "INSERT INTO product_assets (product_id, assets) " +
    "VALUES (%s);"
  val insertStatements = testAssets.map { asset =>
    val assetsJson = assetsToJsons(asset.assets)
    val v = s"${asset.prodId},'$assetsJson'"
    insertStatementTemplate.format(v)
  }

  println(insertStatements.mkString("\n"))
}
