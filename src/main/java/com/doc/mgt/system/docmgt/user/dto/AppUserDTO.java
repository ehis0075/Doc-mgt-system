package com.doc.mgt.system.docmgt.user.dto;

import com.doc.mgt.system.docmgt.user.model.UserRole;
import lombok.Data;

@Data
public class AppUserDTO {

    private String username;

    private String email;

    private UserRole role;

//    private String imageUrl;

    private int followerCount;

    private int followingCount;
//
//    private String fileId;

}
