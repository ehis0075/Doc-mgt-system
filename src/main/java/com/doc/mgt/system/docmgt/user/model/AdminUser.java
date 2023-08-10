package com.doc.mgt.system.docmgt.user.model;

import com.doc.mgt.system.docmgt.role.model.Role;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity(name = "application_users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne
    @ToString.Exclude
    private Role userRole;


//    private String imageUrl;
//
//    private String fileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminUser)) return false;
        AdminUser that = (AdminUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
