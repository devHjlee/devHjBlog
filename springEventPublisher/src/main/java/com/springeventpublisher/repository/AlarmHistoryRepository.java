package com.springeventpublisher.repository;

import com.springeventpublisher.domain.AlarmHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmHistoryRepository extends JpaRepository<AlarmHistory,Long> {
}
