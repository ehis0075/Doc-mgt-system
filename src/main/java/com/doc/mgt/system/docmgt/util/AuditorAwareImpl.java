package com.doc.mgt.system.docmgt.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    private static final ThreadLocal<String> loggedInUser = new ThreadLocal<>();

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(loggedInUser.get());
        // Can use Spring Security to return currently logged in user
        // return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()
    }

    public static String setCurrentUser(HttpServletRequest request) {
        log.info("Setting active user");
        String user = request.getHeader("user");
        loggedInUser.set(user);
        return user;
    }
}