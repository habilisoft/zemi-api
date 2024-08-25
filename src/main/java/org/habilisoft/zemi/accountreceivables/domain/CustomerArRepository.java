package org.habilisoft.zemi.accountreceivables.domain;

import org.habilisoft.zemi.customer.domain.CustomerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerArRepository extends JpaRepository<CustomerAr, CustomerId>{
}
