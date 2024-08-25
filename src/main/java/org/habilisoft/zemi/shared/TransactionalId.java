package org.habilisoft.zemi.shared;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public record TransactionalId(DocumentId documentId, Long sequence) implements Serializable {
    public static TransactionalId of(DocumentId documentId, Long sequence) {
        return new TransactionalId(documentId, sequence);
    }
}
