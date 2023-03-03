package com.springjpabulk;

import com.springjpabulk.domain.IdentityBatchTable;
import com.springjpabulk.domain.NoIdentityBatchTable;
import com.springjpabulk.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class InsertTest {
    @Autowired
    NoIdentityBatchTableRepository noIdentityBatchTableRepository;
    @Autowired
    IdentityBatchTableRepository identityBatchTableRepository;
    @Autowired
    IdentityBatchRepository identityBatchRepository;

    @Test
    public void noIdentityJPABatch(){
        List<NoIdentityBatchTable> noIdentityBatchTables = new ArrayList<NoIdentityBatchTable>();
        for(int i = 1; i < 20000; i++){
            noIdentityBatchTables.add(NoIdentityBatchTable.builder().id(i).name("C"+i).build());
        }
        noIdentityBatchTableRepository.saveAll(noIdentityBatchTables);
    }
    @Test
    public void identityJPABatch(){
        List<IdentityBatchTable> identityBatchTables = new ArrayList<>();
        for(int i = 1; i < 20000; i++){
            IdentityBatchTable identityBatchTable = IdentityBatchTable.builder().name("A"+i).build();
            identityBatchTables.add(identityBatchTable);
        }
        identityBatchTableRepository.saveAll(identityBatchTables);
    }

    @Test
    public void identityJDBCBatch(){
        List<IdentityBatchTable> identityBatchTables = new ArrayList<>();
        for(int i = 1; i < 20000; i++){
            IdentityBatchTable identityBatchTable = IdentityBatchTable.builder().name("A"+i).build();
            identityBatchTables.add(identityBatchTable);
        }
        identityBatchRepository.saveAll(identityBatchTables);
    }

}
