package com.doc.mgt.system.docmgt.permission.repository;


import com.doc.mgt.system.docmgt.permission.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);

    boolean existsByName(String name);

    List<Permission> findByUserATrue();

    List<Permission> findBySuperAdminTrue();

    Page<Permission> findBySuperAdminTrue(Pageable pageable);

    Page<Permission> findByUserATrue(Pageable pageable);

}
