package com.ticketswap.service;

import com.ticketswap.dto.user.UserDto;
import com.ticketswap.dto.user.UserEditDto;
import com.ticketswap.model.Category;
import com.ticketswap.model.User;
import com.ticketswap.repository.CategoryRepository;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public UserDto getProfile(User user) {
        return UserDto.map(user);
    }

    public void editProfile(User user, UserEditDto userEditDto) {
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setUsername(userEditDto.getUsername());

        if (userEditDto.getPreferredCategoryId() != null) {
            Long categoryId = userEditDto.getPreferredCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + categoryId + " not found."));
            user.setPreferredCategory(category);
        }

        userRepository.save(user);
    }
}
