package org.habilisoft.zemi.accountreceivables;

import org.habilisoft.zemi.sales.customer.domain.CustomerRegistered;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class AccountReceivablesService {
    @ApplicationModuleListener
    public void on(CustomerRegistered customerRegistered) {

    }
}
