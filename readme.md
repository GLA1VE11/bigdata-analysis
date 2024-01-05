# 基于hadoop的用户听歌行为分析与歌曲推荐

## 系统功能架构图

![系统功能架构图](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E7%B3%BB%E7%BB%9F%E5%8A%9F%E8%83%BD%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 系统技术架构图

![系统技术架构图](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E7%B3%BB%E7%BB%9F%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 项目分工

[见成果展示.pptx](https://github.com/GLA1VE11/bigdata-analysis/blob/main/%E6%88%90%E6%9E%9C%E5%B1%95%E7%A4%BA.pptx)

## docker部署
本项目前期部署在本地，后期移植为前后端docker镜像 + hdfs-docker镜像

具体步骤如下（以前后端docker镜像为例）：
1. 使用 docker load 命令加载前后端镜像。
```
docker load -i /path/to/mysql.tar
docker load -i /path/to/demo.tar
docker load -i /path/to/nginx.tar
```

2. 创建一个新的目录，将 docker-compose.yml 文件保存在这个目录中(修改文件中的jar及dist文件目录)

3. 在保存了 docker-compose.yml 文件的目录中运行以下命令启动服务：

```
docker-compose up -d
```
4. 部署hdfs-docker

注：1. 因git文件大小限制，前后端docker镜像存在百度网盘（链接：https://pan.baidu.com/s/1akRncEDPqlxUNZRssdvdFw  提取码：a1d7 ），三个tar文件如下：
![docker](https://github.com/GLA1VE11/bigdata-analysis/blob/master/docker.png)
2. 启动docker后访问IP:9092
3. 用户名/密码：User_pkq/123456

#### docker成功启动后如下图所示
![启动docker](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E5%90%AF%E5%8A%A8docker.png)

#### 本地项目使用到的库版本信息如下：

- spark 1.4.0
- hadoop 2.6.0
- hive 2.1.0
- sqoop 1.4.7
- mahout 0.12.1

#### hdfs-docker项目使用到的库版本信息如下：
 - hadoop 3.2.1
 - mysql 8.0.22
 - hive 3.1.2
 - spark 3.0.1
 - scala 2.12.12
 - sqoop 1.4.7
 - mahout 0.12.1

#### hdfs-docker项目说明：
 - hdfs-docker镜像存在北大网盘（链接：https://disk.pku.edu.cn/#/link/32B9AD14F89F26154B7835C0FF9C504E）
 - 镜像对应的容器名分别为：mysql_hive、Master、Slave1、Slave2
 - ！！由于docker的特殊性，每次进入Master容器都需要执行 source /etc/profile
 - mysql_hive中root账户密码为abc123456
 - 若Master容器中服务没有自动启动，则分别输入以下指令进行启动：start-all.sh、cd /usr/local/spark-3.0.1/sbin、./start-all.sh
 - 可访问http://127.0.0.1:8088/cluster/apps、http://127.0.0.1:9870/
 - 原始数据集（.csv）位于Master:/home、MapReduce分析代码位于Master:/home/map-reduce、Hive分析代码见本说明文档、推荐部分代码位于Master:/home/Recommend

## map-reduce分析
- 上传数据到hdfs中

```
./hadoop jar /home/map-reduce/uploadData/src/main/java/uploadData.jar /home/mars_tianchi_user_action.csv /user/data/mars_tianchi_user_action.csv
./hadoop jar /home/map-reduce/uploadData/src/main/java/uploadData.jar /home/mars_tianchi_songs.csv /user/data/mars_tianchi_songs.csv
```

- 分析收藏量Top 10的歌曲

```
./hadoop jar /home/map-reduce/topCollectSong/src/main/java/topCollectSong.jar /user/data/mars_tianchi_user_action /user/analysis/topCollect
```

- 分析下载量Top 10的歌曲

```
./hadoop jar /home/map-reduce/topDownloadSong/src/main/java/topDownloadSong.jar /user/data/mars_tianchi_user_action /user/analysis/topDownload
```

- 分析播放量Top10的歌曲

```
./hadoop jar /home/map-reduce/topTimeSong/src/main/java/topTimeSong.jar /user/data/mars_tianchi_user_action /user/analysis/topPlay
```
## Hive操作

- 首先确保mysql服务成功运行：

```
sudo systemctl status mysql
```

- 确保可以远程登录mysql

```
mysql -h master -u root -p
```

- 将本地的两个csv文件由linux上传至hdfs：

```
hdfs dfs -mkdir -p /user/data
hdfs dfs -put /home/mars_tianchi_user_action.csv /user/data/
hdfs dfs -put /home/mars_tianchi_songs.csv /user/data/
```

- 创建Hive中的数据库及对应的表

```mysql
> hive
CREATE DATABASE IF NOT EXISTS mars;
USE mars;

CREATE TABLE IF NOT EXISTS mars_tianchi_user_action (
    user_id STRING,
    song_id STRING,
    action_type STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;

CREATE TABLE IF NOT EXISTS mars_tianchi_songs (
    song_id STRING,
    artist_id STRING,
    song_init_plays STRING
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;

```

- 成功连接hive后，并执行上面的命令后，可以查看已经建好的数据库和表

```mysql
show databases;
use mars;
show tables;
```

## 包含推荐算法

#### Mahout操作	

我们尝试使用Mahout机器学习库进行推荐。以下是一些可以使用的算法和相应的 Mahout 命令行参数：

1. **基于用户的协同过滤**（User-based Collaborative Filtering）:

   ```
   mahout recommenditembased 
     --input /path/to/ratings 
     --output /path/to/recommendations 
     --similarityClassname SIMILARITY_PEARSON_CORRELATION 
     --tempDir /path/to/temp
   ```

2. **基于物品的协同过滤**（Item-based Collaborative Filtering）:

   ```
   mahout recommenditembased 
     --input /path/to/ratings 
     --output /path/to/recommendations 
     --similarityClassname SIMILARITY_COSINE 
     --tempDir /path/to/temp
   ```

   这里的 `SIMILARITY_COSINE` 表示使用余弦相似度。

3. **随机森林推荐**（Random Forest-based Recommendations）:

   ```
   mahout buildforest 
     --input /path/to/ratings 
     --output /path/to/model 
     --descriptor descriptor 
     --numTrees 100
   ```

   推荐结果产生后，可以使用如下命令查看结果：

   ```
   hadoop fs -cat /mahout/mahout_output/part-r-00000
   ```

#### 其他算法

- 图随机游走推荐算法
- 序列推荐STAMP算法
- 基于图神经网络的推荐算法(GCN)

#### 算法性能对比如下图：

![Figure_1](https://github.com/GLA1VE11/bigdata-analysis/blob/master/Figure_1.png)

![Figure_2](https://github.com/GLA1VE11/bigdata-analysis/blob/master/Figure_2.png)

## 前端展示

#### 首页

![首页](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E9%A6%96%E9%A1%B5.png)

#### 用户歌曲推荐

![歌曲推荐](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E9%9F%B3%E4%B9%90%E6%8E%A8%E8%8D%90.png)

#### 最热歌手排行榜

![歌手排行](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E6%AD%8C%E6%89%8B%E6%8E%92%E8%A1%8C.png)

#### 最热歌曲排行榜

![歌曲排行](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E6%AD%8C%E6%9B%B2%E6%8E%92%E8%A1%8C.png)



