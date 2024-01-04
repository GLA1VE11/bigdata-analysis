import pandas as pd
import random
import networkx as nx

# 读取数据
data_path = "F:/mars_tianchi_user_action.csv"
df = pd.read_csv(data_path)
df.columns = ["user_id", "song_id", "rating"]

# 创建图
G = nx.Graph()
for index, row in df.iterrows():
    G.add_edge(row["user_id"], row["song_id"], weight=row["rating"])

# 随机游走函数
def random_walk(G, start_node, walk_length):
    path = [start_node]
    current_node = start_node
    for _ in range(walk_length):
        neighbors = list(G.neighbors(current_node))
        if not neighbors:
            break
        next_node = random.choice(neighbors)
        path.append(next_node)
        current_node = next_node
    return path

# 对每个用户进行随机游走，生成推荐
recommendations = {}
for user in set(df["user_id"]):
    walk = random_walk(G, user, 10)  # 假设游走长度为10
    song_counts = {}
    for node in walk:
        if node in df["song_id"].values:
            song_counts[node] = song_counts.get(node, 0) + 1
    sorted_songs = sorted(song_counts, key=song_counts.get, reverse=True)[:10]
    recommendations[user] = sorted_songs

# 保存推荐结果到CSV
recommendation_list = []
for user, recs in recommendations.items():
    recs_dict = {'user_id': user}
    for i, song in enumerate(recs):
        recs_dict[f'recommended_song_{i+1}'] = song
    recommendation_list.append(recs_dict)

recommendation_df = pd.DataFrame(recommendation_list)
recommendation_df.to_csv("F:/user_recommendations_graph.csv", index=False)
