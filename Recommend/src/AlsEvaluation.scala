import java.io.File
import scala.io.Source
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.{DateTime, Duration}
import org.joda.time._
import org.joda.time.format._
import org.jfree.data.category.DefaultCategoryDataset
import org.apache.spark.mllib.regression.LabeledPoint

object AlsEvaluation {
  /*设置日志及乱七八糟的配置*/
  def SetLogger: Unit ={
    System.setProperty("hadoop.home.dir", "/usr/local/hadoop")
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("com").setLevel(Level.OFF)
    System.setProperty("spark.ui.showConsoleProgress","false")
    Logger.getRootLogger.setLevel(Level.OFF)
  }
 
  /*数据准备 @return 返回（训练数据，评估数据，测试数据）*/
  def PrepareData():(RDD[Rating],RDD[Rating],RDD[Rating])={
    val sc=new SparkContext(new SparkConf().setAppName("Recommend").setMaster("local[4]").set("spark.testing.memory","21474800000"))
    //创建用户评分数据
    print("开始读取用户评分数据中...")
    val rawUserData=sc.textFile("hdfs://master:9000/user/pkq/rec_data/u.data")
    val rawRatings=rawUserData.map(_.split("\t").take(3))
    val ratingsRDD=rawRatings.map{case Array(user,movie,rating) => Rating( user.toInt ,movie.toInt,rating.toFloat)}
    println("共计："+ratingsRDD.count().toString+"条评分")
    //创建电影ID和名称对应表
    print("开始读取电影数据中...")
    val itemRDD=sc.textFile("hdfs://master:9000/user/pkq/rec_data/u.item")
    val moiveTitle=itemRDD.map(_.split("\\|").take(2)).map(array=>(array(0).toInt,array(1))).collect().toMap
    //显示数据记录数
    val numRatings=ratingsRDD.count()
    val numUser=ratingsRDD.map(_.user).distinct().count()
    val numMoive=ratingsRDD.map(_.product).distinct().count()
    println("共计：评分"+numRatings+"条 用户"+numUser+"个 电影"+numMoive+"个")
    //将数据分为三个部分并且返回
    print("将数据分为：")
    val Array(trainData,validationData,testData)=ratingsRDD.randomSplit(Array(0.8,0.1,0.1))
    println("训练数据："+trainData.count()+"条 评估数据："+validationData.count()+"条 测试数据："+testData.count()+"条")
    (trainData,validationData,testData)
  }
 
  /*计算RMSE值	*@param model 训练模型	*@param validationData 评估数据	*@return RMSE值 */
  def computeRmse(model: MatrixFactorizationModel, validationData: RDD[Rating]):(Double) ={
    val num=validationData.count();
    val predictedRDD=model.predict(validationData.map(r=>(r.user,r.product)))
    val predictedAndVali=predictedRDD.map(p=>((p.user,p.product),p.rating)).join(validationData.map(r=>((r.user,r.product),r.rating))).values
    math.sqrt(predictedAndVali.map(x=>(x._1-x._2)*(x._1-x._2)).reduce(_+_)/num)
  }
 
  /** 训练模型
    * @param trainData 训练数据
    * @param validationData 评估数据
    * @param rank 训练模型参数
    * @param numIterations 训练模型参数
    * @param lambda 训练模型参数
    * @return 模型返回的RMSE（该值越小，误差越小）值，训练模型所需要的时间
    */
  def trainModel(trainData: RDD[Rating], validationData: RDD[Rating], rank: Int, numIterations: Int, lambda: Double):(Double,Double)={
    val startTime=new DateTime()
    val model=ALS.train(trainData,rank,numIterations,lambda)
    val endTime=new DateTime()
    val Rmse=computeRmse(model,validationData)
    val duration=new Duration(startTime,endTime)
    println(f"训练参数：rank：$rank= 迭代次数：$numIterations%.2f lambda：$lambda%.2f 结果 Rmse $Rmse%.2f"+" 训练需要时间："+duration.getMillis+"毫秒")
    (Rmse,duration.getStandardSeconds)
  }
 
