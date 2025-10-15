<script setup>

import { ref } from 'vue'
import { loginApi } from '@/api/login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const loginForm = ref({
    username: '',
    password: ''
})

const onSubmit = async () => {
    console.log(loginForm.value)
    const res = await loginApi(loginForm.value)
    if (res.code) {
        ElMessage.success('登录成功')
        localStorage.setItem('loginUser', JSON.stringify(res.data))
        router.push('/layout')
    } else {
        console.log(res)
        ElMessage.error('登录失败')
    }
}

const onReset = () => {
    loginForm.value = {
        username: '',
        password: ''
    }
}
</script>

<template>

    <div class="login-container">
    <div class="login-form">
        <h2 class="title">Tlias-Management System</h2>
        <el-form :model="loginForm" label-width="80px" style="max-width: 600px" autocomplete="off" >
    
            <el-form-item label="Username" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" autocomplete="new-username"/>
            </el-form-item>
            
            <el-form-item label="Password" prop="password">
            <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" autocomplete="new-password"/>
            </el-form-item>
        
            <el-form-item>
            <el-button class="button"  type="primary" @click="onSubmit">Login</el-button>
            <el-button class="button" type="info" @click="onReset">Reset</el-button>
            </el-form-item>
        </el-form>
    </div>
    </div>


</template>

<style>

.login-container{
    padding: 15%;
    height: 100%;
    /* background-image: url('../../assets/bg1.jpg'); */
    background-repeat: no-repeat;
    background-size: cover;
    /* background-color: rgb(68, 190, 190); */
}

.login-form{
    max-width: 400px;
    padding: 30px;
    margin: 0 auto;
    border: 1px solid #e0e0e0;
    border-radius: 10px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    background-color: white;
}

.title {
  font-size: 30px;
  font-family: '楷体';
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.button {
  margin-top: 30px;
  width: 120px;
}

</style>