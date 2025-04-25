package com.event.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class EventManagementServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EventManagementServiceApplication.class, args);
  }

}
