package com.preproject.server.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        log.error("# Authentication failed: {}", exception.getMessage());
        request.setAttribute("msg", "Not Found login information");
        request.setAttribute("nextPage", "/login");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        request.getRequestDispatcher("/error").forward(request, response);
    }
}
