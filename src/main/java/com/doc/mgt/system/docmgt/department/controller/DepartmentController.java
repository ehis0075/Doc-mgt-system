package com.doc.mgt.system.docmgt.department.controller;

import com.doc.mgt.system.docmgt.department.dto.CreateUpdateDepartmentRequestDTO;
import com.doc.mgt.system.docmgt.department.dto.DepartmentDTO;
import com.doc.mgt.system.docmgt.department.dto.DepartmentListDTO;
import com.doc.mgt.system.docmgt.department.service.DepartmentService;
import com.doc.mgt.system.docmgt.general.dto.PageableRequestDTO;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/dept")
@CrossOrigin(origins = {"http://*", "https://*", "https://document-management.vercel.app/", "https://document-management.vercel.app", "http://127.0.0.1:5500/", "http://127.0.0.1:5500", "http://localhost:5500/"}, maxAge = 3600)
public class DepartmentController {

    private DepartmentService departmentService;
    private GeneralService generalService;

    @PostMapping("/create")
    public Response createDepartment(@RequestBody CreateUpdateDepartmentRequestDTO requestDTO) {

        DepartmentDTO data = departmentService.createDepartment(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    // get all dept api
    @PostMapping()
    public Response getAllDepartment(@RequestBody PageableRequestDTO request) {

        DepartmentListDTO data = departmentService.getAllDept(request);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);

    }
}
