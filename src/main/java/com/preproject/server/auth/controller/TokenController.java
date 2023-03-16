package com.preproject.server.auth.controller;

import com.preproject.server.auth.utils.CookieUtils;
import com.preproject.server.auth.utils.JwtTokenizer;
import com.preproject.server.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
@Slf4j
public class TokenController {

    private final JwtTokenizer jwtTokenizer;

    private final CookieUtils cookieUtils;

    @RequestMapping("/reissue-token")
    public String reIssueToken(
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        String redirectURI = "";
        try {
            Cookie[] cookies = request.getCookies();
            Cookie redirectCookie = cookieUtils.findRedirectCookie(cookies);
            redirectURI = redirectCookie.getValue();
            redirectCookie.setMaxAge(0);
            Cookie refreshTokenCookie = cookieUtils.findRefreshTokenCookie(cookies);
            String refreshToken = refreshTokenCookie.getValue();
            jwtTokenizer.verifyRefreshToken(refreshToken, response);
        } catch (GeneralException e) {
                return "redirect:/login";
        }

        return "redirect:"+((Objects.equals(redirectURI, "")) ? "/":redirectURI);
    }

}