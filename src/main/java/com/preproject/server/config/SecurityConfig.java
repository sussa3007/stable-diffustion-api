package com.preproject.server.config;

import com.preproject.server.auth.filter.JwtAuthenticationFilter;
import com.preproject.server.auth.filter.JwtAuthorizationFilter;
import com.preproject.server.auth.handler.CustomAuthenticationFailureHandler;
import com.preproject.server.auth.handler.CustomAuthenticationSuccessHandler;
import com.preproject.server.auth.handler.JwtAccessDeniedHandler;
import com.preproject.server.auth.handler.JwtAuthenticationEntryPoint;
import com.preproject.server.auth.utils.CookieUtils;
import com.preproject.server.auth.utils.JwtAuthorityUtils;
import com.preproject.server.auth.utils.JwtProperties;
import com.preproject.server.auth.utils.JwtTokenizer;
import com.preproject.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenizer jwtTokenizer;

    private final JwtAuthorityUtils authorityUtils;

    private final CookieUtils cookieUtils;

    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .apply(new CustomFilterConfig())
                .and()
                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        new JwtAuthenticationEntryPoint(cookieUtils)
                )
                .accessDeniedHandler(new JwtAccessDeniedHandler(cookieUtils))
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies(JwtProperties.COOKIE_NAME_ACCESS_TOKEN)
                .deleteCookies(JwtProperties.COOKIE_NAME_REFRESH_TOKEN)
                .deleteCookies(JwtProperties.REDIRECTION_URI);
        return http.build();
    }

    public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager =
                    builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);

            jwtAuthenticationFilter.setAuthenticationSuccessHandler(
                    new CustomAuthenticationSuccessHandler(cookieUtils));
            jwtAuthenticationFilter.setAuthenticationFailureHandler(
                    new CustomAuthenticationFailureHandler()
            );

            jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(
                    new AntPathRequestMatcher("/login","POST"));

            JwtAuthorizationFilter jwtAuthorizationFilter =
                    new JwtAuthorizationFilter(jwtTokenizer, authorityUtils,cookieUtils);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        }
    }
}
