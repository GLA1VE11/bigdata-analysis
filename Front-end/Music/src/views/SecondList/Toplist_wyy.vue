<template>
    <div class='toplist' id="main">
    </div>
    <button @click="prevPage" :disabled="currentPage === 1" id="preButton">上一页</button>
    <button @click="nextPage" :disabled="currentPage === pageCount" id="'nxtButton'">下一页</button>
</template>


<script setup>
import { getTopSingers } from '@/network/home'
import { onMounted, reactive, toRefs, ref, computed } from 'vue';
import * as echarts from 'echarts';

// 分页数据
const pageSize = 5; // 每页显示5个
let currentPage = ref(1); // 当前页码，初始为1
const pageCount = ref(0);
const currentSingers = computed(() => {
  let start = (currentPage.value - 1) * pageSize;
  let end = start + pageSize;
  return allSingers.slice(start, end);
});


// 定义响应式数据
const state = reactive({
  option: {
    title: {
        text: 'Top歌手实时分数',
        subtext: '单位：千万',
        left: 'center', // 标题居中
    },
    tooltip: {},
    xAxis: {
        data: [], // x轴的数据，后面将会填充歌手的名字
        axisLabel: {
            interval: 0, // 强制显示所有标签
            rotate: 45, // 如果标签仍然重叠，可以通过旋转标签的方式减少重叠
            // 其他样式配置...
        },
    },
    yAxis: {
        axisLabel: {
            formatter: function(value) {
                // 假设所有值都在百万以上，您可以将它们转换为'万'的单位
                return (value / 10000000) + '千万';
            }
        }
    },
    series: [{
        name: 'Score',
        type: 'bar',
        data: [], // y轴的数据，后面将会填充歌手的分数
        barWidth: '50%', // 设置柱状的宽度
        barGap: '10%',   // 在同一类目中的柱状间隔为类目宽度的10%
        itemStyle: {
            barBorderRadius: 5,
            borderWidth: 1,
            borderType: 'solid',
            borderColor: '#73c0de',
            shadowColor: '#5470c6',
            shadowBlur: 3
        },
        showBackground: true,
        backgroundStyle: {
            color: 'rgba(220, 220, 220, 0.8)'
        },
        label: {
            show: true, // 开启显示
            position: 'top', // 在上方显示
            textStyle: { // 数值样式
                color: 'black',
                fontSize: 10
            }
        },
    }]
  }
});

// 使用toRefs可以解构出state中的属性，同时保持它们的响应性
let allSingers = reactive([]); // 这应该是一个100个元素的数组
const { option } = toRefs(state);

const ShowBar = function(){  
    // console.log(currentPage.value, pageCount.value);
    var myChart = echarts.init(document.getElementById('main'));
    // 更新option中的数据
    state.option.series[0].data = currentSingers.value.map(singer => singer.score);
    state.option.xAxis.data = currentSingers.value.map(singer => singer.name);
    // 绘制图表
    myChart.setOption(state.option, true); // true表示不合并，而是全部替换现有的option
};

const goToPage = (page) => {
    if (page >= 1 && page <= pageCount.value) {
        currentPage.value = page;
        ShowBar(); // 每次翻页都需要重新渲染图表
    }
};

const nextPage = () => {
    goToPage(currentPage.value + 1);
};

const prevPage = () => {
    goToPage(currentPage.value - 1);
};


const initTopSingers = async () => {
    const { data: res } = await getTopSingers();

    allSingers = res.list.artists;
    pageCount.value = Math.ceil(allSingers.length / pageSize); // 计算页数
};

onMounted(async () => {
    await initTopSingers();
    ShowBar(); // 确保图表在获取到数据之后再渲染
});

</script>


<style>
.toplist {
    width: 400px;
    height: 50%;
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
    top: 20px;
    left: 20px;
}
</style>