package com.doc.mgt.system.docmgt.auth_token;

public interface AuthTokenService {
    void saveToken(AuthToken authToken);
    AuthToken getValidTokenByAccessToken(String accessToken);
    void revokeToken(String accessToken);
    boolean validateToken(String accessToken);
}
