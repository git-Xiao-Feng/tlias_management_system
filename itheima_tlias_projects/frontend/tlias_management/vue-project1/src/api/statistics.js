import request from '@/utils/request'


// 获取每个职位的人数
export const empJobDataApi = () => request.get('/report/empJobData')


// 获取员工性别人数
export const empGenderDataApi = () => request.get('/report/empGenderData')


// 获取学员的学历统计
export const studentDegreeDataApi = () => request.get('/report/studentDegreeData')


// 获取每个班级的学员人数统计
export const studentCountDataApi = () => request.get('/report/studentCountData')

