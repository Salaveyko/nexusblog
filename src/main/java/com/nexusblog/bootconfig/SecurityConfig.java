package com.nexusblog.bootconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                                        "/css/**",
                                        "/script/**",
                                        "/blog",
                                        "/blog/add"
                                ).permitAll()
                                .requestMatchers("/myblog/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(
                                        "/login",
                                        "/registration"
                                ).anonymous()
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/blog/myblog")
                        .permitAll()
                )
                .rememberMe()
                .key("superDuper-key")
                .tokenValiditySeconds(86400)
                .and()
                .logout((logout) -> logout
                        .logoutSuccessUrl("/blog")
                        .permitAll())

                .exceptionHandling()
                .accessDeniedPage("/access-denied");

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
