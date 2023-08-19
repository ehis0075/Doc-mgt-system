package com.doc.mgt.system.docmgt.security;

import com.doc.mgt.system.docmgt.auth_token.AuthTokenService;
import com.doc.mgt.system.docmgt.exception.GeneralException;
import com.doc.mgt.system.docmgt.general.enums.ResponseCodeAndMessage;
import com.doc.mgt.system.docmgt.role.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    private final AuthTokenService authTokenService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String email, Role userRole) {
        log.info("Request to Generate jwt token for user {}", email);

        //set claims
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("auth", userRole);

        //set validity
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        //sign jwt
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        //extract token from authorization header
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //double validation
    //check with jwt for the authenticity of the token
    //check with us if the token is not revoked
    public boolean validateToken(String token) {
        return validateJwtToken(token)
                && authTokenService.validateToken(token);
    }

    private boolean validateJwtToken(String token) {
        log.info("Request to validate token");

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {

            log.info("Expired or invalid JWT token");
            throw new GeneralException(ResponseCodeAndMessage.UNAUTHORIZED_97.responseCode, "Expired or invalid JWT token!");
        }

    }
}
