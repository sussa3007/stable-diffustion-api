package com.preproject.server.auth.handler;

import com.preproject.server.auth.utils.CookieUtils;
import com.preproject.server.auth.utils.JwtProperties;
import com.preproject.server.constant.ErrorCode;
import com.preproject.server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final CookieUtils cookieUtils;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");
        logExceptionMessage(authException, (Exception) exception);

        if (exception instanceof GeneralException) {
            ErrorCode errorCode = ((GeneralException) exception).getErrorCode();
            if (errorCode.equals(ErrorCode.NOT_FOUND_COOKIE)) {
                request.setAttribute("msg", "Not found Cookie & login information");
                request.setAttribute("nextPage", "/login");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                request.getRequestDispatcher("/error").forward(request, response);
            } else if (errorCode.equals(ErrorCode.EXPIRED_ACCESS_TOKEN)) {
                Cookie cookie = cookieUtils.createCookie(
                        JwtProperties.REDIRECTION_URI,
                        request.getRequestURI(),
                        JwtProperties.EXPIRATION_TIME);
                response.addCookie(cookie);
                response.sendRedirect("/auth/reissue-token");
            }

        } else {
            Exception ex = (Exception) exception;
            log.error("### exception = {}", ex.getMessage());
            response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        }
    }

    private void logExceptionMessage(AuthenticationException authException, Exception exception) {
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("Unauthorized error happened: {}", message);
    }
}
