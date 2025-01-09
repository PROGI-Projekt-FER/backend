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
public class UserEditDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private Category preferredCategory;

    public static UserEditDto map(User user) {
        UserEditDto dto = new UserEditDto();
        dto.setUserId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setPreferredCategory(user.getPreferredCategory());
        return dto;
    }

    public User toEntity() {
        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPreferredCategory(preferredCategory);
        return user;
    }
}
