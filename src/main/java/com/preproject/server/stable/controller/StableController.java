package com.preproject.server.stable.controller;

import com.preproject.server.stable.dto.StableImageDto;
import com.preproject.server.stable.dto.StableResponseDto;
import com.preproject.server.stable.service.StableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class StableController {

    private final StableService service;

    @GetMapping
    public ModelAndView helloWorld() {
        return new ModelAndView("stable/create-image");
    }

    @PostMapping("stable-diffusion")
    public ModelAndView stableDiffusion(
            @ModelAttribute StableImageDto dto
    ) throws Throwable {
        System.out.println(dto.toString());
        StableResponseDto response = service.callAIModel(dto);

        return new ModelAndView(
                "stable/response-image",
                Map.of(
                        "response", response
                )
        );
    }
}
