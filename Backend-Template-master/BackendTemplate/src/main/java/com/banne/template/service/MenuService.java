package com.banne.template.service;

import com.banne.template.model.dto.PageMenuQueryRequest;
import com.banne.template.model.entity.Menu;
import com.banne.template.model.vo.MenuListVO;
import com.banne.template.model.vo.MenuVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MenuService {

/*
    Long menuRemove(long id);

    Long menuUpdate(Menu menu);

    Menu menuQueryById(long id);

    PageInfo<Menu> menuQuery(PageMenuQueryRequest pageQueryRequest);

    Map<String, Object> findMenuInfo(long id);*/

    List<MenuListVO> findNodes();


    void menuAdd(Menu menu);

    void updateById(Menu menu);

    void removeById(Long id);

    List<MenuVO> findUserMenuList();

    Map<String, Object> findSysRoleMenuByRoleId(Long roleId);
}
