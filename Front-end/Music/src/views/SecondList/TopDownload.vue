<template>
  <div>
    <div ref="chartRef" style="width: 100%; height: 500px; margin-top: 2vh;"></div>
    <div class="pagination">
      <el-button @click="changePage(state.currentPage - 1)" :disabled="state.currentPage <= 1">上一页</el-button>
      <span>第 {{ state.currentPage }} 页，共 {{ state.totalPages }} 页</span>
      <el-button @click="changePage(state.currentPage + 1)" :disabled="state.currentPage >= state.totalPages">下一页</el-button>
    </div>
  </div>
</template>

<script setup>
//nextTick 确保在 DOM 更新后再执行初始化。myChart 变量被声明为 let myChart = null; 并且在 onMounted 钩子内部赋值，这样就可以确保我们只在 chartRef 引用的元素存在时初始化图表。
import { onMounted, ref, reactive, nextTick } from 'vue';
import * as echarts from 'echarts';
import { ElButton } from 'element-plus';
import request from "@/network/index.js";

const chartRef = ref(null);
let myChart = null;  // ECharts 实例
let songData = reactive([]); // 这应该是一个100个元素的数组
const state = reactive({
  currentPage: 1,   // 当前页码
  pageSize: 10,     // 每页显示的条目数
  totalPages: 0     // 总页数
});

const displayPage = () => {
  // 计算当前页的数据范围
  const startIndex = (state.currentPage - 1) * state.pageSize;
  const endIndex = startIndex + state.pageSize;
  const pageData = songData.slice(startIndex, endIndex);
  // console.log(songData, pageData);
  // 配置 ECharts 的选项
  const option = {
    title: {
      text: 'Top50 Download',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: pageData.map(item => `song ${item.songId}`),
      axisTick: {
        alignWithLabel: true
      },
      axisLabel: {
          interval: 0, // 强制显示所有标签
          rotate: 45, // 如果标签仍然重叠，可以通过旋转标签的方式减少重叠
      },
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: pageData.map(item => item.songInitPlays),
      type: 'bar',
      barWidth: '60%',
      label: {
        show: true,
        position: 'top'
      },
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
          offset: 0, color: '#83bff6'
        }, {
          offset: 0.5, color: '#188df0'
        }, {
          offset: 1, color: '#188df0'
        }]),
        shadowBlur: 10,
        shadowColor: 'rgba(0, 0, 0, 0.5)'
      },
      emphasis: {
        itemStyle: {
          color: '#76a7fa'
        }
      }
    }]
  };

  // 使用配置的选项设置 ECharts 实例
  myChart.setOption(option);
};

// 获取艺术家数据
const fetchTopsongs = async () => {
  try {
    const response = await request.get('http://localhost:8088/top_download?top=50');
    songData = response.data; // 假设返回的数据是前50名艺术家
    // console.log(songData);
    state.totalPages = Math.ceil(songData.length / state.pageSize);
    displayPage(); // 显示第一页
  } catch (error) {
    console.error('Error fetching top songs:', error);
  }
};

// 切换页面
const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= state.totalPages) {
    state.currentPage = newPage;
    displayPage();
  }
};

onMounted(() => {
  nextTick(() => {
    if (chartRef.value !== null) {
      // console.log(chartRef.value);
      myChart = echarts.init(chartRef.value); // 在这里初始化 ECharts 实例
      fetchTopsongs(); // 然后获取数据并显示
    }
  });
});
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination span {
  margin: 0 15px;
}

/* 适配 Element UI 按钮的样式 */
.el-button {
  margin: 0 5px;
}
</style>
