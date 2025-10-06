import request from '@/utils/request'



//查询所有部门
export const getall = () => {
  return request.get('/depts')
}


//增加部门
export const adddept = (data) => {
  return request.post('/depts', data)
}

//删除部门
export const deletedept = (id) => {
  return request.delete('/depts', { params: { id } })           //查询参数
}


//修改部门
export const updatedept = (data) => {
  return request.put('/depts', data)           //请求体参数
}


//查询单个部门
export const getByIddept = (id) => {
  return request.get(`/depts/${id}`);             // ${id}路径参数
}