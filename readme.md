# 基于hadoop的用户听歌行为分析与歌曲推荐

## 系统功能架构图

![系统功能架构图](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E7%B3%BB%E7%BB%9F%E5%8A%9F%E8%83%BD%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 系统技术架构图

![系统技术架构图](https://github.com/GLA1VE11/bigdata-analysis/blob/master/%E7%B3%BB%E7%BB%9F%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 项目分工

[见成果展示.pptx](https://github.com/GLA1VE11/bigdata-analysis/blob/main/%E6%88%90%E6%9E%9C%E5%B1%95%E7%A4%BA.pptx)

## docker部署

1. 使用 docker load 命令加载镜像。例如：
  - docker load -i /path/to/mysql.tar
  - docker load -i /path/to/demo.tar
  - docker load -i /path/to/nginx.tar

2. 创建一个新的目录，将 docker-compose.yml 文件保存在这个目录中(修改文件中的jar及dist文件目录)

3. 在保存了 docker-compose.yml 文件的目录中运行以下命令启动服务：

```
docker-compose up -d
```
## map-reduce分析
- 上传本地数据到hdfs中

```
./hadoop jar jar_dir/uploadData.jar user_data_dir/mars_tianchi_user_action.csv bigdata/data/user
./hadoop jar jar_dir/uploadData.jar user_data_dir/mars_tianchi_songs_.csv bigdata/data/song
```

- 分析收藏量Top 10的歌曲

```
./hadoop jar jar_dir/topCollectSong.jar bigdata/data/user bigdata/analysis/topCollect
```

- 分析下载量Top 10的歌曲

```
./hadoop jar jar_dir/topDownloadSong.jar bigdata/data/user bigdata/analysis/topdownload
```

- 分析播放量Top10的歌曲

```
./hadoop jar jar_dir/topTimeSong.jar bigdata/data/user bigdata/analysis/topPlay
```

## 包含推荐算法

#### 基于Mahout实现的推荐算法

- 基于用户的协同过滤（User-based Collaborative Filtering）
- 基于物品的协同过滤（Item-based Collaborative Filtering）
- 基于矩阵分解的推荐（Matrix Factorization-basedRecommendations）
- 基于 SVD 的推荐（Singular Value Decomposition）
- 基于逻辑回归的推荐（Logistic Regression-based Recommendations）
- 随机森林推荐（Random Forest-based Recommendations）

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



