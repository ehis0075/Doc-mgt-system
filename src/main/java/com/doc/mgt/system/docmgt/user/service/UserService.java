package com.doc.mgt.system.docmgt.user.service;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.user.dto.AppUserDTO;
import com.doc.mgt.system.docmgt.user.dto.SignUpRequest;
import com.doc.mgt.system.docmgt.user.dto.UserListDTO;
import com.doc.mgt.system.docmgt.user.dto.UserRequestDTO;
import com.doc.mgt.system.docmgt.user.model.ApplicationUser;

public interface UserService {
    Response signIn(String username, String password);

    AppUserDTO signup(SignUpRequest request);

    AppUserDTO updateUser(SignUpRequest request, Long userId, String username);

    void deleteUser(Long userId, String username);

    AppUserDTO getUserDTO(ApplicationUser request);

    ApplicationUser getUserByUsername(String username);

    ApplicationUser saveUser(ApplicationUser user);

    UserListDTO getAllUsers(UserRequestDTO request);

}
