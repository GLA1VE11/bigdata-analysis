//该文件专门用于创建整个应用的路由器
// 引入路由函数和路由模式函数
import { createRouter, createWebHashHistory } from 'vue-router'
// 引入组件
import SongList from '@/views/SecondList/SongList.vue'
import ContentList from '@/views/FirstList/ContentList.vue'
import Play from '@/views/FirstList/Play.vue'
import Collect from '@/views/FirstList/Collect.vue'
import noLogin from '@/views/noLogin.vue'
import Login2 from '@/views/SecondList/Login2.vue'
import TopSong from '@/views/SecondList/TopSong.vue'
import Toplist_wyy from '@/views/SecondList/Toplist_wyy.vue'
import TopArtist from '@/views/SecondList/TopArtist.vue'
import TopCollect from '@/views/SecondList/TopCollect.vue'
import TopDownload from '@/views/SecondList/TopDownload.vue'
import { useToast } from 'vue-toastification'

const toast = useToast();

// 创建并暴露一个路由器
// 命名路由：简化路由的跳转
const routes = [
    {
        path: '/SongList',
        component: SongList,
        meta: { requiresAuth: true },
    },  //多级路由：path不写/，children里写子路由
    {
        path: '/ContentList',
        component: ContentList,
    },
    {
        path: '/noLogin',
        component: noLogin
    },
    {
        path: '/login',
        component: Login2
    },
    {
        path: '/',
        redirect: '/ContentList'
    },
    {
        path: '/TopList_wyy',
        component: Toplist_wyy,
    },
    {
        path: '/TopArtist',
        component: TopArtist,
    },
    {
        path: '/TopCollect',
        component: TopCollect,
    },
    {
        path: '/TopDownload',
        component: TopDownload,
    },
    {
        path: '/TopSong',
        component: TopSong,
    },
    {
        path: '/collect',
        component: Collect,
        meta: { requiresAuth: true },
    },
    {
        path: '/play',
        component: Play,
        meta: { requiresAuth: true },
    },
]

const router = createRouter({
    routes,
    history: createWebHashHistory()
})


// 用于检查用户是否登录的函数
export function isLoggedIn() {
    // 尝试获取存储在localStorage中的用户信息
    const honeyUser = localStorage.getItem('honey-user');
    if (honeyUser) {
      // 解析用户信息
      const userData = JSON.parse(honeyUser);
      // 检查token是否存在
      return !!userData.token;
    }
    return false; // 如果没有honey-user数据，则用户没有登录
  }
  
router.beforeEach((to, from, next) => {
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    const goingToLoginPage = to.path === '/login';
    const loggedIn = isLoggedIn();

    if (requiresAuth && !loggedIn) {
        toast.warning('您需要登录，才能访问该页面。');
        // 如果路由需要认证，并且用户未登录，重定向到登录页面
        next('/noLogin');
    } else if (goingToLoginPage && loggedIn) {
        // 如果用户已登录并尝试访问登录页，则显示提示并停留在当前页面
        toast.warning('您已经登录，无需再次登录。');
        next(false); // 使用 next(false) 来取消当前的导航
    } else {
        // 如果路由不需要认证或者用户已登录，则正常导航
        next();
    }
});

  
export default router