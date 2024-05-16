package com.banne.template.service.imp;

import cn.hutool.core.bean.BeanUtil;
import com.banne.template.common.context.BaseContext;
import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.utils.MenuHelpUtil;
import com.banne.template.common.utils.SnowflakeIdGeneratorUtil;
import com.banne.template.mapper.MenuMapper;
import com.banne.template.mapper.RoleMenuMapper;
import com.banne.template.model.dto.PageMenuQueryRequest;
import com.banne.template.model.entity.Menu;
import com.banne.template.model.vo.MenuListVO;
import com.banne.template.model.vo.MenuVO;
import com.banne.template.service.MenuService;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class MenuServiceImp implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private SnowflakeIdGeneratorUtil snowflakeIdGenerator;

    // 方便用于实现 查询
    @Resource
    private MenuHelpUtil menuHelpUtil;

    // 查询所有的菜单信息
    @Override
    public List<MenuListVO> findNodes() {
        // 1.查询所有菜单信息
        List<Menu> list = menuMapper.findNodes();
        // 判断集合是否为空
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<MenuListVO> menuListVOS = new ArrayList<>();
        // 进行一次封装
        for (Menu menu : list) {
            MenuListVO menuListVO = new MenuListVO();
            BeanUtil.copyProperties(menu,menuListVO);
            menuListVOS.add(menuListVO);
        }
        // 2.将查询到的集合进行封装(封装为前端需要的内容)
        List<MenuListVO> menuList = menuHelpUtil.builderTree(menuListVOS);
        return menuList;
    }

    // 进行菜单的添加
    @Override
    public void menuAdd(Menu menu) {
        long id = snowflakeIdGenerator.nextId(10);
        menu.setId(id);
        menuMapper.save(menu);
        // 新添加一个菜单，那么此时就需要将该菜单所对应的父级菜单设置为半开
        updateSysRoleMenuIsHalf(menu);
    }

    @Override
    public void updateById(Menu menu) {
        menuMapper.updateById(menu);
    }

    @Override
    public void removeById(Long id) {
        // 1.查询是否存在子菜单,存在则不允许删除
        int count = menuMapper.countByParentId(id);
        System.out.println(count);
        if (count > 0){
            throw new BusinessException(ResultCodeEnum.NODE_ERROR);
        }
        menuMapper.deleteById(id);
    }

    private void updateSysRoleMenuIsHalf(Menu menu) {
        // 1.获取到当前菜单的父级菜单
        Menu parentMenu = menuMapper.selectByParentId(menu.getParentId());

        if (parentMenu!= null){
            // 2.将该id的菜单设置为半开
            roleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId());

            // 递归调用 可能需要几次才可以找到 顶级父级菜单
            updateSysRoleMenuIsHalf(parentMenu);
        }
    }

    // 动态路由的实现
    @Override
    public List<MenuVO> findUserMenuList() {
        // 1.获取到当前登录的用户id
        Long user_id = BaseContext.getCurrentId();
        // 2.根据用户id获取到具有的菜单功能;
        List<Menu> list= menuMapper.findUserMenuByUserId(user_id);

        List<MenuListVO> menuListVOS = new ArrayList<>();

        // 进行一次封装
        for (Menu menu : list) {
            MenuListVO menuListVO = new MenuListVO();
            BeanUtil.copyProperties(menu,menuListVO);
            menuListVOS.add(menuListVO);
        }

        log.info("菜单信息:{}",menuListVOS);

        // 3.将获取的用户具有的菜单信息,进行封装  --> 封装成具有层级效果的菜单进行展示
        List<MenuListVO> sysMenus = menuHelpUtil.builderTree(menuListVOS);

        // 4.再进行进一步的转换  进行了一次封装不需要 返回 id 值
        List<MenuVO> sysMenuVos = buildMenus(sysMenus);
        return sysMenuVos;
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<MenuVO> buildMenus(List<MenuListVO> menus) {

        List<MenuVO> sysMenuVoList = new LinkedList<MenuVO>();
        for (MenuListVO sysMenu : menus) {
            MenuVO sysMenuVo = new MenuVO();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<MenuListVO> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }

    // 根据角色id 查询当前角色具有的菜单
    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        Map<String,Object> map = new HashMap<String, Object>();

        // 1.查询所有菜单信息
        List<Menu> list = menuMapper.findNodes();
        // 判断集合是否为空
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<MenuListVO> menuListVOS = new ArrayList<>();
        // 进行一次封装
        for (Menu menu : list) {
            MenuListVO menuListVO = new MenuListVO();
            BeanUtil.copyProperties(menu,menuListVO);
            menuListVOS.add(menuListVO);
        }
        // 将查询到的集合进行封装(封装为前端需要的内容)
        List<MenuListVO> sysMenus = menuHelpUtil.builderTree(menuListVOS);

        // 2.根据角色Id查询当前角色具有的菜单
        List<Long> roleMenuIds =  roleMenuMapper.rolemenuQueryByRoleId(roleId);

        map.put("menuList",sysMenus);
        map.put("roleMenuIds" , roleMenuIds) ;

        return map;
    }

}
