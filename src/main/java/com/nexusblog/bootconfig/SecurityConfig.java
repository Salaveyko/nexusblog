package com.nexusblog.bootconfig;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers(
                                        PathRequest.toStaticResources().atCommonLocations()
                                ).permitAll()
                                .requestMatchers(
                                        "/",
                                        "/login",
                                        "/registration",
                                        "/registrationConfirm",
                                        "/uploads/**"
                                ).permitAll()
                                .requestMatchers(
                                        "/profile/**",
                                        "/**"
                                ).hasAnyRole("USER", "ADMIN")

                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/myblog")
                        .permitAll()
                )
                .rememberMe()
                .key("superDuper-key")
                .tokenValiditySeconds(86400)
                .and()
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .securityContext()
                .securityContextRepository(securityContextRepository());

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
