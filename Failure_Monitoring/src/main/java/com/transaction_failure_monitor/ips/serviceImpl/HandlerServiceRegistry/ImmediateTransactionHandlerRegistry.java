package com.transaction_failure_monitor.ips.serviceImpl.HandlerServiceRegistry;

import com.transaction_failure_monitor.ips.service.handlerService.ImmediateTransactionHandlerService;
import com.transaction_failure_monitor.ips.util.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImmediateTransactionHandlerRegistry {

    private final Map<TransactionType, ImmediateTransactionHandlerService<?>> handlers = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(ImmediateTransactionHandlerRegistry.class);

    public ImmediateTransactionHandlerRegistry(List<ImmediateTransactionHandlerService<?>> handlerList) {
        for (ImmediateTransactionHandlerService<?> handler : handlerList) {
            TransactionType type = handler.getTransactionType();
            handlers.put(type, handler);
            logger.info("Registered handler for: {}", type);
        }
    }

    public ImmediateTransactionHandlerService<?> getHandler(TransactionType type) {
        ImmediateTransactionHandlerService<?> handler = handlers.get(type);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for: " + type + ". Available: " + handlers.keySet());
        }
        return handler;
    }

    public ImmediateTransactionHandlerService<?> getHandler(String typeString) {
        try {
            TransactionType type = TransactionType.valueOf(typeString.toUpperCase());
            return getHandler(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + typeString);
        }
    }


}