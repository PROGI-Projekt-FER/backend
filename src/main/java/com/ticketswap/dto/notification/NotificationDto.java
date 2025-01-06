package com.ticketswap.dto.notification;

import com.ticketswap.model.Notification;
import com.ticketswap.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private String message;

    public static NotificationDto map(Notification notif) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notif.getId());
        dto.setMessage(notif.getMessage());
        return dto;
    }

    public Notification toEntity(User user) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setMessage(message);
        notification.setUser(user);
        notification.setSeenByUser(false);
        return notification;
    }
}
