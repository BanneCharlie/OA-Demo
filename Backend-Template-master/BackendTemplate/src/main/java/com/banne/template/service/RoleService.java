package com.banne.template.service;

import com.banne.template.model.dto.AssignMenu;
import com.banne.template.model.dto.PageRoleQueryRequest;
import com.banne.template.model.entity.Role;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RoleService {
    Long roleAdd(Role role);

    Long roleRemove(long id);

    Long roleUpdate(Role role);

    Role roleQueryById(long id);

    PageInfo<Role> roleQuery(PageRoleQueryRequest pageQueryRequest);

    void roleAssignMenu(AssignMenu assignMenu);

    Map<String, Object> findRoleInfo(long id);
}
