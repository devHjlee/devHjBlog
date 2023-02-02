package com.springbootquartz.service;

import com.springbootquartz.dto.QuartzBatchLogDto;
import com.springbootquartz.repository.QuartzBatchLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("quartzBatchLogService")
@Transactional
@RequiredArgsConstructor
public class QuartzBatchLogService {
    private final QuartzBatchLogRepository quartzBatchLogRepository;

    public void save(QuartzBatchLogDto qt) {
        log.debug(qt.toString());
        quartzBatchLogRepository.save(qt.toEntity());
    }
}
