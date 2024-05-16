package com.banne.template.mapper;


import com.banne.template.model.dto.PageQueryRequest;
import com.banne.template.model.entity.User;
import com.banne.template.model.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 86188
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-03-08 16:29:57
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByUserName(String userAccount);

    Long userAddAll(User user);

    Long userRemove(long id);

    Long userUpdate(User user);

    User userQueryById(long id);


    List<User> userQueryByDepartmentId(long departmentId);

    List<User> userQueryAll();

    List<User> userPageQuery(PageQueryRequest pageQueryRequest);

    boolean userAdd(User realUser);

    Long userModifyStatus(Long userId, Integer status);

    List<User> userQueryAllS();

    List<UserVO> userPageQueryUserVO(PageQueryRequest pageQueryRequest);
}




