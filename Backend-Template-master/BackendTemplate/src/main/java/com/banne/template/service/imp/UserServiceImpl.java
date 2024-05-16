package com.banne.template.service.imp;


import cn.hutool.db.Page;
import com.alibaba.excel.util.StringUtils;
import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.properties.JwtProperties;
import com.banne.template.common.utils.JwtUtil;
import com.banne.template.common.utils.SnowflakeIdGeneratorUtil;
import com.banne.template.mapper.DepartMapper;
import com.banne.template.mapper.UserMapper;
import com.banne.template.mapper.UserRoleMapper;
import com.banne.template.model.dto.AssignRole;
import com.banne.template.model.dto.PageQueryRequest;
import com.banne.template.model.entity.Department;
import com.banne.template.model.entity.User;
import com.banne.template.model.vo.LoginUserVO;
import com.banne.template.model.vo.UserVO;
import com.banne.template.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 86188
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-03-08 16:29:57
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private SnowflakeIdGeneratorUtil snowflakeIdGeneratorUtil;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private UserMapper  userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private DepartMapper departMapper;

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "123456";


    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return LoginUserVO
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验  前端可以添加对于数据输入的规则
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCodeEnum.LOGIN_ERROR);
        }
        if (userPassword.length() < 4) {
            throw new BusinessException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 2. 加密 todo
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword).getBytes());
        log.info("加密后的密码：" + encryptPassword);
        // 查询用户是否存在
        User user = userMapper.selectByUserName(userAccount);

        // 用户不存在 null / ""
        if (ObjectUtils.isEmpty(user)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ResultCodeEnum.LOGIN_ERROR);
        }

        if (!user.getPassword().equals(encryptPassword)) {
            throw new BusinessException(ResultCodeEnum.LOGIN_ERROR);
        }

        // 3. 记录用户的登录态  todo
        // request.getSession().setAttribute(USER_LOGIN_STATE, user);

        // 登录成功后,生成JWT令牌,并存放到Redis数据库中(当令牌泄漏时,可以通过撤销Redis中的令牌避免他人违规操作)

        // 将当前登录的用户id存放入令牌中
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String jwtToken = JwtUtil.createJWT(jwtProperties.getKey(), jwtProperties.getTtlMillis(), claims);

        // 存放入Redis中
        redisTemplate.opsForValue().set("jwtToken:"+user.getId(),jwtToken);

        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setToken(jwtToken);
        loginUserVO.setRefresh_token("");
        // 4. 返回结果
        return loginUserVO;
    }

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCodeEnum.USER_ACCOUNT_FORMAT);
        }
        if (userPassword.length() < 4) {
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_FORMAT);
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT).getBytes());
        // 3.用户的账号密码存入到数据库中

        // 用户账号不可重复
        User user = userMapper.selectByUserName(userAccount);
        if (!ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        // 插入数据库中  使用链式编程
        User realUser = User.builder().userName(userAccount)
                .password(encryptPassword)
                .build();
        boolean saveResult = userMapper.userAdd(realUser);
        // 存储数据库失败
        if (!saveResult) {
            throw new BusinessException(ResultCodeEnum.SAVE_DB_ERROR);
        }
        // 4. 返回存储用户的id
        return realUser.getId();
    }

    @Override
    public Long userAdd(User user) {
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ResultCodeEnum.ADD_FAIL);
        }
        if (user.getUserAccount()==null) {
            throw new BusinessException(ResultCodeEnum.USER_ACCOUNT_FORMAT);
        }
        if (user.getUserName() == null){
            throw new BusinessException(ResultCodeEnum.ADD_NULL);
        }
        if (user.getDepartmentId() == null){
            throw new BusinessException(ResultCodeEnum.ADD_NULL);
        }

        long l = snowflakeIdGeneratorUtil.nextId(10);
        user.setId(l);
        // 进行密码的加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT).getBytes());
        user.setPassword(encryptPassword);

        return userMapper.userAddAll(user);
    }

    @Override
    public Long userRemove(long id) {
        return userMapper.userRemove(id);
    }

    @Override
    public Long userUpdate(User user) {
/*        if (user.getPhone().length() != 11) {
            throw new BusinessException(ResultCodeEnum.USER_CHECK_PHONE_FORMAT);
        }*/
        return userMapper.userUpdate(user);
    }

    @Override
    public User userQueryById(long id, HttpServletRequest request) {
        return userMapper.userQueryById(id);
    }

