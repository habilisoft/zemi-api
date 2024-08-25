package org.habilisoft.zemi.taxesmanagement.customer.domain;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTaxRepository extends JpaRepository<CustomerTax, CustomerId>{
}
