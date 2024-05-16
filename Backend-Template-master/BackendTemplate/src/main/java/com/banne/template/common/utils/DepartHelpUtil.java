package com.banne.template.common.utils;

import com.banne.template.model.entity.Depart;
import com.banne.template.model.vo.DepartVO;
import com.banne.template.model.vo.MenuListVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartHelpUtil {
    public List<DepartVO> builderTree(List<DepartVO> list) {
        List<DepartVO> departList = new ArrayList<>();

        for(DepartVO depart : list){
            // 2.1 判断当前集合是否为父级菜单
            if(depart.getParentId() == 0){
                // 为子集添加数据
                departList.add(findChildren(depart,list));
            }
        }
        return departList;
    }

    // 查找是否存在父级菜单的子菜单
    private DepartVO findChildren(DepartVO depart, List<DepartVO> list) {
        // 1.初始化Menu的 children属性
        depart.setChildren(new ArrayList<DepartVO>());

        for(DepartVO departVO : list){
            // 2.判断Menu是否存在子菜单
            if(depart.getId().longValue() == departVO.getParentId().longValue()){
                // 进而下一步接着寻找menu是否存在子菜单
                depart.getChildren().add(findChildren(departVO,list));
            }
        }
        return depart;
    }
}
