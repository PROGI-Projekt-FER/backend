package com.ticketswap.controller;

import com.ticketswap.dto.notification.NotificationDto;
import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.service.AuthService;
import com.ticketswap.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotificationsForLoggedInUser() {
        var loggedInUser = authService.getLoggedInUser()
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        List<NotificationDto> unseenNotifications = notificationService.getUnseenNotificationsForLoggedInUser(loggedInUser);
        return ResponseEntity.ok(unseenNotifications);
    }

    @PutMapping("/{id}/dismiss")
    public ResponseEntity<NotificationDto> dismissNotification(@PathVariable Long id) {
        NotificationDto updatedNotification = notificationService.dismissNotification(id);
        return ResponseEntity.ok(updatedNotification);
    }
}
