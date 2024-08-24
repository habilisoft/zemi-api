package org.habilisoft.zemi.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class TransactionalIdGenerator {
    private final JdbcClient jdbcClient;

    public TransactionalId generate(DocumentId documentId) {
        Long sequence = jdbcClient.sql("SELECT document_next_sequence(?)")
                .param(documentId.value())
                .query(Long.class)
                .single();
        return new TransactionalId(documentId, sequence);
    }
}
