package com.banne.template.service.imp;

import com.banne.template.common.utils.SnowflakeIdGeneratorUtil;
import com.banne.template.mapper.RoleMapper;
import com.banne.template.mapper.RoleMenuMapper;
import com.banne.template.mapper.UserRoleMapper;
import com.banne.template.model.dto.AssignMenu;
import com.banne.template.model.dto.PageRoleQueryRequest;
import com.banne.template.model.entity.Role;
import com.banne.template.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImp implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private SnowflakeIdGeneratorUtil snowflakeIdGeneratorUtil;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public Long roleAdd(Role role) {
        long id = snowflakeIdGeneratorUtil.nextId(10);
        role.setId(id);
        Long result = roleMapper.roleAdd(role);
        return result;
    }

    @Override
    public Long roleRemove(long id) {
        Long result = roleMapper.roleRemove(id);
        return result;
    }

    @Override
    public Long roleUpdate(Role role) {
        Long result = roleMapper.roleUpdate(role);
        return result;
    }

    @Override
    public Role roleQueryById(long id) {
        Role role = roleMapper.roleQueryById(id);
        return role;
    }

    @Override
    public PageInfo<Role> roleQuery(PageRoleQueryRequest pageQueryRequest) {
        PageHelper.startPage(pageQueryRequest.getPage(),pageQueryRequest.getPageSize());

        List<Role> roleList = roleMapper.roleQuery(pageQueryRequest.getRoleName());

        return new PageInfo<Role>(roleList);
    }

    @Override
    public void roleAssignMenu(AssignMenu assignMenu) {
        // 操作的表为 menu_role 进行数据的添加

        // 1.获取当前角色id
        Long roleId = assignMenu.getRoleId();

        // 根据当前 角色id进行删除
        roleMenuMapper.deleteByRoleId(roleId);

        // 2.获取需要分配的菜单id
        List<Map<String,Number>> menuInfo = assignMenu.getMenuIdList();

        // 3.进行循环插入数据
        if (menuInfo!=null && menuInfo.size() > 0) {
            long id = snowflakeIdGeneratorUtil.nextId(10);
            assignMenu.setId(id);
            roleMenuMapper.roleAssignMenu(assignMenu);
        }
    }

    @Override
    public Map<String, Object> findRoleInfo(long id) {
        // 0.创建map集合存储 信息
        Map<String, Object> map = new HashMap<>();

        // 1. 获取所有的角色信息
        List<Role> roleList = roleMapper.queryAll();
        map.put("allRoleList",roleList);

        // 2. 根据当前用户的 id 获取所有的信息
        List<Long> roleIdList = userRoleMapper.userRoleQueryByUserId(id);
        map.put("roleIdList",roleIdList);

        return map;
    }
}
