import request from '@/utils/request'

const api_name = '/menu'
// 分页列表
export const FindNodes = () => {
  return request({
    url: `${api_name}/query`,
    method: 'get',
  })
}

// 保存信息
export const SaveMenu = sysMenu => {
  return request({
    url: `${api_name}/add`,
    method: 'post',
    data: sysMenu,
  })
}

// 修改信息
export const UpdateSysMenuById = sysMenu => {
  return request({
    url: `${api_name}/modify`,
    method: 'put',
    data: sysMenu,
  })
}

// 根据id删除数据
export const RemoveSysMenuById = id => {
  return request({
    url: `${api_name}/remove/${id}`,
    method: 'delete',
  })
}

// 查询所有菜单  以及当前角色的拥有的菜单
export const GetMenuListByRoleId = (roleId) => {
  return request({
    url: `${api_name}/findMenuInfo/${roleId}`,
    method: 'get',
  })
}

// 根据当前用户 id  获取需要配置的  实现动态路由
export const GetMenus = () => {
  return request({
    url: `/menu/menus`, // 通过路径传递参数
    method: 'get', // 请求方法
  });
}

