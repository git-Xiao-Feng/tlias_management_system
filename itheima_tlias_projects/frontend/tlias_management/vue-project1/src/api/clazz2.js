import request from '@/utils/request'


// 获取班级列表
export const getClazzList = (name,begin,end,page,pageSize) => {
 return request.get(`/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
}


// 删除班级
export const deleteClazz = (id) => request.delete(`/clazzs/${id}`)


// 添加班级
export const addClazz = (data) => request.post('/clazzs', data)



// 获取单个班级
export const getClazzOne = (id) => request.get(`/clazzs/${id}`)



// 修改班级
export const updateClazz = (data) => request.put(`/clazzs`, data)

