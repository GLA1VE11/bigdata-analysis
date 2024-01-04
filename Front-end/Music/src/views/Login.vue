<template>
    <img class="bg" src="@/assets/网易云音乐.jpeg" />
    <div class="modal-overlay">
      <div class="modal-container">
        <div class="modal-header">
          <h2>登录 / 注册</h2>
        </div>
        <div class="modal-body">
          <input type="text" placeholder="用户名" v-model="username" required>
          <input type="password" placeholder="密码" v-model="password" required>
        </div>
        <div class="modal-footer">
          <button @click="handleLogin">登录</button>
          <button @click="handleRegister">注册</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import router from '@/router/index.js'
  import axios from 'axios';
  
  let username = ref('');
  let password = ref('');
  
  const handleLogin = async () => {
    console.log('处理登录', username.value, password.value);
    
    try {
      // const params = new URLSearchParams();
      // params.append('username', username.value);
      // params.append('password', password.value);
      // 使用URLSearchParams来构造请求的负载。这会导致发送的Content-Type为application/x-www-form-urlencoded，而你的Spring Boot后端则期望的是application/json格式的负载，因为你使用了@RequestBody注解。因为axios默认使用application/json作为Content-Type，所以这应该与你的Spring Boot后端期望的格式匹配。
      const userData = {
        username: username.value,
        password: password.value
      };

      const response = await axios.post('http://localhost:8088/login', userData);
      console.log(response);
      
      
      // 判断响应状态码
      if (response.status === 200) {
        // 假设后端返回的用户对象在data属性中，并且用户的ID在id字段
        const user = response.data;

        // 登录成功后存储用户信息
        localStorage.setItem('userID', user.id);
        localStorage.setItem('userInfo', JSON.stringify(user)); // 存储整个用户对象
        alert('登录成功！');
        console.log(localStorage);
        // 登录成功，重定向到首页
        router.push('/');
      } else {
        // 如果状态码不是200，则假定登录失败，弹窗提示
        alert('用户名或密码错误！');
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        // 如果响应状态码是401，也弹窗提示
        alert('用户名或密码错误！');
        username.value = password.value = "";
      } else {
        // 其他错误，可以提示网络或服务器问题
        console.error('登录请求出错:', error);
        alert('登录请求失败，请稍后再试。');
      }
    }
  };
  const handleRegister = () => {
    console.log('处理注册');
    router.push("/")
    // 实现注册逻辑，如跳转到注册页面或显示注册表单
  };
  </script>
  
  <style scoped>
  .bg {
    margin-top: 150px;
    width: 100%;
    opacity: 0.5;
    filter: blur(6px);
}
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  
  .modal-container {
    background: #fff;
    padding: 20px;
    border-radius: 5px;
    width: 90%;
    max-width: 400px;
  }
  
  .modal-header h2 {
    text-align: center;
    margin-bottom: 20px;
  }
  
  .modal-body {
    margin-bottom: 20px;
  }
  
  .modal-body input {
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
    box-sizing: border-box;
  }
  
  .modal-footer {
    text-align: right;
  }
  
  .modal-footer button {
    padding: 10px 20px;
    margin-left: 10px;
    border: none;
    border-radius: 5px;
    background-color: #5c8df6;
    color: white;
    cursor: pointer;
  }
  
  .modal-footer button:hover {
    background-color: #4a77d4;
  }
  </style>
  