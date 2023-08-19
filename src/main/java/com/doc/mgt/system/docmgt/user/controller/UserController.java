package com.doc.mgt.system.docmgt.user.controller;


import com.doc.mgt.system.docmgt.general.dto.Response;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.general.service.GeneralService;
import com.doc.mgt.system.docmgt.user.dto.*;
import com.doc.mgt.system.docmgt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final GeneralService generalService;

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

    @PostMapping("logout")
    @Operation(summary = "Log user out")
    public void logout(HttpServletRequest request) {
        userService.logOut(request);
    }
}
