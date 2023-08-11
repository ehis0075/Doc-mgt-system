package com.doc.mgt.system.docmgt.role.controller;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.role.dto.CreateUpdateRoleDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleListDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleRequestDTO;
import com.doc.mgt.system.docmgt.role.service.AdminRoleService;
import com.doc.mgt.system.docmgt.util.AuditorAwareImpl;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/adminRoles")
public class AdminRoleController {

    private final HttpServletRequest request;
    private final GeneralService generalService;
    private final AdminRoleService adminRoleService;

    public AdminRoleController(HttpServletRequest request, GeneralService generalService, AdminRoleService adminRoleService) {
        this.request = request;
        this.generalService = generalService;
        this.adminRoleService = adminRoleService;
    }

//    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping("/create")
    public Response createRole(@RequestBody CreateUpdateRoleDTO requestDTO) {

//        String user = principal.getName();
        String user = "ehis";

        RoleDTO data = adminRoleService.addRole(requestDTO, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

//    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping("/update/{roleId}")
    public Response updateRole(@RequestBody CreateUpdateRoleDTO requestDTO, @PathVariable Long roleId) {

        String user = "ehis";
//        String user = principal.getName();

        RoleDTO data = adminRoleService.updateRole(requestDTO, roleId, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

//    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @PostMapping("/delete/{id}")
    public Response deleteRole(@PathVariable Long id) {

        String user = "ehis";

        adminRoleService.deleteRole(id, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, null);
    }

//    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @PostMapping("/delete/{RoleName}")
    public Response deleteRoleByName(@PathVariable String RoleName) {

         String user = "ehis";

        adminRoleService.deleteRoleByName(RoleName, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, null);
    }

//    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    @PostMapping()
    public Response getAllRoles(@RequestBody RoleRequestDTO requestDTO) {

        String user = "ehis";

        RoleListDTO data = adminRoleService.getAllRoles(requestDTO, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
