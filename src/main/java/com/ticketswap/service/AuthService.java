package com.ticketswap.service;

import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.model.User;
import com.ticketswap.model.UserRole;
import com.ticketswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuth2User customUser) {
            return userRepository.findById(customUser.getUser().getId());
        }
        return Optional.empty();
    }

    public Optional<User> getLoggedInAdmin() {
        Optional<User> loggedInUser = getLoggedInUser();
        if (loggedInUser.isPresent() && loggedInUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return loggedInUser;
        } else {
            return Optional.empty();
        }
    }
}
