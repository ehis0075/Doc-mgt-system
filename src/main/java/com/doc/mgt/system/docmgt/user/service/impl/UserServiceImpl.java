package com.doc.mgt.system.docmgt.user.service.impl;

import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.role.dto.RoleDTO;
import com.doc.mgt.system.docmgt.role.model.Role;
import com.doc.mgt.system.docmgt.role.repository.AdminRoleRepository;
import com.doc.mgt.system.docmgt.role.service.AdminRoleService;
import com.doc.mgt.system.docmgt.security.JwtService;
import com.doc.mgt.system.docmgt.token.Token;
import com.doc.mgt.system.docmgt.token.TokenRepository;
import com.doc.mgt.system.docmgt.token.TokenType;
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

    //    private final HttpServletRequest request;
//    private final HttpServletResponse response;
//    private final Authentication authentication;
//
//    private final LogoutService logoutService;
    private final TokenRepository tokenRepository;
    private final AdminRoleRepository adminRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
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

            AdminUser user = userRepository.findByUsername(username);

            Role userRole = user.getUserRole();

            //generate jwt token
            String token = jwtService.generateToken(username, userRole);

//            // revoke user token
//            revokeUserTokens(username);
//
//            // save the user token
//            saveUserToken(user, token);

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
    public AdminUser getUserByUsername(String username) {
        log.info("Validating that user => {} exist in db ", username);

        AdminUser user = adminUserRepository.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new GeneralException(ResponseCodeAndMessage.RECORD_NOT_FOUND_88);
        }
        return user;
    }

    @Override
    public String getLoggedInUser() {
        log.info("Getting logged in user");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();

    }

    @Override
    public AdminUserDTO addUser(CreateUpdateUserDTO createUserDto, String performedBy) {
        log.info("creating a user with payload {} by user {}", createUserDto, performedBy);

        //validate first name, last name and phone number
        GeneralUtil.validateUsernameAndEmail(createUserDto.getUsername(), createUserDto.getEmail());

        // email to lower case
        String email = createUserDto.getEmail().toLowerCase();

        // check that email is not null or empty
        validateEmail(email);

        //get the role
        Role role = getRole(createUserDto.getRoleId());


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
        log.info("Request to update a user with user id {} with payload {} by user {}", adminUserId, createAdminUserDto, performedBy);

        AdminUser adminUser = getAdminUserById(adminUserId);
        Role role = adminUser.getUserRole();

        // check that role id is not null or empty
        if (!Objects.equals(createAdminUserDto.getRoleId(), adminUser.getUserRole().getId())) {
            role = getRole(createAdminUserDto.getRoleId());
        }

        // check that user cannot modify itself
        if (Objects.equals(adminUser.getEmail(), performedBy)) {
            throw new GeneralException(ResponseCodeAndMessage.OPERATION_NOT_SUPPORTED_93.responseMessage, "Logged in user cannot modify itself!");
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
    public void revokeUserTokens(String username) {
        log.info("Request to revoke the token for user {}", username);

        List<Token> validUserToken = tokenRepository.findAllByUser_Username(username);
        if (Objects.isNull(validUserToken))
            return;
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(AdminUser user, String jwtToken) {
        log.info("Request to save token for user {}", user.getUsername());

        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private List<AdminUserDTO> convertToAdminUserDTOList(List<AdminUser> adminUserList) {
        log.info("Converting Admin User List to Admin User DTO List");

        return adminUserList.stream().map(this::getUserDTO).collect(Collectors.toList());
    }

    private void validateEmail(String email) {

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
