package com.preproject.server.auth.details;

import com.preproject.server.auth.utils.JwtAuthorityUtils;
import com.preproject.server.constant.ErrorCode;
import com.preproject.server.member.entity.Member;
import com.preproject.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final JwtAuthorityUtils authorityUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member member = optionalMember.orElseThrow(
                () -> new UsernameNotFoundException(ErrorCode.NOT_FOUND_MEMBER.getMessage()));
        return new CustomUserDetails(member);
    }

    private final class CustomUserDetails extends Member implements UserDetails {
        public CustomUserDetails(Member member) {
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setPassword(member.getNickName());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

}
