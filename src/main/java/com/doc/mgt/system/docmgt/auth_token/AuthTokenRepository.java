package com.doc.mgt.system.docmgt.auth_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByAccessToken(String accessToken);

}
