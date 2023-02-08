package com.preproject.server.stable.controller;

import com.preproject.server.stable.dto.StableImageDto;
import com.preproject.server.stable.dto.StableResponseDto;
import com.preproject.server.stable.service.StableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class StableController {

    private final StableService service;

    @GetMapping
    public ModelAndView home() {
        return new ModelAndView("stable/create-image");
    }

    @PostMapping("loading")
    public ModelAndView loading(
            @ModelAttribute StableImageDto dto
    ) throws Throwable {
        Map<String, Object> map = service.callAIModel(dto);

        return new ModelAndView("loading",
                Map.of(
                        "msg", "이미지 생성중 입니다.",
                        "nextPage",
                        "/stable-diffusion?url=" +map.get("url")
                                +"&model=" +map.get("model")
                                +"&key="+map.get("key")
                )
        );
    }

    @GetMapping("stable-diffusion")
    public ModelAndView stableDiffusion(
            @RequestParam Map<String, Object> param
    ) throws Throwable {
        StableResponseDto response = service.getStableImageDto(
                (String) param.get("url"),
                (String) param.get("model"),
                (String) param.get("key")
        );

        return new ModelAndView(
                "stable/response-image",
                Map.of(
                        "response", response
                )
        );
    }
}
