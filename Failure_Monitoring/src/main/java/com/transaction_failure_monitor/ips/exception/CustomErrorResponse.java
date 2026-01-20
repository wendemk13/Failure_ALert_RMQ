package com.transaction_failure_monitor.ips.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String message;

    public CustomErrorResponse(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

}