import pandas as pd
import tensorflow as tf
import numpy as np
from sklearn.model_selection import train_test_split
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Embedding, Dense, LSTM, Attention
from tensorflow.keras.preprocessing.sequence import pad_sequences

# 数据读取
data_path = "F:/mars_tianchi_user_action.csv"
df = pd.read_csv(data_path)
df.columns = ["user_id", "song_id", "rating"]

# 数据预处理：将用户互动转换成序列
user_groups = df.groupby("user_id").agg(list).reset_index()
sequence_length = 10  # 定义序列长度
sequences = []
for _, row in user_groups.iterrows():
    song_ids = row['song_id'][:sequence_length]
    sequences.append(song_ids)

# 创建歌曲 ID 到索引的映射
song_id_to_idx = {song_id: idx for idx, song_id in enumerate(df['song_id'].unique())}
sequences_idx = [[song_id_to_idx[song] for song in seq] for seq in sequences]

# 序列填充
X_padded = pad_sequences(sequences_idx, maxlen=sequence_length, padding='post')

# 模型构建
def build_stamp_model(sequence_length, num_songs):
    inputs = Input(shape=(sequence_length,))
    embedding = Embedding(input_dim=num_songs, output_dim=50)(inputs)
    lstm_out = LSTM(50, return_sequences=True)(embedding)
    attention = Attention()([lstm_out, embedding])
    lstm_out_last = lstm_out[:, -1, :]
    concat = tf.concat([lstm_out_last, attention[:, -1, :]], axis=-1)
    outputs = Dense(num_songs, activation='softmax')(concat)
    model = Model(inputs=inputs, outputs=outputs)
    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy')
    return model

# 训练模型
model = build_stamp_model(sequence_length, len(song_id_to_idx))
y = np.array([seq[-1] for seq in X_padded])  # 最后一首歌作为目标
X_train, X_test, y_train, y_test = train_test_split(X_padded, y, test_size=0.2)
model.fit(X_train, y_train, epochs=5, validation_data=(X_test, y_test))

# 生成推荐
recommendations = {}
for user_id, seq in zip(user_groups['user_id'], X_padded):
    predicted = model.predict([seq.reshape(1, -1)])
    top_songs_idx = predicted.argsort()[0][-10:]  # 获取前 10 个推荐
    recommendations[user_id] = [list(song_id_to_idx.keys())[idx] for idx in top_songs_idx]

# 保存推荐结果到CSV
recommendation_list = []
for user, recs in recommendations.items():
    recs_dict = {'user_id': user}
    for i, song in enumerate(recs):
        recs_dict[f'recommended_song_{i+1}'] = song
    recommendation_list.append(recs_dict)

recommendation_df = pd.DataFrame(recommendation_list)
recommendation_df.to_csv("F:/user_recommendations_stamp.csv", index=False)
