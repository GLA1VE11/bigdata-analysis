<template>
  <div style="height: 90%; display: flex; align-items: center; justify-content: center; background: linear-gradient(to right, #d3d3d3, #c9a0ff);">
    <div style="display: flex; background-color: white; width: 80%; border-radius: 5px; overflow: hidden">
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <img src="@/assets/login.png" alt="" style="max-width: 100%; max-height: 100%;">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%" :rules="rules" ref="loginRef">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px; margin-top: 30px;">欢迎登录</div>
          <el-form-item prop="username">
            <el-input :prefix-icon="User" size="default" placeholder="请输入账号" v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input :prefix-icon="Lock" size="default" show-password placeholder="请输入密码" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <div style="display: flex; align-items: center;">
              <el-input placeholder="请输入验证码" :prefix-icon="Check" size="default" style="flex: 1" v-model="user.code"></el-input>
              <div style="flex: 1; height: 36px; margin-left: 10px;">
                <valid-code @update:value="getCode" />
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="login">登 录</el-button>
          </el-form-item>
          <div style="display: flex; justify-content: space-between; font-size: 14px;">
            <div>还没有账号？请 <span style="color: #0f9876; cursor: pointer" @click="$router.push('/register')">注册</span></div>
          </div>
        </el-form>
      </div>
    </div>


  </div>
</template>

<script setup>
    import { ref, reactive, onMounted } from 'vue';
    import ValidCode from "@/components/ValidCode";
    import {User, Lock, Check} from "@element-plus/icons-vue";
    import request from "@/network/index.js";
    import router from '@/router/index.js'
    import { useToast } from 'vue-toastification'

    const toast = useToast();
    // State is now a reactive object instead of using the data option
    const user = reactive({
      username: '',
      password: '',
      code: '',
    });

    const code = ref('');
    const loginRef = ref(null);
    
    const rules = {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
      code: [
        {
          validator: (_, value) => {
            if (value === '') {
              return Promise.reject(new Error('请输入验证码'));
            } else if (value.toLowerCase() !== code.value) {
              return Promise.reject(new Error('验证码错误'));
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
    };

    function getCode(value) {
      code.value = value.toLowerCase();
    }

    async function login() {
      // Validate the form first
      const isValid = await loginRef.value.validate();
      if (isValid) {
        // Perform the login request
        try {
          const res = await request.post('http://localhost:8088/login', user);
          // console.log(res);
          if (res.code === '200') {
            // ElMessage.success('登录成功');
            localStorage.setItem("honey-user", JSON.stringify(res.data));
            // // 强制刷新页面，这会导致整个应用重新加载
            // window.location.reload();
            // router.push('/');
            toast.success('登录成功');
            // 延迟1秒后执行重定向到根页面
            setTimeout(() => {
              window.location.href = '/'; // 通过设置 window.location.href 重定向到根页面
            }, 1000); // 1000毫秒 = 1秒
          } else {
            toast.error(res.msg);
          }
        } catch (error) {
          // console.log(error)
          toast.error('请求失败');
          // ElMessage.error('请求失败');
        }
      }
    }

    onMounted(() => {
      // Any onMounted logic here
    });
</script>

<style scoped>

</style>