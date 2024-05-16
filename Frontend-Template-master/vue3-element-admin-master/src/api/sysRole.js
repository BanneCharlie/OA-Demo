import request from '@/utils/request'

// 分页查询角色数据
export const GetSysRoleListByPage = (pageRoleQueryRequest) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...pageRoleQueryRequest, // 扩展操作符展开pageRoleQueryRequest的所有属性
  };

  return request({
    url: '/role/query', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}


// 查询所有的角色数据
export const GetAllRoleList = (id) => {
  return request({
    url: `/role/findRoleInfo/${encodeURIComponent(id)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 添加角色
export const SaveSysRole = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageRoleQueryRequest的所有属性
  };

  return request({
    url: '/role/add', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 修改角色
export const UpdateSysRole = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageRoleQueryRequest的所有属性
  };

  return request({
    url: '/role/modify', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

// 删除角色
export const DeleteSysRole = (id) => {
  return request({
    url: `/role/remove/${encodeURIComponent(id)}`, // 通过路径传递参数
    method: 'post', // 请求方法
  });
}

// 实现菜单的分配
export const DoAssignRoleMenu = (data) => {
  // 确保传递的请求体符合API要求，通常API期望直接接收data对象
  const requestData = {
    ...data, // 扩展操作符展开pageRoleQueryRequest的所有属性
  };

  return request({
    url: '/role/assignMenu', // API endpoint
    method: 'post', // 请求方法
    data: requestData, // 将请求参数放在data字段下
  });
}

