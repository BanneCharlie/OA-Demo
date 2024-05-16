package com.banne.template.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    void userAssignMenu(Long id,Long userId, Long roleId);

    List<Long> userRoleQueryByUserId(long id);

    void deleteByUserId(Long userId);
}
