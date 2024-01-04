import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.recommendation.{ALS, Rating}

object Recommend {
  def main(args: Array[String]): Unit = {
    // 设置日志级别，关闭不必要的信息输出
    org.apache.log4j.Logger.getLogger("org").setLevel(org.apache.log4j.Level.ERROR)
    org.apache.log4j.Logger.getLogger("com").setLevel(org.apache.log4j.Level.ERROR)

    // 创建Spark配置和上下文
    val conf = new SparkConf().setAppName("MusicRecommendation").setMaster("local[4]")
    val sc = new SparkContext(conf)

    // 读取用户行为数据并转换为Rating类型的RDD
    val rawData = sc.textFile("hdfs://master:9000/user/hive/warehouse/mars.db/mars_tianchi_user_action/mars_tianchi_user_action.csv")
    val userBehavior = rawData.map(_.split(',')).flatMap { case Array(user, song, action) =>
      try {
        // 假设action是评分，如果不是，需要将其转换成评分值
        Some(Rating(user.toInt, song.toInt, action.toDouble))
      } catch {
        case e: NumberFormatException => None // 捕获转换错误
      }
    }

    // 构建ALS模型
    val rank = 10
    val numIterations = 10
    val lambda = 0.01
    val alpha = 0.01 // 隐式反馈的置信度参数
    val model = ALS.trainImplicit(userBehavior, rank, numIterations, lambda, alpha)

    // 获取所有用户的ID
    val userIDs = userBehavior.map(_.user).distinct().collect()

    // 对每个用户推荐5首歌曲
    val recommendations = userIDs.flatMap { user =>
      val recs = model.recommendProducts(user, 5)
      recs.map(r => (user, r.product))
    }

    // 转换推荐结果为CSV格式并保存到HDFS
    val recommendationsString = recommendations.map { case (user, song) => s"$user,$song" }
    sc.parallelize(recommendationsString)
      .saveAsTextFile("hdfs://master:9000/user/pkq/rec_out")

    println("Done.")
    // 停止Spark上下文
    sc.stop()
  }
}
