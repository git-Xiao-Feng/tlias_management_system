<script  setup>
import {
  Document,
  Menu as IconMenu,
  HomeFilled,
  PieChart,
  HelpFilled,
  Hide,
} from '@element-plus/icons-vue'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginUser = ref('')

onMounted(() => {
  const userStr = localStorage.getItem('loginUser')
  if(userStr){
    try {
      loginUser.value = JSON.parse(userStr)
    } catch (e) {
      console.error('解析用户信息失败:', e)
    }
  }
  console.log(loginUser.value)
  if (!loginUser.value) {
    ElMessage.error('请先登录！')
    console.log('请先登录！')
    router.push('/')
  }
})

const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}

const quitLogin = () => { 
  localStorage.removeItem('loginUser');
  router.push('/');
}
</script>


<template>
    <div class="common-layout">
    <el-container style="height: 100vh;">
      <!-- header -->
      <el-header class="header">
        
        <span id="tlias-management">tlias-management </span>
        <el-button type="primary" class="logout" @click="quitLogin()">退出登录【{{loginUser.username}}】</el-button>
        
      </el-header>   

      <!-- aside -->
      <el-container>
        <el-aside class="aside">
          
          <el-menu
          router 
         
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-menu-item index="index1">
          
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          
        
        </el-menu-item>


        <el-sub-menu index="2">
          <template #title>
          <el-icon><icon-menu /></el-icon>
          <span>班级学员管理</span>
          </template>
          <el-menu-item index="clazz2"><el-icon><HelpFilled /></el-icon>班级管理</el-menu-item>
            <el-menu-item index="stu2">学员管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="3" >
          <template #title>
          <el-icon><document /></el-icon>
          <span>系统信息管理</span>
          </template>
          <el-menu-item index="dept3">部门管理</el-menu-item>
          <el-menu-item index="staff3">员工管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="4">
            <template #title>
          <el-icon><PieChart /></el-icon>
          <span>数据统计管理</span>
          </template>
            <el-menu-item index="emp4">员工信息统计</el-menu-item>
            <el-menu-item index="stu4">学员信息统计</el-menu-item>
        </el-sub-menu>
      </el-menu>

      
        </el-aside>
        <el-main class="main">
            <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>



    
    
  </div>
</template>

<style>
/* 或者更通用的方法，禁用所有非输入元素的焦点 */
*:not(input):not(textarea) {
  outline: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}
</style>

<style scoped>
/* .logout {
  margin-left: auto;
} */



#tlias-management {
  color: white;
  font-size: 45px;
  font-weight: 600;
  margin-left: 20px;
  

}

.common-layout {
  height: 100vh;
  width: 100%;
}

.header {
  background-color: rgb(51, 126, 204);
  height: 10%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.aside {
  /*background-color: rgb(121, 187, 255);*/
  height: 100%;
  width: 15%;
}


.el-menu-vertical-demo{
  height: 100%;
  border-right: none;
}

</style>


