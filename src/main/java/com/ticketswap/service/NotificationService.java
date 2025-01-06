package com.ticketswap.service;

import com.ticketswap.dto.notification.NotificationDto;
import com.ticketswap.model.Notification;
import com.ticketswap.model.User;
import com.ticketswap.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AuthService authService;

    public List<NotificationDto> getUnseenNotificationsForLoggedInUser(User loggedInUser) {
        return notificationRepository.getUnseenNotificationsForUser(loggedInUser).stream().map(NotificationDto::map).toList();
    }

    public List<NotificationDto> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
                .map(NotificationDto::map)
                .toList();
    }

    public Optional<NotificationDto> getNotificationById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.map(NotificationDto::map);
    }

    public NotificationDto dismissNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setSeenByUser(true);

        Notification updatedNotification = notificationRepository.save(notification);
        return NotificationDto.map(updatedNotification);
    }

    public NotificationDto createNotification(NotificationDto notificationDto) {
        var user = authService.getLoggedInUser()
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        Notification notification = notificationDto.toEntity(user);
        Notification savedNotification = notificationRepository.save(notification);
        return NotificationDto.map(savedNotification);
    }

    public NotificationDto updateNotification(Long id, NotificationDto notificationDto) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        existingNotification.setMessage(notificationDto.getMessage());
        Notification updatedNotification = notificationRepository.save(existingNotification);
        return NotificationDto.map(updatedNotification);
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);
    }
}
