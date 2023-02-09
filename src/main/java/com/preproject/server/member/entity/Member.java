package com.preproject.server.member.entity;

import com.preproject.server.auth.utils.JwtAuthorityUtils;
import com.preproject.server.member.dto.MemberDto;
import com.preproject.server.stable.entity.Stable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(length = 100,nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @ToString.Exclude
    @OrderBy("stableId")
    @OneToMany(mappedBy = "member")
    private final Set<Stable> stables = new LinkedHashSet<>();

    public void addStable(Stable stable) {
        this.stables.add(stable);
    }

    public static Member of(MemberDto dto) {
        return Member.builder()
                .nickName(dto.getNickName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                .build();
    }
}
