package com.banne.template.controller;

import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.result.Result;
import com.banne.template.model.dto.AssignMenu;
import com.banne.template.model.dto.PageRoleQueryRequest;
import com.banne.template.model.entity.Role;
import com.banne.template.service.RoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Slf4j
@Api(tags = "角色管理模块")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/add")
    @ApiOperation("角色的添加")
    public Result<Long> roleAdd(@RequestBody Role role, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(role.getRoleName())){
            throw new BusinessException(ResultCodeEnum.ADD_NULL);
        }

        Long result = roleService.roleAdd(role);
        System.out.println("结果为 "  + role);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "角色的删除")
    public Result<Long> roleRemove(@PathVariable("id") long id, HttpServletRequest request) {
        Long result = roleService.roleRemove(id);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/modify")
    @ApiOperation(value = "角色的修改")
    public Result<Long> roleUpdate(@RequestBody Role role, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(role.getRoleName())){
            throw new BusinessException(ResultCodeEnum.MODIFY_NULL);
        }
        Long result = roleService.roleUpdate(role);

        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query/{id}")
    @ApiOperation(value = "根据id查询角色当前信息")
    public Result<Role> roleQueryById(@PathVariable("id")long id, HttpServletRequest request) {
        Role role = roleService.roleQueryById(id);
        return Result.build(role,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询角色信息")
    public Result<PageInfo<Role>> roleQuery(@RequestBody PageRoleQueryRequest pageQueryRequest, HttpServletRequest request) {

        PageInfo<Role> result = roleService.roleQuery(pageQueryRequest);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    //todo  查询当前所有的角色信息 并且 根据用户id 找到用户已经选择的角色 Map集合存储
    @PostMapping("/findRoleInfo/{id}")
    @ApiOperation(value = "查询当前用户对应的角色的信息")
    public Result<Map<String,Object>> findRoleInfo(@PathVariable("id") long id){
        Map<String,Object> result = roleService.findRoleInfo(id);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }


    //todo 为选中的角色分配  菜单
    @PostMapping("/assignMenu")
    @ApiOperation(value = "当前角色分配菜单")
    public Result<Long> roleAssignMenu(@RequestBody AssignMenu assignMenu){
        roleService.roleAssignMenu(assignMenu);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }


}
