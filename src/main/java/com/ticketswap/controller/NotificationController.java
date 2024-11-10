package com.ticketswap.controller;

import com.ticketswap.dto.notification.NotificationDto;
import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotificationsForLoggedInUser(@AuthenticationPrincipal CustomOAuth2User loggedInUser) {
        if (loggedInUser == null) return ResponseEntity.ok(List.of());
        List<NotificationDto> notifs = notificationService.getUnseenNotificationsForLoggedInUser(loggedInUser.getUser());
        return ResponseEntity.ok(notifs);
    }
}
