package com.springjpabulk.repository;

import com.springjpabulk.domain.NoIdentityBatchTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoIdentityBatchTableRepository extends JpaRepository<NoIdentityBatchTable, Long> {
}
