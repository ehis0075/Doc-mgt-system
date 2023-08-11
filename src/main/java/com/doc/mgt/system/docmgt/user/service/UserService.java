package com.doc.mgt.system.docmgt.user.service;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.user.dto.*;
import com.doc.mgt.system.docmgt.user.model.AdminUser;

public interface UserService {
//    Response signIn(String username, String password);

    AdminUserDTO getOneAdminUser(String email);

    AdminUser getUserForLogin(String email);

    void logoutUser(String email);

    AdminUserDTO addUser(CreateUpdateUserDTO createAdminUserDto, String performedBy);

    AdminUserDTO updateUser(CreateUpdateUserDTO createAdminUserDto, Long userId, String performedBy);

    void validateThatAdminUserDoesNotExist(String email);

    UserListDTO getAllUsers(UserRequestDTO requestDTO);

    AdminUserDTO getUserDTO(AdminUser adminUser);

    UserListDTO getAllUsersByPermissionName(String permissionName);

    boolean signOut(String email);

}
