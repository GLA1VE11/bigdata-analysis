import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Dense
import spektral

# 数据读取
data_path = "F:/mars_tianchi_user_action.csv"
df = pd.read_csv(data_path)
df.columns = ["user_id", "song_id", "rating"]

# 构建图
user_ids = df['user_id'].unique()
song_ids = df['song_id'].unique()
num_users = len(user_ids)
num_songs = len(song_ids)

# 假设嵌入向量的维度为 64
embedding_dim = 64
user_embeddings = np.random.rand(num_users, embedding_dim)  # 模拟用户嵌入
song_embeddings = np.random.rand(num_songs, embedding_dim)  # 模拟歌曲嵌入
node_features = np.concatenate([user_embeddings, song_embeddings], axis=0)

adjacency_matrix = np.zeros((num_users + num_songs, num_users + num_songs))
for _, row in df.iterrows():
    user_idx = np.where(user_ids == row['user_id'])[0][0]
    song_idx = np.where(song_ids == row['song_id'])[0][0] + num_users
    adjacency_matrix[user_idx, song_idx] = 1
    adjacency_matrix[song_idx, user_idx] = 1

# 图卷积网络模型
def build_gcn_model(num_features, num_classes):
    X_in = Input(shape=(num_features,))
    A_in = Input((None,), sparse=True)

    gc1 = spektral.layers.GCNConv(16, activation='relu')([X_in, A_in])
    gc2 = spektral.layers.GCNConv(num_classes, activation='softmax')([gc1, A_in])

    model = Model(inputs=[X_in, A_in], outputs=gc2)
    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy')
    return model

# 准备训练数据
X_train, X_test, y_train, y_test = train_test_split(node_features, df['rating'], test_size=0.2)
A = spektral.utils.sparse_matrix(adjacency_matrix)

# 训练模型
model = build_gcn_model(X_train.shape[1], num_songs)
model.fit([X_train, A], y_train, epochs=5, validation_data=([X_test, A], y_test))

# 生成推荐
predictions = model.predict([node_features, A])
recommendations = np.argsort(predictions, axis=1)[:,-10:]  # 提取前10个推荐

# 保存推荐结果到CSV
recommendation_list = []
for idx, user_id in enumerate(user_ids):
    recs_dict = {'user_id': user_id}
    top_songs = recommendations[idx]
    for i, song_idx in enumerate(top_songs):
        song_id = song_ids[song_idx - num_users]
        recs_dict[f'recommended_song_{i+1}'] = song_id
    recommendation_list.append(recs_dict)

recommendation_df = pd.DataFrame(recommendation_list)
recommendation_df.to_csv("F:/user_recommendations_gcn.csv", index=False)
