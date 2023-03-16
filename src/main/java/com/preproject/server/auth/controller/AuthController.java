package com.preproject.server.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class AuthController {



    @GetMapping("/login")
    public String login() {

        return "auth/login";
    }


    @GetMapping("/logout")
    public String logout(@RequestHeader("referer") String referer, Model model) {
        model.addAttribute("backUrl", referer);

        return "auth/logout";
    }


}
