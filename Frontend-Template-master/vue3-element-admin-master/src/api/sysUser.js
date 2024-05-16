import request from '@/utils/request'

// 分页查询部门信息
export const GetSysUserListByPage = (pageQueryRequest) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...pageQueryRequest, // 扩展操作符展开pageRoleQueryRequest的所有属性
  };

  return request({
    url: '/user/query', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 添加用户
export const SaveSysUser = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageDepartQueryRequest的所有属性
  };

  return request({
    url: '/user/add', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 修改用户
export const UpdateSysUser = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageDepartQueryRequest的所有属性
  };

  return request({
    url: '/user/modify', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 删除用户
export const DeleteSysUser = (id) => {
  return request({
    url: `/user/remove/${encodeURIComponent(id)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 根据用户id 获取到对应的用户信息  可以获取到部门id
export const GetSysUserById = (id) => {
  return request({
    url: `/user/query/${encodeURIComponent(id)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 实现角色的分配
export const DoAssignUserRole = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageDepartQueryRequest的所有属性
  };

  return request({
    url: '/user/assignRole', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 部门树 查询用户信息
export const GetSysUserListByDepartId = (id,parentId) => {
  return request({
    url: `/user/${encodeURIComponent(id)}/${encodeURIComponent(parentId)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 根据用户Id 查询 对应的角色id
export const GetRoleIdByUserId = () => {
  return request({
    url: '/user/queryRoleId' ,// 通过路径传递参数
    method: 'post', // 请求方法
  });
}


// 修改用户的状态
export const UpdateSysUserStatus = (userId,status) => {

  return request({
    url: `/user/modifyStatus/${userId}/${status}`, // API endpoint
    method: 'post', // 请求方法
  });
}

// 查询到未被 禁用的用户信息
export const GetSysUserListNotForbidden = () => {
  return request({
    url: '/user/queryAll', // API endpoint
    method: 'post', // 请求方法
  });
}


