package com.doc.mgt.system.docmgt.user.dto;

import com.doc.mgt.system.docmgt.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String username;
    private UserRole userRole;
    private String accessToken;
}
