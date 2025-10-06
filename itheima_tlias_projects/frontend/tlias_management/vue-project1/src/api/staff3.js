import request from '@/utils/request'

// 获取员工列表
export const getStaffList = (name,gender,begin,end,page,pageSize) => {
  return request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
}

// 获取员工详情用于edit查询回显
export const getStaffById = (id) => {
  return request.get(`/emps/${id}`)
}

// 新增员工
export const addStaff = (emp) => {
  return request.post('/emps', emp)
}

// 更新员工信息
export const updateStaff = (emp) => {
  return request.put('/emps', emp)
}



// 批量删除员工
export const DeleteStaff = (ids) => {
  return request.delete(`/emps?ids=${ids}`)
}