package com.banne.template.mapper;

import com.banne.template.model.dto.AssignMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMenuMapper {
    List<Long> rolemenuQueryByRoleId(long id);


    void roleAssignMenu(AssignMenu assignMenu);

    void deleteByRoleId(Long roleId);

    void updateSysRoleMenuIsHalf(Long id);
}
