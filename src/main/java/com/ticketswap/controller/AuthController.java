package com.ticketswap.controller;

import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.util.NotLoggedInException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;
    @GetMapping("/user")
    public Optional<User> getLoggedInUser() {
        return authService.getLoggedInUser();
    }

    @GetMapping("/user/info")
    public ResponseEntity<User> getUserInfo() {
        Optional<User> loggedInUser = authService.getLoggedInUser();
        if (loggedInUser.isEmpty()) throw new NotLoggedInException();
        return ResponseEntity.ok(loggedInUser.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("logged out");
    }

}
