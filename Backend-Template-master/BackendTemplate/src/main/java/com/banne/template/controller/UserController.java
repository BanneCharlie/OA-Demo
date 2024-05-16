package com.banne.template.controller;

import com.banne.template.annotation.Logging;
import com.banne.template.common.context.BaseContext;
import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.result.Result;
import com.banne.template.mapper.UserRoleMapper;
import com.banne.template.model.dto.*;
import com.banne.template.model.entity.User;
import com.banne.template.model.vo.LoginUserVO;
import com.banne.template.model.vo.UserVO;
import com.banne.template.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户管理模块")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 没有创建 userRoleController 和  userRoleService 直接创建 userRoleMapper 操作数据库
     */
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(userLoginRequest)) {
            throw new BusinessException(ResultCodeEnum.LOGIN_CONTENT_NULL);
        }
        String userAccount = userLoginRequest.getUserName();
        String userPassword = userLoginRequest.getPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCodeEnum.LOGIN_CONTENT_NULL);
        }

        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);

        return Result.build(loginUserVO,ResultCodeEnum.SUCCESS);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "用户登出")
    public Result userLogout(@RequestHeader(value = "token") String token){
        // 获取当前登录的用户id
        Long userId = BaseContext.getCurrentId();
        stringRedisTemplate.delete("jwtToken:" + userId);
        return Result.build(null, ResultCodeEnum.LOGOUT);
    }

    /**
     * 用户的注册
     *
     * @param userRegisterRequest
     * @param request
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户的注册")
    public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
         if(ObjectUtils.isEmpty(userRegisterRequest)){
             throw new BusinessException(ResultCodeEnum.USER_REGISTER_NULL);
         }
        String userAccount = userRegisterRequest.getUserName();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }


    @Logging
    @PostMapping("/add")
    @ApiOperation(value = "用户的添加")
    public Result<Long> userAdd(@RequestBody User user, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(user)){
            throw new BusinessException(ResultCodeEnum.ADD_NULL);
        }
        Long result = userService.userAdd(user);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @Logging
    @PostMapping("/remove/{id}")
    @ApiOperation(value = "用户的删除")
    public Result<Long> userRemove(@PathVariable("id")long id, HttpServletRequest request) {
        Long result = userService.userRemove(id);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @Logging
    @PostMapping("/modify")
    @ApiOperation(value = "用户的修改")
    public Result<Long> userUpdate(@RequestBody User user, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(user)){
            throw new BusinessException(ResultCodeEnum.MODIFY_NULL);
        }
        Long result = userService.userUpdate(user);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query")
    @ApiOperation(value = "分页查询用户信息")
    public Result<PageInfo<UserVO>> userQuery(@RequestBody PageQueryRequest pageQueryRequest, HttpServletRequest request) {
        PageInfo<UserVO> result = userService.userQuery(pageQueryRequest);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query/{id}")
    @ApiOperation(value = "根据用户id查询用户当前信息")
    public Result<User> userQueryById(@PathVariable("id")long id, HttpServletRequest request) {
        User user = userService.userQueryById(id, request);
        return Result.build(user,ResultCodeEnum.SUCCESS);
    }

    /**
     * 根据当前用户Id 分配角色  操作 user_role 表 先进行删除 再进行添加 更加高效 (先删除缓存,而不是更新缓存)
     * @param assignRole
     * @return
     */
    @PostMapping("/assignRole")
    @ApiOperation(value = "根据当前用户分配角色")
    public Result<Long> userAssignRole(@RequestBody AssignRole assignRole){
        userService.userAssignRole(assignRole);
        return Result.build(null,ResultCodeEnum.SUCCESS);
    }

    @GetMapping(value = "/getUserInfo")
    @ApiOperation(value = "获取登录用户信息")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        User user = userService.userQueryById(userId, request);
        if (ObjectUtils.isEmpty(user)){
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }
        return Result.build(user,ResultCodeEnum.SUCCESS);
    }

    /**
     * 根据部门id查询部门对应的员工信息  需要判断父子部门 查询到合适的信息
     * @param departmentId
     * @param parentId
     * @param request
     * @return
     */
    @PostMapping("/{departmentId}/{parentId}")
    @ApiOperation(value = "查询每个部门对应的员工信息")
    public Result<List<UserVO>> userQueryByDepartmentId(@PathVariable("departmentId")long departmentId,@PathVariable("parentId")long parentId, HttpServletRequest request) {
          List<UserVO> userVOList =  userService.userQueryByDepartmentId(departmentId,parentId,request);
          return Result.build(userVOList,ResultCodeEnum.SUCCESS);
    }


    @PostMapping("/queryRoleId")
    @ApiOperation(value = "查询当前用户具有的角色id")
    public Result<List<Long>> userQueryRoleByUserId(){
        Long userId = BaseContext.getCurrentId();
        List<Long> roleIdList = userRoleMapper.userRoleQueryByUserId(userId);
        return  Result.build(roleIdList,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/modifyStatus/{userId}/{status}")
    @ApiOperation(value = "修改用户的状态")
    public Result<Long> userModifyStatus(@PathVariable("userId") Long userId, @PathVariable("status") Integer status){
        Long result = userService.userModifyStatus(userId,status);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }


    @PostMapping("/queryAll")
    @ApiOperation(value = "查询所有未被禁用的用户信息")
    public Result<List<User>> userQueryAll(){
        List<User> usersList = userService.userQueryAll();
        return Result.build(usersList,ResultCodeEnum.SUCCESS);
    }
}
