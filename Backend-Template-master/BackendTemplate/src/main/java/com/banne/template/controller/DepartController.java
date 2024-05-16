package com.banne.template.controller;

import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.result.Result;
import com.banne.template.model.dto.PageDepartmentQueryRequest;
import com.banne.template.model.entity.Depart;
import com.banne.template.model.entity.Department;
import com.banne.template.model.vo.DepartVO;
import com.banne.template.service.DepartService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/depart")
@Slf4j
@Api(tags = "部门管理模块")
public class DepartController {

    @Resource
    private DepartService departService;

    @PostMapping("/add")
    @ApiOperation("部门的添加")
    public Result<Long> DepartAdd(@RequestBody Department depart, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(depart.getDepartmentName())){
            throw new BusinessException(ResultCodeEnum.ADD_NULL);
        }

        Long result = departService.departAdd(depart);

        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "部门的删除")
    public Result<Long> departRemove(@PathVariable("id")long id, HttpServletRequest request) {
        Long result = departService.departRemove(id);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/modify")
    @ApiOperation(value = "部门的修改")
    public Result<Long> DepartUpdate(@RequestBody Department depart, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(depart.getDepartmentName())){
            throw new BusinessException(ResultCodeEnum.MODIFY_NULL);
        }
        Long result = departService.departUpdate(depart);

        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query/{id}")
    @ApiOperation(value = "根据id查询部门当前信息")
    public Result<Department> DepartQueryById(@PathVariable("id")long id, HttpServletRequest request) {
        Department Depart = departService.departQueryById(id);
        return Result.build(Depart,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询部门信息")
    public Result<PageInfo<DepartVO>> DepartQuery(@RequestBody PageDepartmentQueryRequest pageQueryRequest, HttpServletRequest request) {
        PageInfo<DepartVO> result = departService.departQuery(pageQueryRequest);
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    @PostMapping("/all")
    @ApiOperation("获取所有的部门信息")
    public Result<List<Department>> DepartAll(HttpServletRequest request) {
        List<Department> result = departService.departQueryAll();
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }

    // 查询部门树 实现返回  类似于 菜单管理的树结构
    @PostMapping("/tree")
    @ApiOperation("获取部门树")
    public Result<List<DepartVO>> DepartTree(HttpServletRequest request) {
        List<DepartVO> result = departService.departQueryTree();
        return Result.build(result,ResultCodeEnum.SUCCESS);
    }
}
