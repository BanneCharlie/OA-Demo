// 导入组件
const Layout = () => import('@/layout/index.vue')
const sysRole = () => import('@/views/system/sysRole.vue')
const sysUser = () => import('@/views/system/sysUser.vue')
const sysMenu = () => import('@/views/system/sysMenu.vue')

const sysDepartment = () => import('@/views/system/sysDepartment.vue')

const sysFile = () => import('@/views/system/sysFile.vue')

// 导出该组件
export default([
  {
    path: "/system",
    component: Layout,
    name: 'system',
    meta: {
      title: '系统管理',
    },
    icon: 'Location',
    children: [
      {
        path: '/role',
        name: 'sysRole',
        component: sysRole,
        meta: {
          title: '角色管理',
        },
        hidden: false
      },
      {
        path: '/user',
        name: 'sysUser',
        component: sysUser,
        meta: {
          title: '用户管理',
        },
        hidden: false
      },
      {
        path: '/menu',
        name: 'sysMenu',
        component: sysMenu,
        meta: {
          title: '菜单管理',
        },
        hidden: false
      },
      {
        path: '/depart',
        name: 'sysDepartment',
        component: sysDepartment,
        meta: {
          title: '部门管理',
        },
        hidden: false
      },
      {
        path: '/file',
        name: 'sysFile',
        component: sysFile,
        meta: {
          title: '文件管理',
        },
        hidden: false
      }
    ]
  }
])