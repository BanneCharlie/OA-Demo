package com.banne.template.service.imp;

import com.banne.template.common.enumeration.ResultCodeEnum;
import com.banne.template.common.exception.BusinessException;
import com.banne.template.common.utils.DepartHelpUtil;
import com.banne.template.common.utils.SnowflakeIdGeneratorUtil;
import com.banne.template.mapper.DepartMapper;
import com.banne.template.model.dto.PageDepartmentQueryRequest;
import com.banne.template.model.entity.Depart;
import com.banne.template.model.entity.Department;
import com.banne.template.model.vo.DepartVO;
import com.banne.template.model.vo.MenuListVO;
import com.banne.template.service.DepartService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DepartServiceImp implements DepartService {

    @Resource
    private DepartMapper departMapper;

    @Resource
    private DepartHelpUtil departHelpUtil;

    @Resource
    private SnowflakeIdGeneratorUtil snowflakeIdGeneratorUtil;

    @Override
    public Long departAdd(Department depart) {
        // 传递雪花算法生成的16位唯一id
        long id = snowflakeIdGeneratorUtil.nextId(12);
        depart.setId(id);

        return departMapper.departAdd(depart);
    }

    @Override
    public Long departRemove(long id) {
        return departMapper.departRemove(id);
    }

    @Override
    public Long departUpdate(Department depart) {
        return departMapper.departUpdate(depart);
    }

    @Override
    public Department departQueryById(long id) {
        return departMapper.departQueryById(id);
    }

    @Override
    public PageInfo<DepartVO> departQuery(PageDepartmentQueryRequest pageQueryRequest) {
        PageHelper.offsetPage(pageQueryRequest.getPage(),pageQueryRequest.getPageSize());

        List<Department> departmentList = departMapper.departQuery(pageQueryRequest);

        // 将 departList 中的数据 封装成 List<DepartVO>
        List<DepartVO> departVOList = new ArrayList<>();

        for (Department depart : departmentList) {
            DepartVO departVO = new DepartVO();
            BeanUtils.copyProperties(depart,departVO);
            departVOList.add(departVO);
        }

        // 2.将查询到的集合进行封装(封装为前端需要的内容)
        List<DepartVO> departLists = departHelpUtil.builderTree(departVOList);

        return new PageInfo<DepartVO>(departLists);
    }

    @Override
    public List<Department> departQueryAll() {
        List<Department> departmentList = departMapper.departQueryAll();
        return departmentList;
    }


    // 实现部门树的查询
    @Override
    public List<DepartVO> departQueryTree() {
        List<Depart> departList =  departMapper.departQueryTree();

        // 将 departList 中的数据 封装成 List<DepartVO>
        List<DepartVO> departVOList = new ArrayList<>();

        for (Depart depart : departList) {
            DepartVO departVO = new DepartVO();
            BeanUtils.copyProperties(depart,departVO);
            departVOList.add(departVO);
        }

        // 2.将查询到的集合进行封装(封装为前端需要的内容)
        List<DepartVO> departLists = departHelpUtil.builderTree(departVOList);

        return departLists;
    }
}
