package com.ticketswap.controller;

import com.ticketswap.dto.app_config.TicketCleanupPeriodDto;
import com.ticketswap.model.User;
import com.ticketswap.service.AppConfigService;
import com.ticketswap.service.AuthService;
import com.ticketswap.util.NotAuthorizedException;
import com.ticketswap.util.NotLoggedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class AppConfigController {

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private AuthService authService;

    @GetMapping("/ticket-cleanup-period")
    public ResponseEntity<TicketCleanupPeriodDto> getTicketCleanupPeriod() {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);
        return ResponseEntity.ok(new TicketCleanupPeriodDto(appConfigService.getTicketCleanupPeriodInSeconds()));
    }

    @PostMapping("/ticket-cleanup-period")
    public ResponseEntity<TicketCleanupPeriodDto> setTicketCleanupPeriod(@RequestBody TicketCleanupPeriodDto ticketCleanupPeriodDto) {
        authService.getLoggedInAdmin().orElseThrow(NotAuthorizedException::new);
        int period = ticketCleanupPeriodDto.getCleanupPeriodInSeconds();
        appConfigService.saveTicketCleanupPeriod(period);
        return ResponseEntity.ok(ticketCleanupPeriodDto);
    }

    @PostMapping("/make-me-admin")
    public ResponseEntity<User> makeUserAdmin() {
        User loggedInUser = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);
        return ResponseEntity.ok(appConfigService.makeUserAdmin(loggedInUser.getId()));
    }

    @PostMapping("/make-me-regular-user")
    public ResponseEntity<User> makeUserRegular() {
        User loggedInUser = authService.getLoggedInUser().orElseThrow(NotLoggedInException::new);
        return ResponseEntity.ok(appConfigService.removeAdminFromUser(loggedInUser.getId()));
    }

}
