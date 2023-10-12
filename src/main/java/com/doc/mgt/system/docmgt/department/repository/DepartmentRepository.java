package com.doc.mgt.system.docmgt.department.repository;

import com.doc.mgt.system.docmgt.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByName(String name);

    Department findByName(String name);
}
