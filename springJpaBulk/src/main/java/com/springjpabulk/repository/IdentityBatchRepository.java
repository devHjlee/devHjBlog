package com.springjpabulk.repository;


import com.springjpabulk.domain.IdentityBatchTable;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IdentityBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<IdentityBatchTable> identityBatchTables) {
        String sql = "INSERT INTO identity_batch_table (name) " +
                "VALUES (?)";
        jdbcTemplate.batchUpdate(sql,
                identityBatchTables,
                identityBatchTables.size(),
                (PreparedStatement ps, IdentityBatchTable identityBatchTable) -> {
                    ps.setString(1, identityBatchTable.getName());
                });
//        jdbcTemplate.batchUpdate(sql,
//                new BatchPreparedStatementSetter() {
//                    @Override
//                    public void setValues(PreparedStatement ps, int i) throws SQLException {
//                        ps.setString(1, identityBatchTables.get(i).getName());
//                    }
//
//                    @Override
//                    public int getBatchSize() {
//                        return identityBatchTables.size();
//                    }
//                });
    }

}
