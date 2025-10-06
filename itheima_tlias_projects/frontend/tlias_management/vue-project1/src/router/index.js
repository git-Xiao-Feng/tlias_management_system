import { createRouter, createWebHistory } from 'vue-router'
import layout from '@/views/layout/index.vue'
import Clazz2 from '@/views/clazz2/index.vue'
import Dept3 from '@/views/dept3/index.vue'
import Index from '@/views/index1/index.vue'
import Staff3 from '@/views/staff3/index.vue'
import Stu2 from '@/views/stu2/index.vue'
import { reactive } from 'vue'
// 可以添加其他页面组件
// import Home from '@/views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'layout',
    component: layout,
    redirect: '/index1',
    children:[
    {path: '/clazz2', name: 'Clazz2', component: Clazz2},
    {path: '/dept3', name: 'Dept3', component: Dept3},
    {path: '/index1', name: 'Index1', component: Index},
    {path: '/staff3', name: 'Staff3', component: Staff3},
    {path: '/stu2', name: 'Stu2', component: Stu2}
    ]
  },
  
  // 可以在这里添加更多路由规则
  // {
  //   path: '/about',
  //   name: 'About',
  //   component: () => import('@/views/About.vue')
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router