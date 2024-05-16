package com.banne.template.mapper;

import com.banne.template.model.dto.PageDepartmentQueryRequest;
import com.banne.template.model.entity.Depart;
import com.banne.template.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartMapper {
    Long departAdd(Department depart);

    Long departRemove(long id);

    Long departUpdate(Department depart);

    Department departQueryById(long id);

    List<Department> departQuery(PageDepartmentQueryRequest pageQueryRequest);

    List<Department> departQueryAll();

    List<Depart> departQueryTree();

    List<Long> departQueryByParentId(long departmentId);
}
