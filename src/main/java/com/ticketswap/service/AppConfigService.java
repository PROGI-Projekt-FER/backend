package com.ticketswap.service;


import com.ticketswap.model.AppConfig;
import com.ticketswap.model.User;
import com.ticketswap.model.UserRole;
import com.ticketswap.repository.AppConfigRepository;
import com.ticketswap.repository.UserRepository;
import com.ticketswap.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppConfigService {
    private final String ticketCleanupPeriodId = "ticket_cleanup_period";

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private UserRepository userRepository;

    public int getTicketCleanupPeriodInSeconds() {
        AppConfig appConfig = appConfigRepository.findById(ticketCleanupPeriodId).orElseGet(() -> appConfigRepository.save(new AppConfig(ticketCleanupPeriodId, 86400)));
        return appConfig.getValue();
    }

    public void saveTicketCleanupPeriod(int periodInSeconds) {
        AppConfig appConfig = appConfigRepository.findById(ticketCleanupPeriodId).orElseGet(() -> appConfigRepository.save(new AppConfig(ticketCleanupPeriodId, periodInSeconds)));
        appConfig.setValue(periodInSeconds);
        appConfigRepository.save(appConfig);
    }

    public User makeUserAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUserRole(UserRole.ADMIN);
        return userRepository.save(user);
    }

    public User removeAdminFromUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUserRole(UserRole.REGULAR);
        return userRepository.save(user);
    }

}
