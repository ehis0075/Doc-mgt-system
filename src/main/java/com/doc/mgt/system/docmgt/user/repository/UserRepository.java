package com.doc.mgt.system.docmgt.user.repository;

import com.doc.mgt.system.docmgt.user.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
    ApplicationUser findByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


}
