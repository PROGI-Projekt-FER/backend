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
    private String firstName;
    private String lastName;
    private String username;
    private Long preferredCategoryId;

    public static UserEditDto map(User user) {
        UserEditDto dto = new UserEditDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setPreferredCategoryId(user.getPreferredCategory().getId());
        return dto;
    }

    public User toEntity(Category category) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPreferredCategory(category);
        return user;
    }
}