/*    @Override
    public PageInfo<UserVO> userQuery(PageQueryRequest pageQueryRequest) {
        PageHelper.startPage(pageQueryRequest.getPage(),pageQueryRequest.getPageSize());

        List<User> users = userMapper.userPageQuery(pageQueryRequest);

        List<UserVO> userVOList = new ArrayList<>();

        for (User user : users) {
            Long departmentId = user.getDepartmentId();
            // 根据部门id 获取部门信息
            Department department = departMapper.departQueryById(departmentId);

            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user,userVO);
            userVO.setDepartmentName(department.getDepartmentName());
            userVOList.add(userVO);
        }

        if (pageQueryRequest.getDepartmentName() != null && pageQueryRequest.getDepartmentName() != "") {
             // 从 UserVO 中 查询出 对应的部门信息
            userVOList = userVOList.stream()
                    .filter(userVO -> userVO.getDepartmentName().contains(pageQueryRequest.getDepartmentName()))
                    .collect(Collectors.toList());
        }


        log.info("userVolist{}",userVOList);

        return new PageInfo<UserVO>(userVOList);
    }*/



    public PageInfo<UserVO> userQuery(PageQueryRequest pageQueryRequest) {
        PageHelper.startPage(pageQueryRequest.getPage(),pageQueryRequest.getPageSize());

        List<UserVO> userVOList = userMapper.userPageQueryUserVO(pageQueryRequest);

        log.info("userVolist{}",userVOList);

        return new PageInfo<UserVO>(userVOList);
    }

    @Override
    public void userAssignRole(AssignRole assignRole) {
        // 1. 获取当前登录的用户id
        Long userId = assignRole.getUserId();

        // 删除当前 用户的所有id
        userRoleMapper.deleteByUserId(userId);

        // 2. 获取到需要分配的角色id
        List<Long> roleIdList = assignRole.getRoleIdList();

        // 3.循环插入 user_role
        for (Long roleId : roleIdList) {
            long id = snowflakeIdGeneratorUtil.nextId(10);
            userRoleMapper.userAssignMenu(id,userId,roleId);
        }
    }

    @Override
    public List<UserVO> userQueryByDepartmentId(long departmentId, long parentId, HttpServletRequest request) {
        if (parentId == 0) {
            // 查询当前的部门下的所有用户   1000    0  查询所有父类id为 1000 的 部门信息
            List<Long> departmentIdList = departMapper.departQueryByParentId(departmentId);

            // 根据这个集合进行查询
            List<UserVO> userVOList = new ArrayList<>();
            for (Long id : departmentIdList) {
                List<User> users = userMapper.userQueryByDepartmentId(id);

                for (User user : users) {
                    Long departId = user.getDepartmentId();
                    // 根据部门id 获取部门信息
                    Department department = departMapper.departQueryById(departId);
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(user,userVO);
                    userVO.setDepartmentName(department.getDepartmentName());
                    userVOList.add(userVO);
                }
            }
            return userVOList;
        }
        // 根据部门id查询部门下的所有用户
        List<User> users = userMapper.userQueryByDepartmentId(departmentId);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : users) {
            // 根据部门id 获取部门信息
            Department department = departMapper.departQueryById(departmentId);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user,userVO);
            userVO.setDepartmentName(department.getDepartmentName());
            userVOList.add(userVO);
        }

        return userVOList;
    }

    @Override
    public Long userModifyStatus(Long userId, Integer status) {
        return userMapper.userModifyStatus(userId,status);
    }


    /**
     * 查询未被禁用的用户信息
     * @return
     */
    @Override
    public List<User> userQueryAll() {
        List<User> usersList = userMapper.userQueryAllS();
        return usersList;
    }
}




