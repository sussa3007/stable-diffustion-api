package com.preproject.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Model {
    STABLE_DIFFUSION_V2_1("Stable Diffusion V2.1","f178fa7a1ae43a9a9af01b833b9d2ecf97b1bcb0acfd2dc5dd04895e042863f1"),
    STABLE_DIFFUSION_V2("Stable Diffusion V2.0","e5e1fd333a08c8035974a01dd42f799f1cca4625aec374643d716d9ae40cf2e4"),
    OPEN_JOURNEY("Openjourney","9936c2001faa2194a261c01381f90e65261879985476014a0a37a334593a05eb"),
    MID_JOURNEY("Midjourney","436b051ebd8f68d23e83d22de5e198e0995357afef113768c20f0b6fcef23c8b"),
    FUN_KO("Funko","85a9b91c85d1a6d74a045286af193530215cb384e55ec1eaab5611a8e36030f8"),
    POKEMON("Pokemon","3554d9e699e09693d3fa334a79c58be9a405dd021d3e11281256d53185868912");


    @Getter
    final String name;

    @Getter
    final String version;
}
