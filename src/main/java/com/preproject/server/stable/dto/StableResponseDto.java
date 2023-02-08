package com.preproject.server.stable.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StableResponseDto {

    Object input;

    Object output;

    public static StableResponseDto of(Object input, Object output) {
        return new StableResponseDto(input, output);
    }
}
