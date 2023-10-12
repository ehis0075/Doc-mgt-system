package com.doc.mgt.system.docmgt.department.service;

import com.doc.mgt.system.docmgt.department.dto.CreateUpdateDepartmentRequestDTO;
import com.doc.mgt.system.docmgt.department.dto.DepartmentDTO;
import com.doc.mgt.system.docmgt.department.dto.DepartmentListDTO;
import com.doc.mgt.system.docmgt.department.model.Department;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;

public interface DepartmentService {

    DepartmentDTO createDepartment(CreateUpdateDepartmentRequestDTO requestDTO);

    DepartmentDTO getDepartmentDTO(Department department);

    Department getDepartmentById(Long id);

    DepartmentListDTO getAllDept(PageableRequestDTO requestDTO);
}
