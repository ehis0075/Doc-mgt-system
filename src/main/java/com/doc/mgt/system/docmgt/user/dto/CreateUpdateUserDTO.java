package com.doc.mgt.system.docmgt.user.dto;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CreateUpdateUserDTO {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private Long roleId;

    private String password;
}
