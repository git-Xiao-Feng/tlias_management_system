import request from '@/utils/request'

// 获取学员列表
export const getStuList = (name,degree,clazzId,page,pageSize) => {
 return request.get(`/students?name=${name}&degree=${degree}&clazzId=${clazzId}&page=${page}&pageSize=${pageSize}`)
}


// 删除学员
export const deleteStu = (ids) => request.delete(`/students/${ids}`)


// 添加学员
export const addStu = (data) => request.post('/students', data)



// 获取单个学员信息
export const getStuOne = (id) => request.get(`/students/${id}`)



// 修改学员信息
export const updateStu = (data) => request.put(`/students`, data)


//违纪扣分
export const deductScore = (id,score) => request.put(`/students/violation/${id}/${score}`)

//
