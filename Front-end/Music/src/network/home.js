import { request } from "./index.js";
import axios from 'axios';
// 因为在Index里封装了另一个request(为了加Token), 所以这里删掉了之前项目定义的request. 这些请求都是向网易云音乐的接口请求数据，不是
// localhost:8088, 所以这里用了原始axios.get方法请求。

// 获取 banner 轮播图，type:资源类型，2为 android
export function getbanner() {
  return axios.get("http://music.cpengx.cn/banner").then((response)=>{
      return response;
  })
}

// 获取 推荐歌单数据，limit：获取条数
export function getRecommendedSongs() {
  return axios.get("http://music.cpengx.cn/personalized/song?limit=11").then((response)=>{
      return response;
  })
}
// 获取 推荐歌单数据，limit：获取条数
export function getRecommendedSongs1() {
  // 注意必须是return axios.get, 直接在.then里面调用只是在A里面，外面是收不到的
  return axios.get("http://music.cpengx.cn/personalized/song?limit=30").then((response)=>{
      return response;
  })
}

// 获取歌手排行榜信息
export function getTopSingers() {
  return axios.get("http://music.cpengx.cn/toplist/artist").then((response)=>{
      return response;
  })
}

// 搜索歌曲
export function searchMusic(keyword,limit=30) {
  return axios.get("http://music.cpengx.cn/search?keywords=${keyword}&limit=${limit}").then((response)=>{
      return response;
  })
}