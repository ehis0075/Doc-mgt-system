package com.doc.mgt.system.docmgt.role.controller;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.role.dto.CreateUpdateRoleDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleListDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleRequestDTO;
import com.doc.mgt.system.docmgt.role.service.AdminRoleService;
import com.doc.mgt.system.docmgt.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/adminRoles")
public class AdminRoleController {

    private final UserService userService;
    private final GeneralService generalService;
    private final AdminRoleService adminRoleService;

    public AdminRoleController(UserService userService, GeneralService generalService, AdminRoleService adminRoleService) {
        this.userService = userService;
        this.generalService = generalService;
        this.adminRoleService = adminRoleService;
    }

    //    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping("/create")
    public Response createRole(@RequestBody CreateUpdateRoleDTO requestDTO) {

        String user = userService.getLoggedInUser();

        RoleDTO data = adminRoleService.addRole(requestDTO, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    //    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping("/update/{roleId}")
    public Response updateRole(@RequestBody CreateUpdateRoleDTO requestDTO, @PathVariable Long roleId) {

        String user = userService.getLoggedInUser();

        RoleDTO data = adminRoleService.updateRole(requestDTO, roleId, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    //    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @PostMapping("/delete/{id}")
    public Response deleteRole(@PathVariable Long id, Principal principal) {

        String user = principal.getName();

        adminRoleService.deleteRole(id, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, null);
    }

    //    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @PostMapping("/delete/{RoleName}")
    public Response deleteRoleByName(@PathVariable String RoleName) {

        String user = userService.getLoggedInUser();

        adminRoleService.deleteRoleByName(RoleName, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, null);
    }

    //    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    @PostMapping()
    public Response getAllRoles(@RequestBody RoleRequestDTO requestDTO) {

        String user = userService.getLoggedInUser();

        RoleListDTO data = adminRoleService.getAllRoles(requestDTO, user);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
