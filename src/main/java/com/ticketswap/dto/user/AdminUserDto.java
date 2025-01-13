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
public class AdminUserDto {
    private Long id;
    private String profilePicUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Category preferredCategory;

    public static AdminUserDto map(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setProfilePicUrl(user.getProfilePicUrl());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPreferredCategory(user.getPreferredCategory());
        return dto;
    }

    public User toEntity(Category category) {
        User user = new User();
        user.setId(id);
        user.setProfilePicUrl(profilePicUrl);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPreferredCategory(category);
        return user;
    }
}
