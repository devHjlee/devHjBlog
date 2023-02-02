package com.springbootquartz.repository;

import com.springbootquartz.domain.QuartzBatchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartzBatchLogRepository extends JpaRepository<QuartzBatchLog, Long> {
}
