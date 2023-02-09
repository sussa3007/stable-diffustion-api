package com.preproject.server.member.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberDto {

    private String nickName;

    private String phoneNumber;

    private String email;

    private String password;
}
