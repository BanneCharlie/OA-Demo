package com.banne.template.mapper;

import com.banne.template.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    Long roleAdd(Role role);

    Long roleRemove(long id);

    Long roleUpdate(Role role);

    Role roleQueryById(long id);

    List<Role> roleQuery(String roleName);

    List<Role> queryAll();
}
