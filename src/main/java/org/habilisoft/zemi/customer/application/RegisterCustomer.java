package org.habilisoft.zemi.customer.application;

import org.habilisoft.zemi.customer.domain.Address;
import org.habilisoft.zemi.customer.domain.Contact;
import org.habilisoft.zemi.customer.domain.CustomerType;
import org.habilisoft.zemi.shared.Command;

import java.time.LocalDateTime;

public record RegisterCustomer(String name,
                               CustomerType type,
                               Contact contact,
                               Address address,
                               LocalDateTime time, String user) implements Command { }
