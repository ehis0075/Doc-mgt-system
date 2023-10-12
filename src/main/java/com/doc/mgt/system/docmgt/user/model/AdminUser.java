package com.doc.mgt.system.docmgt.user.model;

import com.doc.mgt.system.docmgt.department.model.Department;
import com.doc.mgt.system.docmgt.role.dto.RoleDTO;
import com.doc.mgt.system.docmgt.role.model.Role;
import com.doc.mgt.system.docmgt.user.dto.AdminUserDTO;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity(name = "application_users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne
    @ToString.Exclude
    private Role userRole;

    @OneToOne
    @ToString.Exclude
    private Department department;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminUser)) return false;
        AdminUser that = (AdminUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static AdminUserDTO getUserDTO(AdminUser adminUser) {

        AdminUserDTO adminUserDTO = new AdminUserDTO();

        BeanUtils.copyProperties(adminUser, adminUserDTO);

        //get role dto
        RoleDTO roleDTO = Role.getAdminRoleDTO(adminUser.getUserRole());
        adminUserDTO.setRole(roleDTO);

        return adminUserDTO;
    }
}
