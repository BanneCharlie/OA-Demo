import request from '@/utils/request'

// 分页查询部门信息
export const GetSysDepartListByPage = (pageDepartQueryRequest) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...pageDepartQueryRequest, // 扩展操作符展开pageRoleQueryRequest的所有属性
  };

  return request({
    url: '/depart/query', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 添加角色
export const SaveSysDepart = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageDepartQueryRequest的所有属性
  };

  return request({
    url: '/depart/add', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 修改角色
export const UpdateSysDepart = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageDepartQueryRequest的所有属性
  };

  return request({
    url: '/depart/modify', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 删除角色
export const DeleteSysDepart = (id) => {
  return request({
    url: `/depart/remove/${encodeURIComponent(id)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 根据部门id获取部门信息
export const GetSysDepartById = (id) => {
  return request({
    url: `/depart/query/${encodeURIComponent(id)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 获取到所有的部门信息
export const GetAllSysDepartList = () => {
  return request({
    url: '/depart/all', // API endpoint
    method: 'post', // 请求方法
  });
}

// 通过属性结构获取到所有的部门信息
export const FindNodes = () =>{
  return request({
    url: '/depart/tree',
    method: 'post',
  })
}

