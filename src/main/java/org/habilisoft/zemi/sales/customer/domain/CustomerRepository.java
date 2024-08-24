package org.habilisoft.zemi.sales.customer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {
}
