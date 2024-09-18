package org.habilisoft.zemi.invoicing.domain;

import org.habilisoft.zemi.shared.TransactionalId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, TransactionalId> {
}
