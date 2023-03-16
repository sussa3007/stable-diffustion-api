package com.preproject.server.auth.handler;

import com.preproject.server.auth.utils.CookieUtils;
import com.preproject.server.constant.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final CookieUtils cookieUtils;
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.warn("Forbidden error happened: {}", accessDeniedException.getMessage());
        cookieUtils.clearCookies(request.getCookies(),response);
        request.setAttribute("msg", ErrorCode.ACCESS_DENIED.getMessage()+" Return Home");
        request.setAttribute("nextPage", "/");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        request.getRequestDispatcher("/error").forward(request, response);

    }
}
