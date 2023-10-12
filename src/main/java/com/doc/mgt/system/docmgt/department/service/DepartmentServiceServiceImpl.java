package com.doc.mgt.system.docmgt.department.service;

import com.doc.mgt.system.docmgt.department.dto.CreateUpdateDepartmentRequestDTO;
import com.doc.mgt.system.docmgt.department.dto.DepartmentDTO;
import com.doc.mgt.system.docmgt.department.dto.DepartmentListDTO;
import com.doc.mgt.system.docmgt.department.model.Department;
import com.doc.mgt.system.docmgt.department.repository.DepartmentRepository;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class DepartmentServiceServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    private GeneralService generalService;

    @Override
    public DepartmentDTO createDepartment(CreateUpdateDepartmentRequestDTO requestDTO) {
        log.info("Request to create department with payload {}", requestDTO);

        Department department = new Department();
        department.setName(requestDTO.getName());

        department = departmentRepository.save(department);

        return getDepartmentDTO(department);
    }

    @Override
    public DepartmentDTO getDepartmentDTO(Department department) {

        DepartmentDTO departmentDTO = new DepartmentDTO();
        generalService.createDTOFromModel(department, departmentDTO);

        return departmentDTO;
    }

    @Override
    public Department getDepartmentById(Long id) {

        return departmentRepository.getById(id);

    }

    @Override
    public DepartmentListDTO getAllDept(PageableRequestDTO requestDTO) {
        log.info("Getting Dept List with {}", requestDTO);

        Pageable paged = generalService.getPageableObject(requestDTO.getSize(), requestDTO.getPage());
        Page<Department> departmentPage = departmentRepository.findAll(paged);

        return getDeptListDTO(departmentPage);
    }

    private DepartmentListDTO getDeptListDTO(Page<Department> departmentPage) {
        DepartmentListDTO listDTO = new DepartmentListDTO();

        List<Department> departmentList = departmentPage.getContent();
        if (!departmentPage.getContent().isEmpty()) {
            listDTO.setHasNextRecord(departmentPage.hasNext());
            listDTO.setTotalCount((int) departmentPage.getTotalElements());
        }

        List<DepartmentDTO> departmentDTOS = convertToDeptDTOList(departmentList);
        listDTO.setDepartmentDTOList(departmentDTOS);

        return listDTO;
    }

    private List<DepartmentDTO> convertToDeptDTOList(List<Department> departmentList) {
        log.info("Converting Dept List to Dept DTO List");

        return departmentList.stream().map(Department::getDeptDTO).collect(Collectors.toList());
    }

}
