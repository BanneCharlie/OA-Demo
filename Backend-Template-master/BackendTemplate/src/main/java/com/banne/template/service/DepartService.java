package com.banne.template.service;

import com.banne.template.model.dto.PageDepartmentQueryRequest;
import com.banne.template.model.entity.Depart;
import com.banne.template.model.entity.Department;
import com.banne.template.model.vo.DepartVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartService {
    Long departAdd(Department depart);

    Long departRemove(long id);

    Long departUpdate(Department depart);
    
    Department departQueryById(long id);

    PageInfo<DepartVO> departQuery(PageDepartmentQueryRequest pageQueryRequest);

    // 获取所有的部门信息
    List<Department> departQueryAll();

    List<DepartVO> departQueryTree();
}
