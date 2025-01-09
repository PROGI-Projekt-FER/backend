package com.ticketswap.dto.user;

import com.ticketswap.model.Category;
import com.ticketswap.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String profilePicUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Category preferredCategory;

    public static UserDto map(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getId());
        dto.setProfilePicUrl(user.getProfilePicUrl());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPreferredCategory(user.getPreferredCategory());
        return dto;
    }

    public User toEntity() {
        User user = new User();
        user.setId(userId);
        user.setProfilePicUrl(profilePicUrl);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPreferredCategory(preferredCategory);
        return user;
    }
}
