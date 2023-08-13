package com.doc.mgt.system.docmgt.role.repository;


import com.doc.mgt.system.docmgt.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    boolean existsByName(String name);

}
