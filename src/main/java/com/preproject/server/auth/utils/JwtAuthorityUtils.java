package com.preproject.server.auth.utils;

import com.preproject.server.member.entity.Member;
import com.preproject.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthorityUtils {

    private String adminMailAddress = "admin@test.com";

    private final MemberRepository memberRepository;

    public static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    public static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");
    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");
    private final List<String> USER_ROLES_STRING = List.of("USER");

    public static final List<String> ADMIN_ROLES_STRING_CALL = List.of("ADMIN", "USER");
    public static final List<String> USER_ROLES_STRING_CALL = List.of("USER");


    /* 권한 부여 메소드 */
    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        List<GrantedAuthority> result =  roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toList());
        return result;
    }

    public static List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> result =  roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role))
                .collect(Collectors.toList());
        return result;
    }

    public List<String> createRoles(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isPresent()) {
            return findMember.get().getRoles();
        } else {
            if (email.equals(adminMailAddress)) {
                return ADMIN_ROLES_STRING;
            }
            return USER_ROLES_STRING;
        }
    }

}