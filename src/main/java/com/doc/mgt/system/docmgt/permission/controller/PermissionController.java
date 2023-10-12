package com.doc.mgt.system.docmgt.permission.controller;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.permission.dto.PermissionListDTO;
import com.doc.mgt.system.docmgt.permission.dto.PermissionRequestDTO;
import com.doc.mgt.system.docmgt.permission.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
//@CrossOrigin(origins = {"http://*", "https://*", "https://document-management.vercel.app/", "https://document-management.vercel.app", "http://127.0.0.1:5500/", "http://127.0.0.1:5500", "http://localhost:5500/"}, maxAge = 3600)
public class PermissionController {

    private final GeneralService generalService;
    private final PermissionService permissionService;

    public PermissionController(GeneralService generalService, PermissionService permissionService) {
        this.generalService = generalService;
        this.permissionService = permissionService;
    }

//    @PreAuthorize("hasAuthority('VIEW_PERMISSION')")
    @PostMapping()
    public Response getAllPermission(@RequestBody PermissionRequestDTO requestDTO) {
        PermissionListDTO data = permissionService.getAllPermission(requestDTO);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
