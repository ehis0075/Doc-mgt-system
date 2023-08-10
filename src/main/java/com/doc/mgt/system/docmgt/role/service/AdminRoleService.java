package com.doc.mgt.system.docmgt.role.service;


import com.doc.mgt.system.docmgt.permission.model.Permission;
import com.doc.mgt.system.docmgt.role.dto.CreateUpdateRoleDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleListDTO;
import com.doc.mgt.system.docmgt.role.dto.RoleRequestDTO;
import com.doc.mgt.system.docmgt.role.model.Role;

import java.util.List;

public interface AdminRoleService {

    RoleListDTO getAllRoles(RoleRequestDTO requestDTO, String email);

    Role getRoleByName(String name);

    RoleDTO addRole(CreateUpdateRoleDTO roleDTO, String performedBy);

    Role addRole(Role role, String name, List<Permission> permissions);

    RoleDTO updateRole(CreateUpdateRoleDTO roleDTO, Long id, String performedBy);

    RoleDTO getOneRole(Long roleId);

    void deleteRole(Long roleId, String performedBy);

    void deleteRoleByName(String roleName, String performedBy);

    RoleDTO getAdminRoleDTO(Role adminRole);
}
