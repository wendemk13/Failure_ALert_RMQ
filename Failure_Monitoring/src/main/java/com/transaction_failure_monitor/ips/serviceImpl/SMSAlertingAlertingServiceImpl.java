package com.transaction_failure_monitor.ips.serviceImpl;

import com.transaction_failure_monitor.ips.service.SMSAlertingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SMSAlertingAlertingServiceImpl implements SMSAlertingService {
    private final Logger logger = LoggerFactory.getLogger(SMSAlertingAlertingServiceImpl.class);

    @Override
    public void sendSMS(String message) {
        try {
            logger.info("SMS sent successfully." + "\n" + message);
        } catch (Exception e) {
            logger.error("Failed to send the SMS", e);
        }
    }

}
