package com.ticketswap.controller;

import com.ticketswap.model.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/user")
    public OAuth2User getLoggedInUser(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }

    @GetMapping("/user/info")
    public String getUserInfo(@AuthenticationPrincipal CustomOAuth2User customUser) {
        String email = customUser.getAttribute("email");
        Collection<? extends GrantedAuthority> authorities = customUser.getAuthorities();
        return "User email: " + email + ", Roles: " + authorities;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("logged out");
    }

}
