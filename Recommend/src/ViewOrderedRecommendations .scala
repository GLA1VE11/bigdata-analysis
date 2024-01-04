import org.apache.spark.{SparkConf, SparkContext}

object ViewOrderedRecommendations {
  def main(args: Array[String]): Unit = {
    // 设置日志级别，关闭不必要的信息输出
    org.apache.log4j.Logger.getLogger("org").setLevel(org.apache.log4j.Level.ERROR)
    org.apache.log4j.Logger.getLogger("com").setLevel(org.apache.log4j.Level.ERROR)
    val conf = new SparkConf().setAppName("ViewOrderedRecommendations").setMaster("local[4]")
    val sc = new SparkContext(conf)

    // 读取保存在HDFS中的推荐结果
    val recommendations = sc.textFile("hdfs://master:9000/user/pkq/rec_out")

    // 将推荐结果转换为 (user, item) 对，并按用户ID分组
    val userItems = recommendations.map { rec =>
      val Array(user, item) = rec.split(',')
      (user.toInt, item.toInt)
    }.groupByKey()

    // 为每个用户格式化推荐项目列表，按用户ID顺序排列
    val formattedRecommendations = userItems
      .mapValues(_.toList.sorted.mkString("[", ", ", "]"))  // 项目列表排序（如果需要）并转为字符串
      .sortByKey()  // 按用户ID排序
      .map { case (user, items) => s"User: $user, Items: $items" }

    // 打印结果
    formattedRecommendations.collect().foreach(println)

    sc.stop()
  }
}
