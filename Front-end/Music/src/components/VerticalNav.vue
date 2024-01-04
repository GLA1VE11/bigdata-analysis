<!-- 从小人头像到下面的所有图标 -->

<template>
    <div class="user">
        <router-link to="/login" v-if="!isAuthenticated" class="note">
            <el-button type="text" class="login-button">登录</el-button>
        </router-link>
        <div v-else style="display: flex;">
            <el-avatar :src="avatarUrl" class="avatar"/>
            <el-button type="text" class="register-button" @click="logout">登出</el-button>
        </div>
    </div>
    <div class="list">
        <p>我的音乐</p>
        <div><img src="../assets/最近播放.png" alt="" /><router-link to="/play">最近播放</router-link></div>
        <div><img src="../assets/收藏.png" alt="" /><router-link to="/collect">我的收藏</router-link></div>
        <div><img src="../assets/下载.png" alt="" /><router-link to="/noLogin">下载管理</router-link></div>
        <div><img src="../assets/音乐.png" alt="" /><router-link to="/noLogin">iTunes音乐</router-link></div>
        <div><img src="../assets/云盘.png" alt="" /><router-link to="/noLogin">我的音乐云盘</router-link></div>
        <div><img src="../assets/发现音乐.png" alt="" /><router-link to="/noLogin">发现音乐</router-link></div>
        <div><img src="../assets/私人FM.png" alt="" /><router-link  to="/noLogin">私人FM</router-link></div>
        <div><img src="../assets/视频.png" alt="" /><router-link to="/noLogin">视频</router-link></div>
        <div><img src="../assets/关注.png" alt="" /><router-link to="/noLogin">关注</router-link></div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElButton, ElAvatar } from 'element-plus';
import router from '@/router';
import { useToast } from 'vue-toastification';

const toast = useToast();
// 使用 ref 创建一个响应式的登录状态
const isAuthenticated = ref(checkUserLoggedIn());

// 检查用户是否已登录的函数
function checkUserLoggedIn() {
  const honeyUser = localStorage.getItem('honey-user');
  return honeyUser && JSON.parse(honeyUser).token;
}

// 用户的头像 URL
const avatarUrl = require("@/assets/logo1.png"); // 使用 require 或 import 引入本地图片

// 登录操作后调用此方法更新 isAuthenticated
function afterLogin() {
  isAuthenticated.value = true;
}

// 登出函数
function logout() {
  if (!isAuthenticated.value) {
    toast.warning('您还未登录！');
    return;
  }

  // 清除 localStorage 中的用户信息
  localStorage.removeItem('honey-user');
  // 更新 isAuthenticated 状态
  isAuthenticated.value = false;
  toast.success('成功登出！');
  // 重定向到歌曲列表页面
  router.push('/SongList');
}

// 监听 storage 事件
function handleStorageEvent(event) {
  if (event.key === 'honey-user') {
    isAuthenticated.value = checkUserLoggedIn();
  }
}

onMounted(() => {
  window.addEventListener('storage', handleStorageEvent);
});
</script>


<style scoped>
.user {
  display: flex;
  align-items: center;
  justify-content: center; /* Center the elements horizontally */
  gap: 10px; /* Add some space between the elements */
}
hr {
    opacity: 0.3;
}

.list {
    display: flex;
    flex-direction: column;
    margin: 0;
    padding: 0;
    height: 80%;
    width: 100%;
    font-size: small;
}

.list div {
    padding: 5px;
    height: 35px;
}

.list img {
    margin-left: 20px;
    margin-right: 10px;
    width: 20px;
    height: 20px;
}

.list p {
    margin: 25px 0 10px 18px;
    opacity: 40%;
}

.list div:hover {
    background-color: #ccc8c8;
}

a {
    text-decoration: none;
    color: black;
}
.active {
    color: rgb(189, 49, 49);
}

/* 登录按钮样式 */
/* 登录按钮样式，增大并居中 */
.login-button {
  padding: 20px 40px; /* Increase padding for a larger button */
  font-size: 20px; /* Larger font size */
  border: none;
  border-radius: 50px;
  cursor: pointer;
  background-color: #ff4b2b;
  color: white;
  margin-top: 20px; /* Reset margin */
}

/* 登出按钮样式，和头像用户名一起居中 */
.register-button {
  padding: 0px 20px;
  font-size: 14px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  background-color: #e6e6e6;
  color: #333;
  margin-left: 20px;
  margin-top: 30px;
}

/* 头像样式 */
.avatar {
  --el-avatar-size: 48px; /* Increase the size of avatar */
  margin-right: 20px; /* Reset margin right */
  margin-top: 20px; 
}

/* 用户名样式 */
.username {
  font-size: 14px; /* Set the font size for username */
}

/* 登录按钮悬停效果 */
.login-button:hover {
  background-color: #e53e0e;
}

/* 注册（登出）按钮悬停效果 */
.register-button:hover {
  background-color: #d3d3d3;
}
</style>