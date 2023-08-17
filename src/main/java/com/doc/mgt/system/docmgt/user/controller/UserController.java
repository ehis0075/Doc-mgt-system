package com.doc.mgt.system.docmgt.user.controller;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.user.dto.*;
import com.doc.mgt.system.docmgt.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    //    private final HttpServletRequest request;
//    private final HttpServletResponse response;
//    private final Authentication authentication;
    private final UserService userService;

    private final GeneralService generalService;

    public UserController(UserService userService, GeneralService generalService) {

        this.userService = userService;
        this.generalService = generalService;
    }

    //    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping("sign-up")
    public Response signUp(@Valid @RequestBody CreateUpdateUserDTO signUpRequest, Principal principal) {
        AdminUserDTO data = userService.addUser(signUpRequest, principal.getName());
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    @PostMapping("sign-in")
    public ResponseEntity<Response> signIn(@RequestBody SignInRequest request) {
        Response data = userService.signIn(request.getUsername(), request.getPassword());
        if (data.getResponseCode().equals(ResponseCodeAndMessage.SUCCESSFUL_0.responseCode))
            return new ResponseEntity<>(data, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

//    @PostMapping("sign-out")
//    public ResponseEntity<?> signOut() {
//        userService.logoutUser(request, response, authentication);
//        return new ResponseEntity<>("Log out successful", HttpStatus.OK);
//    }

    //    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping("/update/{userId}")
    public Response updateUser(@Valid @RequestBody CreateUpdateUserDTO signUpRequest, @PathVariable Long userId, Principal principal) {
        AdminUserDTO data = userService.updateUser(signUpRequest, userId, principal.getName());
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

    //    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping()
    public Response getAll(@Valid @RequestBody UserRequestDTO request) {
        UserListDTO data = userService.getAllUsers(request);
        return generalService.prepareResponse(ResponseCodeAndMessage.SUCCESSFUL_0, data);
    }

}
