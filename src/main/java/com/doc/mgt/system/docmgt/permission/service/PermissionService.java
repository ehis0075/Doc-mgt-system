package com.doc.mgt.system.docmgt.permission.service;




import com.doc.mgt.system.docmgt.permission.dto.PermissionDTO;
import com.doc.mgt.system.docmgt.permission.dto.PermissionListDTO;
import com.doc.mgt.system.docmgt.permission.dto.PermissionRequestDTO;
import com.doc.mgt.system.docmgt.permission.enums.PermissionType;
import com.doc.mgt.system.docmgt.permission.model.Permission;

import java.util.List;

public interface PermissionService {
    PermissionListDTO getAllAdminPermission(PermissionRequestDTO requestDTO);

    PermissionListDTO getAllPermission(PermissionRequestDTO requestDTO);

    PermissionListDTO getAllUserPermission(PermissionRequestDTO requestDTO);

    Permission createPermission(String name, PermissionType permissionType);

    Permission findByName(String name);

    PermissionDTO getPermissionDTO(Permission permission);

    List<Permission> getPermissionsByPermissionType(PermissionType permissionType);

    void createPermissionsIfNecessary();

}
