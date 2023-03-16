package com.preproject.server.auth.controller;

import com.preproject.server.member.dto.MemberDto;
import com.preproject.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@Controller
public class MemberRegisterController {
    private final MemberService memberService;


    @GetMapping("/sign-up")
    public ModelAndView signUp(
            @RequestParam(value = "email", required = false ,defaultValue = "") String email,
            @RequestParam(value = "check", required = false ,defaultValue = "false") Boolean check
    ) {

        return new ModelAndView(
                "auth/sign-up",
                Map.of(
                        "email", email,
                        "check", check,
                        "backUrl", "/"
                )
        );
    }

    @PostMapping("/checkEmail")
    public ModelAndView validateEmail(
            @RequestParam String email
    ) {
        boolean val = memberService.verifyMemberByEmailReturnBoolean(email);
        if (val) {
            return new ModelAndView(
                    "alert",
                    Map.of(
                            "msg", "가입 가능한 이메일 입니다!.",
                            "nextPage", "/sign-up?email="+email+"&check=" + val,
                            "backUrl", "/"
                    )
            );
        } else {
            return new ModelAndView(
                    "alert",
                    Map.of(
                            "msg", "이미 존재하는 이메일 입니다!.",
                            "nextPage", "/sign-up",
                            "backUrl", "/"
                    )
            );
        }
    }

    @PostMapping("/new-signup")
    public ModelAndView register(
            @Valid @ModelAttribute MemberDto dto
    ) {

        memberService.createMember(dto);

        return new ModelAndView(
                "alert",
                Map.of(
                        "msg", "가입 성공! 다시 로그인 해주세요!.",
                        "nextPage", "/login"
                )
        );
    }

    @GetMapping("/info")
    public ModelAndView userInfo(
            HttpServletRequest request,
            Principal principal,
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        // Todo 회원 정보 반환
        return new ModelAndView(
                "auth/info"

        );
    }

    @PostMapping("/info")
    public ModelAndView updateUser(
            @Valid @ModelAttribute MemberDto dto
    ) {
        memberService.updateMember(dto);
        return new ModelAndView(
                "auth/infoconfirm",
                Map.of(
                        "msg", "Logout!! 다시 로그인 해주세요"
                )
        );
    }

}