  /** 使用jfree.char评估单个参数,这里没有实现
    * @param trainData 训练数据
    * @param validationData 评估数据
    * @param evaluateParameter 评估参数名称
    * @param rankArray rank参数数组
    * @param numIterationsArray 迭代次数参数数组
    * @param lambdaArray lambda参数数组
    */
 
  def evaluateParameter(trainData:RDD[Rating],validationData:RDD[Rating],evaluateParameter:String,rankArray:Array[Int],numIterationsArray:Array[Int],lambdaArray:Array[Double])={
    val dataBarChart = new DefaultCategoryDataset()
    val dataLineChart = new DefaultCategoryDataset()
    for(rank <- rankArray;numIterations <- numIterationsArray;lambda <- lambdaArray){
      val (rmse,time) = trainModel(trainData,validationData,rank,numIterations,lambda)
      val parameterData = evaluateParameter match{
        case "rank" => rank;
        case "numIterations" => numIterations;
        case "lambda" => lambda
      }
      dataBarChart.addValue(rmse,evaluateParameter,parameterData.toString())
      dataLineChart.addValue(time,"Time",parameterData.toString())
    }
    Chart.plotBarLineChart("ALS evaluations " + evaluateParameter,evaluateParameter,"RMSE",0.58,5,"Time",dataBarChart,dataLineChart)
  }
 
  /*
    * 三个参数交叉评估，找出最好的参数组合
    * @param trainData 训练数据
    * @param validationData 评估数据
    * @param rankArray rank参数数组
    * @param numIterationsArray 迭代次数参数数组
    * @param lambdaArray lambda参数数组
    * @return 返回由最好参数组合训练出的模型
    */
 
  def evaluateAllParameter(trainData:RDD[Rating],validationData:RDD[Rating],rankArray:Array[Int],numIterationsArray:Array[Int],lambdaArray:Array[Double]): MatrixFactorizationModel = {
    val evaluations=for(rank <- rankArray;numIterations <- numIterationsArray;lambda <- lambdaArray) yield {
      val (rmse,time)=trainModel(trainData,validationData,rank,numIterations,lambda)
      (rank,numIterations,lambda,rmse)
    }
    val Eval=(evaluations.sortBy(_._4))
    val bestEval=Eval(0)
    println("最佳模型参数：rank："+bestEval._1+" 迭代次数："+bestEval._2+" lambda:"+bestEval._3+" 结果rmse："+bestEval._4)
    val bestModel=ALS.train(trainData,bestEval._1,bestEval._2,bestEval._3)
    (bestModel)
  }
 
  /*训练评估	*@param trainData 训练数据	*@param validationData 评估数据		*@return 返回一个最理想的模型 */
  def trainValidation(trainData:RDD[Rating], validationData:RDD[Rating]):MatrixFactorizationModel={
    println("------评估rank参数使用------")
    evaluateParameter(trainData,validationData,"rank",Array(5,10,15,20,50,100),Array(10),Array(0.1))
    println("------评估numIterations------")
    evaluateParameter(trainData,validationData,"numIterations",Array(10),Array(5,10,15),Array(0.1))
    println("------评估lambda------")
    evaluateParameter(trainData,validationData,"lambda",Array(10),Array(10),Array(0.05,0.1,1,5,10.0))
    println("------所有参数交叉评估找出最好的参数组合------")
    val bestmodel=evaluateAllParameter(trainData,validationData,Array(5,10,15,20,50,100),Array(5,10,15),Array(0.01,0.05,0.1,1,5,10.0))
    bestmodel
  }
 
  def main(args: Array[String]) {
    SetLogger
    println("========================数据准备阶段========================")
    val (trainData,validationData,testData)=PrepareData()
    trainData.persist();validationData.persist();testData.persist()
    println("========================训练验证阶段========================")
    val bestModel=trainValidation(trainData,validationData)
    println("======================测试阶段===========================")
    val testRmse=computeRmse(bestModel,testData)
    println("使用bestModel测试testData，结果rmse="+testRmse)
  }
}
