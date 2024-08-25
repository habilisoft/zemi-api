package org.habilisoft.zemi.taxesmanagement.domain;

import org.habilisoft.zemi.sales.customer.domain.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTaxRepository extends JpaRepository<CustomerTax, CustomerId>{
}
