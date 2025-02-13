package com.ticketswap.controller;

import com.ticketswap.dto.swap.RequestDetailsDto;
import com.ticketswap.dto.ticket.TicketDetailsDto;
import com.ticketswap.dto.ticket.TicketHistoryDto;
import com.ticketswap.dto.user.AdminUserDto;
import com.ticketswap.dto.user.UserDto;
import com.ticketswap.dto.user.UserEditDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.UserService;
import com.ticketswap.util.NotAuthorizedException;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);
        List<AdminUserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/info")
    public ResponseEntity<UserDto> editProfile(@RequestBody @Valid UserEditDto userEditDto) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);

        userService.editProfile(loggedInUser, userEditDto);
        UserDto updatedProfile = userService.getProfile(loggedInUser);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/trade-history")
    public ResponseEntity<List<TicketHistoryDto>> getTradeHistory() {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        return ResponseEntity.ok(userService.getTradeHistory(loggedInUser));
    }

    @GetMapping("/{userId}/trade-history")
    public ResponseEntity<List<TicketHistoryDto>> getTradeHistoryForUser(@PathVariable Long userId) {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);

        return ResponseEntity.ok(userService.getTradeHistoryForUser(userId));
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDetailsDto>> getMyTickets() {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        return ResponseEntity.ok(userService.getMyTickets(loggedInUser));
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<List<RequestDetailsDto>> getPendingRequests() {
        User loggedInUser = authService.getLoggedInUser()
                .orElseThrow(NotLoggedInException::new);
        return ResponseEntity.ok(userService.getPendingRequests(loggedInUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }
}
