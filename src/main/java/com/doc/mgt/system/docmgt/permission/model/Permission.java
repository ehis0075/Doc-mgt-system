package com.doc.mgt.system.docmgt.permission.model;

import com.doc.mgt.system.docmgt.permission.enums.PermissionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean userA = false;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean superAdmin = true;

    public void setPermissionType(PermissionType permissionType) {
        this.userA = permissionType.equals(PermissionType.USER);
        this.superAdmin = true;
    }

    public PermissionType getPermissionType() {
        return this.userA ? PermissionType.USER : PermissionType.SUPER;
    }
}