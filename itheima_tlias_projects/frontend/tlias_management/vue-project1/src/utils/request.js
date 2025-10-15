import axios from 'axios'
import router from '../router'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

request.interceptors.request.use(config => {
  // Do something before request is sent
  let loginUser = JSON.parse(localStorage.getItem('loginUser'))
   // console.log(localStorage.getItem('loginUser'))
    if (loginUser) {
      config.headers.token = loginUser.token
    }
  return config
}, error => {
  // Do something with request error
  return Promise.reject(error)
})

request.interceptors.response.use(response => {
  // Do something with response data
  
  return response.data
}, error => {
  
  console.log(error.response.status)
  if (error.response.status === 401) {
    localStorage.removeItem('loginUser')
    router.push('/')
    ElMessage.error('请先登录')
  }
  // Do something with response error
  return Promise.reject(error)
})

export default request