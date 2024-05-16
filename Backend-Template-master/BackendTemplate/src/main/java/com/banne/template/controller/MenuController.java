package com.banne.template.controller;

import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.result.Result;
import com.banne.template.model.dto.PageMenuQueryRequest;
import com.banne.template.model.entity.Menu;
import com.banne.template.model.vo.MenuListVO;
import com.banne.template.model.vo.MenuVO;
import com.banne.template.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
@Slf4j
@Api(tags = "菜单管理模块")
public class MenuController {
    @Resource
    private MenuService menuService;

/*
    @PostMapping("/add")
    @ApiOperation("菜单的添加")
    public Result<Long> MenuAdd(@RequestBody Menu menu, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(menu.getMenuName())){
            throw new BusinessException(ResultCodeEnum.ADD_NULL);
        }
        Long result = menuService.menuAdd(menu);

        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/remove")
    @ApiOperation(value = "菜单的删除")
    public Result<Long> MenuRemove(@RequestParam("id")long id, HttpServletRequest request) {
        Long result = menuService.menuRemove(id);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/modify")
    @ApiOperation(value = "菜单的修改")
    public Result<Long> MenuUpdate(@RequestBody Menu menu, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(menu.getMenuName())){
            throw new BusinessException(ResultCodeEnum.MODIFY_NULL);
        }
        Long result = menuService.menuUpdate(menu);

        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query/{id}")
    @ApiOperation(value = "根据id查询菜单当前信息")
    public Result<Menu> MenuQueryById(@PathVariable("id")long id, HttpServletRequest request) {
        Menu Menu = menuService.menuQueryById(id);
        return Result.build(Menu,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query")
    public Result<PageInfo<Menu>> MenuQuery(@RequestBody PageMenuQueryRequest pageQueryRequest, HttpServletRequest request) {
        PageInfo<Menu> result = menuService.menuQuery(pageQueryRequest);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }
*/

    /**
     * 菜单列表   查询所有的菜单信息  存在树型结构 需要递归查询
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "查询菜单信息")
    public Result<List<MenuListVO>> findNodes() {
        List<MenuListVO> list = menuService.findNodes();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 添加菜单
     * @param  menu
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("菜单的添加")
    public Result save(@RequestBody Menu menu) {
        menuService.menuAdd(menu);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 修改菜单
     * @param
     * @return
     */
    @PutMapping("/modify")
    @ApiOperation("菜单的修改")
    public Result updateById(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation("菜单的删除")
    public Result removeById(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 根据当前登录用户信息查询 菜单信息 将菜单的信息封装为树形结构 和 查询所有菜单信息相类似 -->  展示动态路由
     * 三表链接
     * @param
     * @return
     */
    @GetMapping("/menus")
    @ApiOperation("实现菜单的动态路由")
    public Result menus() {
        List<MenuVO> sysMenuVoList =  menuService.findUserMenuList() ;
        return Result.build(sysMenuVoList , ResultCodeEnum.SUCCESS) ;
    }

    /**
     * 查询所有的菜单并且根据角色Id找到当前角色选中的菜单  --> 给角色分配 信息时使用到
     * @param
     * @return
     */
    @GetMapping("/findMenuInfo/{id}")
    @ApiOperation(value = "查询当前角色对应的菜单的信息")
    public Result<Map<String , Object>> findSysRoleMenuByRoleId(@PathVariable(value = "id") Long roleId) {
        Map<String , Object> sysRoleMenuList = menuService.findSysRoleMenuByRoleId(roleId) ;
        return Result.build(sysRoleMenuList , ResultCodeEnum.SUCCESS) ;
    }
}
