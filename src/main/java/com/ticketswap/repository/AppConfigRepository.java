package com.ticketswap.repository;

import com.ticketswap.model.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppConfigRepository extends JpaRepository<AppConfig, String> {

}
