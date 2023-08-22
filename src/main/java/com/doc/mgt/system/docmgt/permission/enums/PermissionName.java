package com.doc.mgt.system.docmgt.permission.enums;

public enum PermissionName {
    CREATE_USER(PermissionType.SUPER), VIEW_USER(PermissionType.SUPER),
    VIEW_PERMISSION(PermissionType.SUPER),
    CREATE_ROLE(PermissionType.SUPER), UPDATE_ROLE(PermissionType.SUPER), DELETE_ROLE(PermissionType.SUPER), VIEW_ROLE(PermissionType.SUPER),
    VIEW_DOCUMENT(PermissionType.USER),  UPLOAD_DOCUMENT(PermissionType.SUPER), APPROVE_DOCUMENT(PermissionType.SUPER);
    public PermissionType permissionType;

    PermissionName(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
