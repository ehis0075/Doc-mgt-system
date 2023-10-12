//package com.doc.mgt.system.docmgt.startup;
//
//import com.doc.mgt.system.docmgt.department.model.Department;
//import com.doc.mgt.system.docmgt.department.repository.DepartmentRepository;
//import com.doc.mgt.system.docmgt.permission.enums.PermissionType;
//import com.doc.mgt.system.docmgt.permission.model.Permission;
//import com.doc.mgt.system.docmgt.permission.service.PermissionService;
//import com.doc.mgt.system.docmgt.role.model.Role;
//import com.doc.mgt.system.docmgt.role.service.AdminRoleService;
//import com.doc.mgt.system.docmgt.user.constants.SuperAdminUserConstant;
//import com.doc.mgt.system.docmgt.user.model.AdminUser;
//import com.doc.mgt.system.docmgt.user.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Component
//public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Value("${super.admin.emails}")
//    private String superAdminEmails;
//    boolean alreadySetup = false;
//    private final AdminRoleService adminRoleService;
//    private final PermissionService permissionService;
//    private final UserRepository adminUserRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final DepartmentRepository departmentRepository;
//
//    public SetupDataLoader(AdminRoleService adminRoleService, PermissionService permissionService, UserRepository adminUserRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository) {
//        this.adminRoleService = adminRoleService;
//        this.permissionService = permissionService;
//        this.adminUserRepository = adminUserRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.departmentRepository = departmentRepository;
//    }
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//
//        if (alreadySetup)
//            return;
//
//        //create all permissions
//        permissionService.createPermissionsIfNecessary();
//
//        //get all super-admin permissions
//        List<Permission> adminPermissions = getAllSuperAdminPermissions();
//
//        //create role super admin and assign all permissions and columns
//        Role adminRole = createSuperAdminRoleIfNotFound(adminPermissions);
//
//        getNew();
//
//        List<AdminUser> adminUserList = new ArrayList<>();
//
//        String[] adminEmails = superAdminEmails.split(",");
//        for (int i = 0; i < adminEmails.length; i++) {
//
//            // create super Admin
//            AdminUser superAdmin = createSuperAdmin(adminRole, adminEmails[i]);
//            if (Objects.nonNull(superAdmin)) {
//                adminUserList.add(superAdmin);
//            }
//        }
//
//        // save super admin
//        if (!adminUserList.isEmpty()) {
//            log.info(":: Saving the first set of Super admin...");
//            adminUserRepository.saveAll(adminUserList);
//        }
//
//        alreadySetup = true;
//    }
//
//    private AdminUser createSuperAdmin(Role adminRole, String email) {
//
//        Department department = new Department();
//
//        if(!departmentRepository.existsByName("Admin Department")){
//
//            department.setName("Admin Department");
//
//            department = departmentRepository.save(department);
//        } else {
//             department = departmentRepository.findByName("Admin Department");
//        }
//
//        if (!adminUserRepository.existsByEmail(email)) {
//            AdminUser adminUser = new AdminUser();
//            adminUser.setUsername(SuperAdminUserConstant.adminUserName);
//            adminUser.setEmail(email);
//            adminUser.setPassword(passwordEncoder.encode(SuperAdminUserConstant.password));
//            adminUser.setUserRole(adminRole);
//            adminUser.setDepartment(department);
//            return adminUser;
//        }
//
//        return null;
//    }
//
//
//    public void getNew(){
//
//        Department department = departmentRepository.findByName("Admin Department");
//
//        AdminUser adminUser = adminUserRepository.findByUsername("ehis");
//
//        adminUser.setDepartment(department);
//
//        adminUserRepository.save(adminUser);
//    }
//
//    @Transactional
//    public List<Permission> getAllSuperAdminPermissions() {
//        return permissionService.getPermissionsByPermissionType(PermissionType.SUPER);
//    }
//
//    @Transactional
//    public Role createSuperAdminRoleIfNotFound(List<Permission> permissions) {
//
//        Role adminRole = adminRoleService.getRoleByName(SuperAdminUserConstant.adminUserRole);
//        if (adminRole == null) {
//            adminRole = new Role();
//            adminRole = adminRoleService.addRole(adminRole, SuperAdminUserConstant.adminUserRole, permissions);
//        }
//        return adminRole;
//
//    }
//}