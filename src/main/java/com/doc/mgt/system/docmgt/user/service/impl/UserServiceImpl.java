package com.doc.mgt.system.docmgt.user.service.impl;

import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.role.dto.RoleDTO;
import com.doc.mgt.system.docmgt.role.model.Role;
import com.doc.mgt.system.docmgt.role.repository.AdminRoleRepository;
import com.doc.mgt.system.docmgt.role.service.AdminRoleService;
import com.doc.mgt.system.docmgt.security.JwtTokenProvider;
import com.doc.mgt.system.docmgt.user.dto.*;
import com.doc.mgt.system.docmgt.user.imodel.UserBasicInfoI;
import com.doc.mgt.system.docmgt.user.model.AdminUser;
import com.doc.mgt.system.docmgt.user.repository.UserRepository;
import com.doc.mgt.system.docmgt.user.service.UserService;
import com.doc.mgt.system.docmgt.util.GeneralUtil;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AdminRoleRepository adminRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GeneralService generalService;
    private final AuthenticationManager authenticationManager;

    private final AdminRoleService adminRoleService;
    private final UserRepository adminUserRepository;


    @Override
    public Response signIn(String username, String password) {
        log.info("Request to login with username {}", username);

        try {
            // attempt authentication
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            //if successful, set authentication object in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Role userRole = userRepository.findByUsername(username).getUserRole();

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
    public UserListDTO getAllUsers(UserRequestDTO requestDTO) {
        log.info("Getting Admin Users List");

        Pageable paged = generalService.getPageableObject(requestDTO.getSize(), requestDTO.getPage());
        Page<AdminUser> superUserPage = adminUserRepository.findAll(paged);

        UserListDTO adminUserListDTO = new UserListDTO();

        List<AdminUser> adminUserList = superUserPage.getContent();
        if (superUserPage.getContent().size() > 0) {
            adminUserListDTO.setHasNextRecord(superUserPage.hasNext());
            adminUserListDTO.setTotalCount((int) superUserPage.getTotalElements());
        }

        List<AdminUserDTO> adminUserDTOS = convertToAdminUserDTOList(adminUserList);
        adminUserListDTO.setUserDTOList(adminUserDTOS);

        return adminUserListDTO;
    }

    @Override
    public AdminUserDTO getOneAdminUser(String email) {
        log.info("Getting Admin User using Email => {}", email);

        AdminUser adminUser = Optional.ofNullable(adminUserRepository.findByEmail(email)).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88));

        return getUserDTO(adminUser);
    }

    @Override
    public AdminUser getUserForLogin(String email) {
        log.info("Validating that user => {} exist in db ", email);

        AdminUser user = adminUserRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88);
        }
        return user;
    }

    @Override
    public void logoutUser(String email) {
        log.info("Logging out User using Email => {}", email);

        // call auth service to log out

    }

    @Override
    public AdminUserDTO addUser(CreateUpdateUserDTO createUserDto, String performedBy) {
        log.info("creating a user with payload = {}", createUserDto);

        //validate first name, last name and phone number
        GeneralUtil.validateUsernameAndEmail(createUserDto.getUsername(), createUserDto.getEmail());

        // email to lower case
        String email = createUserDto.getEmail().toLowerCase();

        // check that email is not null or empty
        validateEmail(email);

        //get the role
        Role role = getRole(createUserDto.getRoleId());

        //save user

        //create new user
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(createUserDto.getUsername());
        adminUser.setEmail(createUserDto.getEmail());
        adminUser.setEmail(email);
        adminUser.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        adminUser.setUserRole(role);

        // save to db
        AdminUser savedAdminUser = saveAdminUser(adminUser);


        String roleName = adminUser.getUserRole().getName();

        // convert to dto
        return getUserDTO(savedAdminUser);
    }

    @Override
    public AdminUserDTO updateUser(CreateUpdateUserDTO createAdminUserDto, Long adminUserId, String performedBy) {
        log.info("Request to update an admin user with admin user id = {} with payload = {}", adminUserId, createAdminUserDto);

        AdminUser adminUser = getAdminUserById(adminUserId);
        Role role = adminUser.getUserRole();

        // check that role id is not null or empty
        if (!Objects.equals(createAdminUserDto.getRoleId(), adminUser.getUserRole().getId())) {
            role = getRole(createAdminUserDto.getRoleId());
        }

        // check that user cannot modify itself
        if (Objects.equals(adminUser.getEmail(), performedBy)) {
            throw new GeneralException(ResponseCodeAndMessage.OPERATION_NOT_SUPPORTED_93.responseMessage, "Logged in Admin user cannot modify itself!");
        }

        adminUser.setUserRole(role);

        // save to db
        adminUser = saveAdminUser(adminUser);

        // convert to dto
        return getUserDTO(adminUser);
    }

    @Override
    public void validateThatAdminUserDoesNotExist(String email) {
        log.info("Validating if admin user email {} already exist", email);

        validateEmail(email);
    }

    @Override
    public AdminUserDTO getUserDTO(AdminUser adminUser) {
        log.info("Converting Admin User to Admin User DTO");

        AdminUserDTO adminUserDTO = new AdminUserDTO();

        generalService.createDTOFromModel(adminUser, adminUserDTO);

        //get role dto
        RoleDTO roleDTO = adminRoleService.getAdminRoleDTO(adminUser.getUserRole());
        adminUserDTO.setRole(roleDTO);

        return adminUserDTO;
    }

    @Override
    public UserListDTO getAllUsersByPermissionName(String permissionName) {
        log.info("Getting Admin Users List with permissionName => {}", permissionName);

        List<UserBasicInfoI> usersWithPermission = adminUserRepository.getUsersWithPermission(permissionName);

        UserListDTO adminUserListDTO = new UserListDTO();
        List<AdminUserDTO> adminUserDTOListWithPermission = new ArrayList<>();

        usersWithPermission.forEach(adminUserBasicInfoI -> {
            AdminUserDTO userDTO = new AdminUserDTO();
            userDTO.setId(adminUserBasicInfoI.id());
            userDTO.setEmail(adminUserBasicInfoI.email());
            userDTO.setUsername(adminUserBasicInfoI.username());
            adminUserDTOListWithPermission.add(userDTO);
        });

        adminUserListDTO.setTotalCount(usersWithPermission.size());
        adminUserListDTO.setUserDTOList(adminUserDTOListWithPermission);
        return adminUserListDTO;
    }

    @Override
    public boolean signOut(String email) {
        AdminUser adminUser = getUserForLogin(email);
        logoutUser(email);
        return true;
    }

