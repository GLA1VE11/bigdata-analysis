<template>
    <div style="display: flex;">
        <ul class="nav">
            <li><router-link active-class="active" to="/ContentList">每日推荐</router-link></li>
            <li><router-link active-class="active" to="/SongList">个性歌单</router-link></li>
            <li><router-link active-class="active" to="/TopArtist">歌手排行榜</router-link></li>
            <li><router-link active-class="active" to="/TopSong">播放排行榜</router-link></li>
            <li><router-link active-class="active" to="/TopDownload">下载排行榜</router-link></li>
            <li><router-link active-class="active" to="/TopCollect">收藏排行榜</router-link></li>
        </ul>
        <ul class="search">
            <li><input type="text" placeholder="搜索"></li>
            <li><img src="../assets/设置.png" alt="" /></li>
            <li><img src="../assets/邮件.png" alt="" /></li>
            <li><img src="../assets/皮肤.png" alt="" /></li>
            <li><img src="../assets/重叠.png" alt="" /></li>
        </ul>
        
    </div>
</template>

<script setup>

</script>

<style scoped>
/* 导航栏——搜索框以左 */
.nav {
    height: 64.6px;
    width: 70%;
    float: left;
    background-color: #f6f0f0;
    font-family: '微软雅黑';
    font-size: 14px;
}
.nav li {
    float: left;
    list-style-type: none;
    margin: 14px;
    padding-top: 6px;
    opacity: 60%;
}
a {
    text-decoration: none;
    color: black;
}
.active {
    color: rgb(189, 49, 49);
}
/* 导航栏——搜索框以右 */
.search {
    float: left;
    width: 40%;
    height: 64.6px;
    background-color: #f6f0f0;
}
.search li {
    padding-top: 7px;
    float: left;
    margin: 15px 8px;
    list-style-type: none;
}
.search img {
    /* margin-top: 10px; */
    width: 17px;
    height: 17px;
}
/* 搜索框 */
input {
    
    background-image: url(../assets/搜索.png);
    background-size: 15px,15px;
    background-repeat: no-repeat;
    background-position: 10px;

    margin-left: 10px;
    padding-left: 30px;
    height: 25px;
    width:120px;
    border-style: none;
    border-radius: 20px;
    outline: none;   /* 点击文本框 边框样式去除 */
    opacity: 0.7;
}
</style>