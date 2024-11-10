package com.ticketswap.service;

import com.ticketswap.dto.notification.NotificationDto;
import com.ticketswap.model.User;
import com.ticketswap.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationDto> getUnseenNotificationsForLoggedInUser(User loggedInUser) {
        return notificationRepository.getUnseenNotificationsForUser(loggedInUser).stream().map(NotificationDto::map).toList();
    }
}
