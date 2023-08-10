package com.doc.mgt.system.docmgt.user.service.impl;


import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.security.JwtTokenProvider;
import com.doc.mgt.system.docmgt.user.dto.*;
import com.doc.mgt.system.docmgt.user.model.ApplicationUser;
import com.doc.mgt.system.docmgt.user.model.UserRole;
import com.doc.mgt.system.docmgt.user.repository.UserRepository;
import com.doc.mgt.system.docmgt.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralService generalService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AppUserDTO signup(SignUpRequest request) {
        log.info("Request to create user with payload ={}", request);

        //check if user exists
        validateEmailAndUserName(request);

        //save user
        ApplicationUser user = new ApplicationUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(UserRole.ROLE_USER);

        // upload doc
//        Map<String, String> map = imageService.upload(request.getBase64(), request.getUsername());
//        String fileId = map.get("fileId");
//        String url = map.get("url");
//
//        user.setImageUrl(url);
//        user.setFileId(fileId);

        user = saveUser(user);

        return getUserDTO(user);
    }

    @Override
    public Response signIn(String username, String password) {
        log.info("Request to login with username {}", username);

        try {
            // attempt authentication
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            //if successful, set authentication object in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserRole userRole = userRepository.findByUsername(username).getUserRole();

            //generate jwt token
            String token = jwtTokenProvider.generateToken(username, userRole);

            Response response = new Response();
            response.setResponseCode(ResponseCodeAndMessage.SUCCESSFUL_0.responseCode);
            response.setResponseMessage(ResponseCodeAndMessage.SUCCESSFUL_0.responseMessage);
            response.setData(SignInResponse.builder().username(username).userRole(userRole).accessToken(token).build());

            log.info("Successfully logged-in user {}", username);

            return response;

        } catch (AuthenticationException e) {
            log.info("Incorrect User credentials");
            throw new GeneralException(ResponseCodeAndMessage.AUTHENTICATION_FAILED_95);
        }
    }

    @Override
    public AppUserDTO updateUser(SignUpRequest request, Long userId, String username) {
        log.info("Request to update user {} with payload ={}", userId, request);

        //check if user exists
        validateEmailAndUserName(request);

        //save user
        ApplicationUser user = getUserByUsername(username);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

//        String fieldId = addProfileImage(request);
//        user.setImageUrl(fieldId);

        saveUser(user);

        return getUserDTO(user);
    }

    @Override
    public void deleteUser(Long userId, String username) {
        log.info("Request to delete user {}", userId);

        //save user
        ApplicationUser user = getUserByUsername(username);
        userRepository.delete(user);
    }

    @Override
    public AppUserDTO getUserDTO(ApplicationUser request) {
        log.info("Getting User DTO");
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername(request.getUsername());
        appUserDTO.setEmail(request.getEmail());
        appUserDTO.setRole(UserRole.ROLE_USER);
        appUserDTO.setFollowerCount(request.getNumberOfFollowers());
//        appUserDTO.setFileId(request.getFileId());
//        appUserDTO.setImageUrl(request.getImageUrl());

        return appUserDTO;
    }

    @Override
    public ApplicationUser getUserByUsername(String username) {
        log.info("Request to get a user by username {}", username);

        ApplicationUser user = userRepository.findByUsername(username);

        if (Objects.isNull(user)) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "User does Not exist");
        }
        return user;
    }


    @Override
    public ApplicationUser saveUser(ApplicationUser user) {

        user = userRepository.save(user);
        log.info("successfully saved user to db");
        return user;
    }

    @Override
    public UserListDTO getAllUsers(UserRequestDTO request) {
        log.info("Getting Users List");

        Pageable paged = generalService.getPageableObject(request.getSize(), request.getPage());
        Page<ApplicationUser> applicationUsers = userRepository.findAll(paged);
        log.info("user list {}", applicationUsers);

        UserListDTO userListDTO = new UserListDTO();

        List<ApplicationUser> userList = applicationUsers.getContent();
        if (applicationUsers.getContent().size() > 0) {
            userListDTO.setHasNextRecord(applicationUsers.hasNext());
            userListDTO.setTotalCount((int) applicationUsers.getTotalElements());
        }

        List<AppUserDTO> userDTOList = convertToUserDTOList(userList);
        userListDTO.setUserDTOList(userDTOList);

        return userListDTO;
    }


    private void validateEmailAndUserName(SignUpRequest request) {

        boolean isExist = userRepository.existsByEmail(request.getEmail());

        boolean isExist2 = userRepository.existsByUsername(request.getUsername());

        if (isExist) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Email already exist");
        }

        if (isExist2) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Username already exist");
        }
    }

    private List<AppUserDTO> convertToUserDTOList(List<ApplicationUser> adminUserList) {
        log.info("Converting User List to User DTO List");

        return adminUserList.stream().map(this::getUserDTO).collect(Collectors.toList());
    }

}
