package org.habilisoft.zemi.sales.sale.domain;

import org.habilisoft.zemi.shared.TransactionalId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, TransactionalId> {
}
