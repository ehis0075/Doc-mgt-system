package com.doc.mgt.system.docmgt.role.dto;


import com.doc.mgt.system.docmgt.permission.dto.PermissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;

    private String name;

    private List<PermissionDTO> permissions;

}