package com.preproject.server.auth.handler;

import com.preproject.server.auth.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        log.info("# Authenticated successfully!");
        if (request.getCookies() == null) {
            response.setStatus(HttpStatus.OK.value());
            response.sendRedirect("/");
        }
        Cookie redirectCookie = cookieUtils.findNullSafeRedirectCookie(request.getCookies());
        String redirectURI = redirectCookie.getValue();
        redirectCookie.setMaxAge(0);

        response.sendRedirect(((Objects.equals(redirectURI, "")) ? "/" : redirectURI));

    }
}
