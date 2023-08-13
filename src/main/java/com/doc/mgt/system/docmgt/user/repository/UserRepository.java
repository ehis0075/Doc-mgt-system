package com.doc.mgt.system.docmgt.user.repository;


import com.doc.mgt.system.docmgt.user.imodel.UserBasicInfoI;
import com.doc.mgt.system.docmgt.user.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<AdminUser, Long> {
    AdminUser findByUsername(String username);

    boolean existsByEmail(String email);

    AdminUser findByEmail(String email);

    boolean existsByUserRole_Id(Long role_id);

    boolean existsByUserRole_Name(String name);

    @Query(value = "select au.id, au.email, au.phone_number, au.first_name, au.last_name from admin_user au\n" +
            "    inner join roles_permissions arp on au.role_id = arp.role_id\n" +
            "    inner join permission p on p.id = arp.permission_id\n" +
            "where p.name = ?1 AND au.enabled = 1", nativeQuery = true)
    List<UserBasicInfoI> getUsersWithPermission(String permissionName);


}
