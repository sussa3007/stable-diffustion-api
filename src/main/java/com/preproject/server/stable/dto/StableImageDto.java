package com.preproject.server.stable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StableImageDto {

    String key;

    String model;

    String prompt;

    String negative_prompt;

    // default : 768
    Integer width;

    // default : 768
    Integer height;

    // default : 50
    Integer num_inference_steps;

    // default : 7.5
    Double guidance_scale;

    // default : -1
    Integer seed;

    public static StableImageDto of(String key, String model, Map<String, Object> input) {
        String prompt = (String) Optional.ofNullable(input.get("prompt")).orElse("Not Return");
        String negative_prompt = (String) Optional.ofNullable(input.get("negative_prompt")).orElse("Not Return");
        Integer width = Optional.ofNullable((Double)input.get("width")).orElse(512.0).intValue();
        Integer height = Optional.ofNullable((Double)input.get("height")).orElse(512.0).intValue();
        Integer num_inference_steps = Optional.ofNullable((Double) input.get("num_inference_steps")).orElse(50.0).intValue();
        Double guidance_scale = (Double) Optional.ofNullable(input.get("guidance_scale")).orElse(7.5);
        Integer seed = Optional.ofNullable((Double)input.get("seed")).orElse(-1.0).intValue();
        return new StableImageDto(
                key,
                model,
                prompt,
                negative_prompt,
                width,
                height,
                num_inference_steps,
                guidance_scale,
                seed
        );
    }


}
