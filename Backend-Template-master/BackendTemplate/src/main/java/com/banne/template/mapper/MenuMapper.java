package com.banne.template.mapper;

import com.banne.template.model.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {


/*    Long menuRemove(long id);

    Long menuUpdate(Menu menu);

    Menu menuQueryById(long id);

    List<Menu> menuQuery(String menuName);

    List<Menu> queryAll();*/

    // -----------------------------------------
    List<Menu> findNodes();

    void save(Menu menu);

    void updateById(Menu menu);

    void deleteById(Long id);

    int countByParentId(Long id);

    List<Menu> findUserMenuByUserId(Long userId);

    Menu selectByParentId(Long parentId);
}
