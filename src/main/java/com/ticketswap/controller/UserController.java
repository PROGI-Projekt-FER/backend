package com.ticketswap.controller;

import com.ticketswap.dto.user.UserDto;
import com.ticketswap.dto.user.UserEditDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.UserService;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserDto> getProfile() {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        UserDto profile = userService.getProfile(loggedInUser);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/info")
    public ResponseEntity<UserDto> editProfile(@RequestBody @Valid UserEditDto userEditDto) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        userService.editProfile(loggedInUser, userEditDto);
        UserDto updatedProfile = userService.getProfile(loggedInUser);
        return ResponseEntity.ok(updatedProfile);
    }
}
