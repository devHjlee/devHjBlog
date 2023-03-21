package com.springeventpublisher.repository;

import com.springeventpublisher.domain.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory,Long> {
}
