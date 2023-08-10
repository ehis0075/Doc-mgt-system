package com.doc.mgt.system.docmgt.role.model;


import com.doc.mgt.system.docmgt.permission.model.Permission;
import lombok.Data;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Permission> permissions;

    public void setPermissions(List<Permission> permissions) {
        this.permissions = new HashSet<>(permissions);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role adminRole = (Role) o;
        return id != null && Objects.equals(id, adminRole.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
