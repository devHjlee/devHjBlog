package com.springbootquartz.repository;

import com.springbootquartz.domain.QuartzHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuartzHistoryRepository extends JpaRepository<QuartzHistory, Long> {
}
