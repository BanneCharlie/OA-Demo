package com.banne.template.common.utils;

import com.banne.template.model.entity.Menu;
import com.banne.template.model.vo.MenuListVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuHelpUtil {
    public List<MenuListVO> builderTree(List<MenuListVO> list) {
        List<MenuListVO> menuListVOS = new ArrayList<>();

        for(MenuListVO menu : list){
            // 2.1 判断当前集合是否为父级菜单
            if(menu.getParentId() == 0){
                // 为子集添加数据
                menuListVOS .add(findChildren(menu,list));
            }
        }
        return menuListVOS;
    }

    // 查找是否存在父级菜单的子菜单
    private MenuListVO findChildren(MenuListVO menuPatent, List<MenuListVO> list) {
        // 1.初始化Menu的 children属性
        menuPatent.setChildren(new ArrayList<MenuListVO>());

        for(MenuListVO menu : list){
            // 2.判断Menu是否存在子菜单   父菜单的id == 子菜单的parentId 说明当前菜单menu就是子菜单
            if(menuPatent.getId().longValue() == menu.getParentId().longValue()){
                // 进而下一步接着寻找menu是否存在子菜单
                menuPatent.getChildren().add(findChildren(menu,list));
            }
        }
        return menuPatent;
    }
}
