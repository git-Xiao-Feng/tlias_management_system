<script setup>
import * as echarts from 'echarts/core';
import { GridComponent, TitleComponent, TooltipComponent, LegendComponent } from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import { onMounted, onUnmounted, ref } from 'vue';
import { empJobDataApi , empGenderDataApi} from '@/api/statistics';

import { PieChart } from 'echarts/charts';
import { LabelLayout } from 'echarts/features';

// 注册所需的组件
// 增加了常用的Title和Tooltip组件，使图表功能更完整
 echarts.use([GridComponent, TitleComponent, TooltipComponent, LegendComponent, BarChart, CanvasRenderer, PieChart, LabelLayout]);

const chartRef = ref(null); // 使用ref引用DOM元素
let myChart = null;

onMounted(async () => {
  // 确保在组件挂载后再初始化图表
  await initChart();
  await initGenderChart(); // 添加环形图表初始化
  // 添加窗口大小变化的监听，使图表可以自适应
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  // 组件卸载时清理图表实例，防止内存泄漏
  window.removeEventListener('resize', handleResize);
  if (myChart) {
    myChart.dispose();
    myChart = null;
  }
  if (genderChart) {
    genderChart.dispose();
    genderChart = null;
  }
});

async function initChart() {
  if (chartRef.value) {
    myChart = echarts.init(chartRef.value);
    
    try {
      // 请求接口获取数据
      const res = await empJobDataApi();
      console.log('获取数据成功:', res);
      
      // 假设接口返回的数据结构为 { xData: [...], yData: [...] }
      // 如果实际数据结构不同，请根据实际情况调整
      const xAxisData = res.data.jobList || [];
      const seriesData = res.data.dataList || [];
      console.log('xAxisData:', xAxisData);
      console.log('seriesData:', seriesData);
      
      const option = {
        xAxis: {
          type: 'category',
          data: xAxisData // 动态赋值x轴数据
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: seriesData, // 动态赋值y轴数据
            type: 'bar'
          }
        ]
      };
      
      myChart.setOption(option);
    } catch (error) {
      console.error('获取数据失败:', error);
    }
  }
}

function handleResize() {
  if (myChart) {
    myChart.resize();
  }
  if (genderChart) {
    genderChart.resize();
  }
}



// 添加环形图表相关的ref和变量
const genderChartRef = ref(null); // 环形图表的DOM引用
let genderChart = null; // 环形图表实例


// 添加环形图表初始化函数
async function initGenderChart() {
  if (genderChartRef.value) {
    genderChart = echarts.init(genderChartRef.value);
    
    try {
      // 请求接口获取性别数据
      const res = await empGenderDataApi();
      console.log('获取性别数据成功:', res);
      
      // 假设接口返回的数据结构为 { data: [{name: '男', value: 100}, {name: '女', value: 80}] }
      // 如果实际数据结构不同，请根据实际情况调整
      const genderData = res.data || [];
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '性别分布',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: genderData
          }
        ]
      };
      
      genderChart.setOption(option);
    } catch (error) {
      console.error('获取性别数据失败:', error);
    }
  }
}

</script>

<template>
  
  <!-- 职位人数柱形图 -->
<div style="display: flex; gap: 20px;">
  <div style="flex: 1;">
  <span style="display: block; text-align: center;">职位信息统计</span>
  <div ref="chartRef" style="width: 100%; height: 600px;"></div>
  </div>

  <!-- 性别分布环形图 -->
  <div style="flex: 1;">
  <span style="display: block; text-align: center;">员工性别分布</span>
  <div ref="genderChartRef" style="width: 100%; height: 600px;"></div>
  </div>
</div>
</template>

<style>
</style>