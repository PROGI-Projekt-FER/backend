package com.ticketswap.controller;

import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.model.User;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.util.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dev")
@Profile("dev")
public class DevAuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login-as/{id}")
    public ResponseEntity<User> loginAs(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        User user = userOptional.get();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", user.getExternalId());
        attributes.put("given_name", user.getFirstName());
        attributes.put("family_name", user.getLastName());
        attributes.put("email", user.getEmail());
        attributes.put("name", user.getUsername());
        attributes.put("picture", user.getProfilePicUrl());

        CustomOAuth2User customUser = new CustomOAuth2User(user, attributes);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.ok(user);
    }
}
