
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import Vue3ColorPicker from "vue3-colorpicker";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import "vue3-colorpicker/style.css";
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
// import {Swipe, SwipeItem,Lazyload} from 'vant';
// import 'vant/lib/index.css';

 //将这个vue3app全局挂载到#app元素上
const app = createApp(App);   
app.use(router).use(Vue3ColorPicker).use(store).use(ElementPlus).use(Toast, {
            
    // 每个 toast 显示的位置
    position: 'top-center',
  
    // 每个 toast 持续显示的时间（毫秒），设为 false 则不会自动关闭
    timeout: 1500,
  
    // 是否显示关闭按钮
    closeOnClick: true,
  
    // 是否可以通过点击或拖动来关闭 Toast
    pauseOnFocusLoss: true,
  
    // 当有焦点时，暂停计时
    pauseOnHover: true,
  
    // 是否显示进度条
    showCloseButtonOnHover: false,
  
    // 关闭按钮的 HTML
    closeButton: 'button',
  
    // Toast 进入和退出的动画
    transition: 'Vue-Toastification__bounce',
  
    // 最大的 Toast 数量
    maxToasts: 5,
  
    // 是否可以使用HTML内容
    allowHtml: false,
  
    // 其他全局配置...
  
}).mount('#app');   



