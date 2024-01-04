<template>
    <div class="user-play-history">
      <!-- 用户信息区域 -->
      <div class="user-info">
        <img src="@/assets/logo1.png" alt="用户头像" style="height: 30px;">
        <h3 class="user-name">{{ username }}</h3>
      </div>
  
      <!-- 歌曲列表 -->
      <el-table
        :data="playHistory"
        stripe
        style="width: 100%; margin-top: 20px">
        <el-table-column
          label="封面"
          width="100">
          <template #default="{ row }">
            <!-- 使用随机图片 -->
            <el-image
              style="width: 50px; height: 50px"
              :src="`https://picsum.photos/50/50?random=${row.songId}`"
              fit="fill"
              :alt="row.name"></el-image>
          </template>
        </el-table-column>
        <el-table-column
          prop="songId"
          label="歌曲 ID">
        </el-table-column>
        <el-table-column
          prop="artistId"
          label="艺术家 ID">
        </el-table-column>
        <el-table-column
          prop="songInitPlays"
          label="播放次数">
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import request from "@/network/index.js";
  import { ElTable, ElTableColumn, ElImage } from 'element-plus';
  
  const playHistory = ref([]);
  const username = ref('');
  
  const fetchPlayHistory = async () => {
    try {
      const response = await request.get('/play_history');
      playHistory.value = response.data.map(item => ({
        ...item,
        cover: `https://picsum.photos/50/50?random=${item.songId}` // 假设每首歌曲根据songId获取一个随机图片
      }));
    } catch (error) {
      console.error(error);
    }
  };
  
  onMounted(() => {
    username.value = JSON.parse(localStorage.getItem("honey-user"))?.username || '未知用户';
    fetchPlayHistory();
  });
  </script>
  
  <style scoped>
  /* ...之前的样式... */
  </style>
  
  
  <style scoped>
  .user-play-history {
    padding: 20px;
    max-width: 800px;
    margin: auto;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    flex-direction: column;
  }
  
  .user-name {
    margin-top: 0px;
  }

  .el-table {
    overflow-y: auto; /* 允许垂直方向上溢出的内容滚动 */
    max-height: 450px; /* 设置表格的最大高度 */
  }
  </style>
  