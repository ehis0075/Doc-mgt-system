package com.doc.mgt.system.docmgt.auth_token;

import com.doc.mgt.system.docmgt.user.model.AdminUser;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Column(name = "token", columnDefinition = "TEXT", nullable = false)
    private String accessToken;

    @OneToOne
    public AdminUser user;

    private boolean revoked;
}
