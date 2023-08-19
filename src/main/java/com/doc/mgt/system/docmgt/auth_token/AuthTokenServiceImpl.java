package com.doc.mgt.system.docmgt.auth_token;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final AuthTokenRepository authTokenRepository;

    @Override
    public void saveToken(AuthToken authToken) {
        log.info("Request to save token");
        authTokenRepository.save(authToken);
    }

    @Override
    public AuthToken getValidTokenByAccessToken(String accessToken) {
        log.info("Request to get a token not revoked");
        Optional<AuthToken> optionalAuthToken = authTokenRepository.findByAccessToken(accessToken);
        AuthToken authToken = null;
        if (optionalAuthToken.isPresent()) {
             authToken = optionalAuthToken.get();
            if (!authToken.isRevoked()) {
                log.info("Valid token gotten");
                return authToken;
            }
        }
        return authToken;
    }
    @Override
    public void revokeToken(String accessToken) {
        log.info("Request to revoked token {} to logout", accessToken);
        AuthToken authToken = getValidTokenByAccessToken(accessToken);
        if (authToken != null) {
            authToken.setRevoked(true);
            authTokenRepository.save(authToken);
            log.info("Token successfully revoked");
        }
        log.info("Token is not revoked");
    }

    @Override
    public boolean validateToken(String accessToken) {
        log.info("Request to check if token is not revoked");
        return getValidTokenByAccessToken(accessToken).isRevoked();
    }
    private List<AuthToken> findAllInvalidTokens() {
        return authTokenRepository.findAll().stream()
                .filter(AuthToken::isRevoked)
                .collect(Collectors.toList());
    }

     @Scheduled(cron = "0 0 0 * * ?", zone = "Africa/Lagos") //schedule to run every midnight
    private void deleteAllRevokedTokens() {
        final List<AuthToken> allRevokedTokens = findAllInvalidTokens();
        if (!allRevokedTokens.isEmpty()) {
            authTokenRepository.deleteAll(allRevokedTokens);
        }
    }
}