//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//
//        // Perform any additional cleanup or custom actions here
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        return "redirect:/login?logout";
//    }

    private List<AdminUserDTO> convertToAdminUserDTOList(List<AdminUser> adminUserList) {
        log.info("Converting Admin User List to Admin User DTO List");

        return adminUserList.stream().map(this::getUserDTO).collect(Collectors.toList());
    }

    private void validateEmail(String email) {

//        if (GeneralUtil.invalidEmail(email)) {
//            log.info("Admin User email {} is invalid", email);
//            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Admin User Email " + email + " is invalid!");
//        }

        if (adminUserRepository.existsByEmail(email)) {
            throw new GeneralException(ResponseCodeAndMessage.ALREADY_EXIST_86.responseCode, "Email already exist");
        }
    }

    private Role getRole(Long roleId) {
        if (Objects.isNull(roleId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "Role id cannot be empty!");
        }

        return adminRoleRepository.findById(roleId).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Role id cannot be empty"));
    }

    private AdminUser getAdminUserById(Long adminUserId) {
        log.info("Getting admin user by adminUserId {}", adminUserId);

        // check that admin user id is not null or empty
        if (!Objects.nonNull(adminUserId)) {
            throw new GeneralException(ResponseCodeAndMessage.INCOMPLETE_PARAMETERS_91.responseCode, "admin user id cannot be null or empty!");
        }

        return adminUserRepository.findById(adminUserId).orElseThrow(() -> new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88.responseCode, "Admin User not found"));
    }

    private AdminUser saveAdminUser(AdminUser adminUser) {
        log.info("::: saving admin user to db :::");
        return adminUserRepository.save(adminUser);
    }
}
