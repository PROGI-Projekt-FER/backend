package com.ticketswap.config;

import com.ticketswap.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll()
                )
            .oauth2Login(oauth2Login ->
                oauth2Login
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(customOAuth2UserService)
                        )
            )
            .logout( logout ->
                    logout
                            .logoutSuccessUrl("/api")
                            .deleteCookies("JSESSIONID")  // Remove session cookies
                            .invalidateHttpSession(true)  // Invalidate session
            );

        return http.build();
    }
}