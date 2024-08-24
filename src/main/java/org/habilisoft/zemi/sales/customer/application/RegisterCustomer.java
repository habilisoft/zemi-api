package org.habilisoft.zemi.sales.customer.application;

import org.habilisoft.zemi.sales.customer.domain.Address;
import org.habilisoft.zemi.sales.customer.domain.Contact;
import org.habilisoft.zemi.sales.customer.domain.CustomerType;
import org.habilisoft.zemi.sales.customer.domain.NcfType;
import org.habilisoft.zemi.shared.Command;
import org.habilisoft.zemi.shared.MonetaryAmount;

import java.time.LocalDateTime;
import java.util.Optional;

public record RegisterCustomer(String name,
                               CustomerType type,
                               Contact contact,
                               Address address,
                               Optional<NcfType> ncfType,
                               Optional<MonetaryAmount> creditLimit,
                               LocalDateTime time, String user) implements Command { }
