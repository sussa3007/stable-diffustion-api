package com.preproject.server.auth.utils;


import com.preproject.server.constant.ErrorCode;
import com.preproject.server.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
@Slf4j
public class CookieUtils {

    public Cookie createCookie(String cookieName, String value, int maxAge) {
        String newVelue = value;
        if (value.equals("/error")) {
            newVelue = "/";
        }
        Cookie cookie = new Cookie(cookieName, newVelue);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie findRedirectCookie(Cookie[] cookies) {
        if (cookies == null) throw new GeneralException(ErrorCode.NOT_FOUND_COOKIE);
        Cookie findCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.REDIRECTION_URI))
                .findFirst().orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_COOKIE));
        if (findCookie.getValue().equals("/error")) {
            findCookie.setValue("/");
        }
        return findCookie;
    }

    public Cookie findNullSafeRedirectCookie(Cookie[] cookies) {
        if (cookies == null) throw new GeneralException(ErrorCode.NOT_FOUND_COOKIE);
        Cookie findCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.REDIRECTION_URI))
                .findFirst().orElse(new Cookie(JwtProperties.REDIRECTION_URI, ""));
        if (findCookie.getValue().equals("/error")) {
            findCookie.setValue("/");
        }
        return findCookie;
    }

    public Cookie findRefreshTokenCookie(Cookie[] cookies) {
        if (cookies == null) throw new GeneralException(ErrorCode.NOT_FOUND_COOKIE);
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME_REFRESH_TOKEN))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_COOKIE));
    }

    public Cookie findAccessTokenCookie(Cookie[] cookies) {
        if (cookies == null) throw new GeneralException(ErrorCode.NOT_FOUND_COOKIE);
        Cookie findCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME_ACCESS_TOKEN))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_COOKIE));
        String jws = findCookie.getValue().replace("Bearer", "");
        findCookie.setValue(jws);
        return findCookie;
    }

    public void clearCookies(Cookie[] cookies, HttpServletResponse response) {
        if (cookies == null) throw new GeneralException(ErrorCode.NOT_FOUND_COOKIE);
        Arrays.stream(cookies)
                .forEach(cookie -> {
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        });
    }
}
