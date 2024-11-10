package com.ticketswap.repository;

import com.ticketswap.model.Notification;
import com.ticketswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
    SELECT notif FROM notification notif
    WHERE notif.user = :user
    AND notif.seenByUser = false
    """)
    List<Notification> getUnseenNotificationsForUser(User user);
}
