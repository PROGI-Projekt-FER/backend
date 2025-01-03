package com.ticketswap.config;

import com.ticketswap.service.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
                .csrf().disable()
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
                        .successHandler(new CustomOAuth2SuccessHandler())
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

@Component
class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Extract user details from the Authentication object
        Object principal = authentication.getPrincipal();

        String username = "";
        String email = "";
        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            // Access user attributes (e.g., email, name, etc.)
            username = oAuth2User.getAttribute("name"); // Change "name" to the key for the attribute you need
            email = oAuth2User.getAttribute("email"); // Change "name" to the key for the attribute you need
        }

        // Encode parameters to ensure special characters are handled correctly
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        // Redirect to frontend with the username as a parameter (adjust as needed)
//        String frontendRedirectUrl = "https://frontend-edlb.onrender.com?username=" + encodedUsername + "&email=" + encodedEmail;
        String frontendRedirectUrl = "https://ticketswap-frontend-koxq.onrender.com";
//        Cookie cookie = new Cookie(
//                "my_cookie",
//                "12345678"
//        );
//        cookie.setSecure(true);
//        response.addCookie(cookie);
//        response.addHeader("Set-Cookie", String.format("%s; SameSite=None", cookieToString(cookie)));
        response.sendRedirect(frontendRedirectUrl);
    }

    // Helper method to convert Cookie to a string
    private String cookieToString(Cookie cookie) {
        return String.format("%s=%s; Path=%s; %s %s",
                cookie.getName(),
                cookie.getValue(),
                cookie.getPath(),
                cookie.isHttpOnly() ? "HttpOnly;" : "",
                cookie.getSecure() ? "Secure;" : ""
        );
    }
}