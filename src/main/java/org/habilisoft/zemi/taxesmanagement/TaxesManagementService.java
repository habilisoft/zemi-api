package org.habilisoft.zemi.taxesmanagement;

import org.habilisoft.zemi.sales.customer.domain.CustomerRegistered;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class TaxesManagementService {
    @ApplicationModuleListener
    public void on(CustomerRegistered customerRegistered) {

    }
}
