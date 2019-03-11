package org.xg.dbUtils

import org.xg.dbModels.{AssetCfg, MProdDetail, MProduct}

import scala.collection.mutable.ListBuffer

object InsertProductsUtil extends App {

  val prodInfoMap = Map(
    1 -> (
      "Astaxin虾青素", 1499.99, "抗氧化剂,美白",
      MProdDetail(
        "SV", "虾青素清除自由基的能力是维生素C的功效的6000倍；是维生素E的1000倍；是辅酶Q10的800倍；是一氧化氮的1800倍；是纳豆的3100倍；是花青素的700倍；是β-胡萝卜素功效的10倍；是lycopene（番茄红素）功效的1800倍；是carotol（叶黄素）功效的200倍；是teapolyphenols（茶多酚）功效的320倍。"
      )
    ),
    2 -> (
      "瑞典ACO Gravid孕妇产妇复合维生素", 139.99, "孕产妇,维生素,矿物质",
      MProdDetail(
        "SV", "含有12种维生素和10种矿物质。针对孕期和哺乳期女性对于营养的额外需求添加了如叶酸、铁、钙和维生素D在内的多种维生素和矿物质。含有重要的抗氧化功能的维生素C和E，能够防止DNA，蛋白质和脂肪的氧化变化。"
      )
    ),
    3 -> (
      "Pharbio Omega-3 Forte 70%高纯度深海鱼油", 9.99, "鱼油,Omega-3",
      MProdDetail(
        "SV", "瑞典销量最好，品质最高的成人鱼油，也是世界上纯度最高的鱼油，是瑞典国家药房鱼油销量冠军。70%的Omega3 含量（DHA/EPA), 瑞典医生唯一推荐孕妇哺乳期间补充DHA/EPA的鱼油，含DHA有助于宝宝大脑、眼睛等器官的发育。Pharbio产品完全按照欧洲严格的药品标准生产，高纯度源自先进的生产工艺，去除了鱼油本身含有的大部分饱和脂肪酸。在普通天然鱼油中，大约含有20-25%的不需要的饱和脂肪酸，而Pharbio Omega-3 Forte胶囊只含有5%不需要的饱和脂肪酸。将对人体有害的饱和脂肪酸尽量剔除并提纯（世界上没有任何一款鱼油是可以完全剔除饱和脂肪酸的），从营养学角度认为非重要的脂肪酸性物质以及可能存在的环境毒素和重金属也被尽量剔除，确保是健康有效的鱼油，纯度世界第一高！"
      )
    ),
    4 -> (
      "LIFE Q10 100mg 辅酶", 149.99, "辅酶Q10",
      MProdDetail(
        "US", "每粒Life Q10包含30毫克辅酶Q10，也叫作辅酶q，它是一种脂溶性的类似维生素的物质。Q10的主要功能是在细胞线粒体能量（三磷酸腺苷）合成时充当辅酶。所有的细胞都是靠三磷酸腺苷才能进行各种活动流程，因此辅酶对身体所有的细胞而言都是至关重要的，如此一来，辅酶对所有组织和器官的重要性也就不言而喻了。"
      )
    )
  )

  private val AssetsNA:AssetCfg = null
  val products = (1 to 4).map { idx =>
    val (name, price0, keywords, details) = prodInfoMap(idx)
    MProduct(
      idx, name, price0, MProdDetail.toJson(details), keywords, AssetsNA
    )
  }.toArray

  val insertStatementTemplate =
    "INSERT INTO products (id, name, price0, detailed_info, keywords)" +
      "  VALUES (%s);"

  val insertStatements = products.map { p =>
    val params = ListBuffer[String]()
    params += p.id.toString
    params += s"'${p.name}'"
    params += p.price0.toString
    params += s"'${p.detailedInfo}'"
    params += s"'${p.keywords}'"
    insertStatementTemplate.format(params.mkString(", "))
  }

  println(insertStatements.mkString("\n"))
}
