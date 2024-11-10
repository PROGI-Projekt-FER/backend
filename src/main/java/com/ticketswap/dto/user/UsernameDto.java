package com.ticketswap.dto.user;

import com.ticketswap.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsernameDto {
    private Long userId;
    private String username;

    public static UsernameDto map(User user) {
        UsernameDto dto = new UsernameDto();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public User toEntity() {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        return user;
    }
}
