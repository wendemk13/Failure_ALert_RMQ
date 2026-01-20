package com.transaction_failure_monitor.ips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class FailureAlertingApplication {
    static void main(String[] args) {
        SpringApplication.run(FailureAlertingApplication.class, args);
    }
}
