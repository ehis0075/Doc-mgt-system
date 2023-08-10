package com.doc.mgt.system.docmgt.user.model;

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
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private int numberOfFollowers;

    private int numberOfFollowing;

//    private String imageUrl;
//
//    private String fileId;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<ApplicationUser> followers;  //people following this user

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<ApplicationUser> following;  //people this user is following

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationUser)) return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
