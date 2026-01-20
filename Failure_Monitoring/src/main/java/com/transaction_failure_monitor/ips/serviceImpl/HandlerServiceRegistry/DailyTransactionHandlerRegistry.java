package com.transaction_failure_monitor.ips.serviceImpl.HandlerServiceRegistry;

import com.transaction_failure_monitor.ips.service.handlerService.DailyTransactionHandlerService;
import com.transaction_failure_monitor.ips.util.TransactionType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyTransactionHandlerRegistry {
    private final Map<TransactionType, DailyTransactionHandlerService<?>> handlers = new HashMap<>();

    public DailyTransactionHandlerRegistry(List<DailyTransactionHandlerService<?>> handlerList) {
        handlerList.forEach(h -> handlers.put(h.getTransactionType(), h));
    }

    public DailyTransactionHandlerService<?> getHandler(TransactionType type) {
        return handlers.get(type);
    }

    public Collection<DailyTransactionHandlerService<?>> getAllHandlers() {
        return handlers.values();
    }


}
