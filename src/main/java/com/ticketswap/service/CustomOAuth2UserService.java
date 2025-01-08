package com.ticketswap.service;

import com.ticketswap.model.CustomOAuth2User;
import com.ticketswap.model.User;
import com.ticketswap.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String externalId = oAuth2User.getAttribute("sub"); // Google's unique ID
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");

        User user = userRepository.findByExternalId(externalId).orElseGet(() -> {
            User newUser = new User();
            newUser.setExternalId(externalId);
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setProfilePicUrl(pictureUrl);
            return userRepository.save(newUser);
        });

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
