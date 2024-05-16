import request from '@/utils/request'

// 登录接口
export const Login = data => {
  return request({
    url: '/user/login',
    method: 'post',
    data,
  })
}

// 退出功能
export const Logout = () => {
  return request({
    url: '/user/logout',
    method: 'post',
  })
}

// 获取登录用户信息
export const GetUserinfo = () => {
  return request({
    url: '/user/getUserInfo',
    method: 'get',
  })
}

