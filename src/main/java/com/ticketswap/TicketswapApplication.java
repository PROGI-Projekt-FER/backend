package com.ticketswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TicketswapApplication {

	public static void main(String[] args) {
		if (isLocalEnvironment()) {
			System.setProperty("spring.profiles.active", "dev");
			System.out.println("RUNNING IN DEV MODE");
		}
		SpringApplication.run(TicketswapApplication.class, args);
	}

	private static boolean isLocalEnvironment() {
		String hostname = System.getenv("HOSTNAME");
		System.out.println("HOSTNAME: " + hostname);
		return hostname == null || hostname.contains("local");
	}

}
