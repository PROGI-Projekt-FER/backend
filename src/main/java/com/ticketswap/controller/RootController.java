package com.ticketswap.controller;

import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.model.User;
import com.ticketswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Api is running");
    }

}
